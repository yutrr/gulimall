package com.xie.gulimall.product;

import com.xie.gulimall.product.service.AttrGroupService;
import com.xie.gulimall.product.service.CategoryService;
import com.xie.gulimall.product.vo.SpuItemAttrGroupVo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Var;

import org.junit.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 1.引入oss-starter
 * 2.配置key,endpoint相关信息即可
 * 3.使用OSSClient 进行相关操作
 */
@Slf4j
@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    CategoryService categoryService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    AttrGroupService attrGroupService;

    @Test
    public void test1(){
        List<SpuItemAttrGroupVo> attrGroupWithAttrsBySpuId = attrGroupService.getAttrGroupWithAttrsBySpuId(5L, 225L);
        System.out.println(attrGroupWithAttrsBySpuId);

    }
    @Test
    public void redisson(){
        System.out.println(redissonClient);
    }

    @Test
    public void testStringRedisTemplate(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        //保存
        ops.set("hello","world_"+ UUID.randomUUID().toString());
        //查询
        String hello = ops.get("hello");
        System.out.println(" 之前保存的数据是 " + hello);
    }

    @Test
    public void testFindPath(){
        Long[] catelogPath = categoryService.findCatelogPath(231L);
        log.info("完整路径：{}", Arrays.asList(catelogPath));
    }

    @Test
    void contextLoads() {
    }
    @Test
    public void testUpload() {
        Integer a=new Integer(2);
        Integer b=new Integer(2);
        if (a==b){
            System.out.println(a==b);
        }

    }


}
