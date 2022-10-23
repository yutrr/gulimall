package com.xie.gulimall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xie.gulimall.product.service.CategoryBrandRelationService;
import com.xie.gulimall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.common.utils.PageUtils;
import com.xie.common.utils.Query;

import com.xie.gulimall.product.dao.CategoryDao;
import com.xie.gulimall.product.entity.CategoryEntity;
import com.xie.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    //本地缓存
    //private Map<String, Object> cache=new HashMap<>();
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1.查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        //2.组装成父子的树形结构
        //2.1)找到所有的父分类
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map((menu)->{
            menu.setChildren(getChildrens(menu,entities));
            return menu;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort()==null?0: menu1.getSort())- (menu2.getSort()==null?0: menu2.getSort());
        }).collect(Collectors.toList());
        return level1Menus;
    }

    @Override
    public void removeMenusByIds(List<Long> asList) {
        //TODO 1.检查当前删除的菜单，是否被别的地方引用

        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    //[2/23/225]
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths=new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return  parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有关联的数据
     * @param category
     */
    @Transactional
    @Override
    public void updateCasade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    //查询1级分类
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        long l = System.currentTimeMillis();
        System.out.println("调用了 getLevel1Categorys  查询了数据库........【一级分类】");
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        System.out.println("消耗时间："+(System.currentTimeMillis()-l));
        return categoryEntities;
    }

    //TODO 堆外内存溢出异常:OutOfDirectMemoryError
    //1）springboot2.0以后默认使用lettuce作为操作redis的客户端。他使用netty进行网络通信
    //2）lettuce的bug导致netty堆外内存溢出 -Xmx1024m；netty如果没有指定堆外内存，默认使用-Xmx1024m，跟jvm设置的一样【迟早会出异常】
    //   可以通过Dio.netty.maxDirectMemory进行设置
    //  解决方案：不能使用Dio.netty.maxDirectMemory只去调堆外内存
    //1）升级lettuce客户端。 2）切换使用jedis
    //redisTemplate
    //lettuce,jedis操作redis的底层客户端。spring再次封装redisTemplate；
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        //给缓存中放json字符串，然后拿出的json字符串，还用逆转为能用的对象类型，【序列化与反序列化】

        /**
         * 1.空结果缓存:解决缓存穿透（去查一个不存在的数据）
         * 2.设置过期时间（加随机值）：解决缓存雪崩（数据在同一时间内全部过期了）
         * 3.加锁：解决缓存击穿
         */


        //1.加入缓存逻辑,缓存中存的数据是json字符串
        //JSON跨语言，跨平台兼容
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        if (BeanUtil.isEmpty(catalogJson)){
            //2.缓存中没有，查询数据库
            System.out.println("缓存不命中....将要查询数据库.....");
            Map<String, List<Catelog2Vo>> catalogJsonFromDb = getCatalogJsonFromDbWithRedisLock();

            return catalogJsonFromDb;
        }

        System.out.println("缓存命中....直接返回......");
        //转为我们指定的对象
       Map<String,List<Catelog2Vo>> result= JSON.parseObject(catalogJson,new TypeReference<Map<String,List<Catelog2Vo>>>(){});
        return result;
    }

    //采用Redis分布式锁
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithRedisLock() {

        //1.占分布式锁，去redis占坑
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", "111",300,TimeUnit.SECONDS);
        if (lock){
            System.out.println("获取分布式锁成功....");
            //加锁成功...执行业务
            //2.设置过期时间,必须和加锁是同步的
            //redisTemplate.expire("lock",30,TimeUnit.SECONDS);
            //获取值对比+对比成功删除=原子操作  lua脚本解锁
//            String lockValue = redisTemplate.opsForValue().get("lock");
//            if (uuid.equals(lockValue)){
//                //删除我自己的锁
//                redisTemplate.delete("lock");//删除锁
//            }
            Map<String, List<Catelog2Vo>> dataFromDb;
            try {
                dataFromDb = getDataFromDb();
            }finally {
                String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1]\n" +
                        "then\n" +
                        "    return redis.call('del',KEYS[1])\n" +
                        "else\n" +
                        "    return 0\n" +
                        "end";
                //删除锁
                Long lock1 = redisTemplate.execute(new DefaultRedisScript<Long>(luaScript, Long.class),
                        Arrays.asList("lock"), uuid);
            }

            return dataFromDb;
        }else{
            //加锁失败...重试。synchronized()
            //休眠100ms重试
            System.out.println("获取分布式锁失败....等待重试");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getCatalogJsonFromDbWithRedisLock();//自旋的方式
        }

    }

    private Map<String,List<Catelog2Vo>> getDataFromDb(){
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        if (!BeanUtil.isEmpty(catalogJson)){
            //缓存不为null直接返回
            Map<String,List<Catelog2Vo>> result= JSON.parseObject(catalogJson,new TypeReference<Map<String,List<Catelog2Vo>>>(){});
            return result;
        }
        System.out.println("查询了数据库......");
        List<CategoryEntity> selectList = baseMapper.selectList(null);

        //1.查询所有1级分类
        List<CategoryEntity> level1Categorys = getParent_cid(selectList,0L);
        //1),所有1级分类
        //2.封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1.每一个的一级分类，查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntities = getParent_cid(selectList,v.getCatId());
            //2.封装上面的结果
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //1.找当前二级分类的三级分类封装成vo
                    List<CategoryEntity> level3Catelog= getParent_cid(selectList,l2.getCatId());
                    if (level3Catelog!=null){
                        List<Catelog2Vo.Catelog3Vo> collect = level3Catelog.stream().map(l3 -> {
                            //2.封装成指定的格式
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());

                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));

        //3.查到数据再放入缓存,将对象转为json放在缓存中
        String s = JSON.toJSONString(parent_cid);
        redisTemplate.opsForValue().set("catalogJson",s,1, TimeUnit.DAYS);
        return parent_cid;
    }


    //采用本地锁的方式
    //从数据库查询并封装分类工具
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithLocalLock() {
        //1.如果缓存中有就用缓存中的数据
        /*Map<String, List<Catelog2Vo>> catalogJson= (Map<String, List<Catelog2Vo>>) cache.get("catalogJson");
        if (cache.get("catalogJson")==null){
            //调用业务
            //返回数据又放入缓存
            cache.put("catalogJson",parent_cid);
        }
        return catalogJson;*/
        //只要是同一把锁，就能锁住需要这个锁的所有线程
        //1.synchronized (this):SpringBoot所有的组件在容器中都是单例的。
        //TODO 本地锁：synchronized，JUC(Lock),在分布式的情况下，想要锁住所有，必须使用分布式锁
        synchronized (this){
            //得到锁以后，我们应该再去缓存中确定一次，如果没有才需要继续查询
            String catalogJson = redisTemplate.opsForValue().get("catalogJson");
            if (!BeanUtil.isEmpty(catalogJson)){
                //缓存不为null直接返回
                Map<String,List<Catelog2Vo>> result= JSON.parseObject(catalogJson,new TypeReference<Map<String,List<Catelog2Vo>>>(){});
                return result;
            }
            System.out.println("查询了数据库......");
            /**
             * 1.将数据库多次查询变为一次
             */
            List<CategoryEntity> selectList = baseMapper.selectList(null);

            //1.查询所有1级分类
            List<CategoryEntity> level1Categorys = getParent_cid(selectList,0L);
            //1),所有1级分类
            //2.封装数据
            Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                //1.每一个的一级分类，查到这个一级分类的二级分类
                List<CategoryEntity> categoryEntities = getParent_cid(selectList,v.getCatId());
                //2.封装上面的结果
                List<Catelog2Vo> catelog2Vos = null;
                if (categoryEntities != null) {
                    catelog2Vos = categoryEntities.stream().map(l2 -> {
                        Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                        //1.找当前二级分类的三级分类封装成vo
                        List<CategoryEntity> level3Catelog= getParent_cid(selectList,l2.getCatId());
                        if (level3Catelog!=null){
                            List<Catelog2Vo.Catelog3Vo> collect = level3Catelog.stream().map(l3 -> {
                                //2.封装成指定的格式
                                Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());

                                return catelog3Vo;
                            }).collect(Collectors.toList());
                            catelog2Vo.setCatalog3List(collect);
                        }
                        return catelog2Vo;
                    }).collect(Collectors.toList());
                }

                return catelog2Vos;
            }));

            //3.查到数据再放入缓存,将对象转为json放在缓存中
            String s = JSON.toJSONString(parent_cid);
            redisTemplate.opsForValue().set("catalogJson",s,1, TimeUnit.DAYS);
            return parent_cid;
        }
    }

    /**
     * 查询出父ID为 parent_cid的List集合
     */
    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList,Long parent_cid) {
        List<CategoryEntity> collect = selectList.stream().filter(item -> item.getParentCid() == parent_cid).collect(Collectors.toList());
        //return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
        return collect;
    }

    //225，23，2
    private List<Long> findParentPath(Long catelogId,List<Long> paths){
        //1.收集当前节点Id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }

    //递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all){
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            //1.找到子菜单
            return categoryEntity.getParentCid().longValue() == root.getCatId().longValue();
        }).map(categoryEntity -> {
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            //2.菜单的排序
            return (menu1.getSort()==null?0: menu1.getSort())- (menu2.getSort()==null?0: menu2.getSort());
        }).collect(Collectors.toList());
        return children;
    }
}