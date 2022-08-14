package com.xie.gulimall.ware.service.impl;

import com.xie.common.utils.R;
import com.xie.gulimall.ware.feign.ProductFeignService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.common.utils.PageUtils;
import com.xie.common.utils.Query;

import com.xie.gulimall.ware.dao.WareSkuDao;
import com.xie.gulimall.ware.entity.WareSkuEntity;
import com.xie.gulimall.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    WareSkuDao wareSkuDao;

    @Autowired
    ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        /**
         * skuId: 1
         * wareId: 1
         */
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        String skuId= (String) params.get("skuId");
        if (StringUtils.isNotEmpty(skuId)){
            wrapper.eq("sku_id",skuId);
        }

        String wareId= (String) params.get("wareId");
        if (StringUtils.isNotEmpty(wareId)){
            wrapper.eq("ware_id",wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }



    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //1. 判断如果没有库存记录新增
        List<WareSkuEntity> entities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (entities==null||entities.size()==0){
            WareSkuEntity skuEntity=new WareSkuEntity();
            skuEntity.setSkuId(skuId);
            skuEntity.setStock(skuNum);
            skuEntity.setWareId(wareId);
            skuEntity.setStockLocked(0);
            //TODO 远程查询sku的名字,如果失败，整个事务无需回滚
            //1.自己catch掉异常
            //TODO 还可以用什么办法让异常出现后不回滚？高级
            try{
                R info = productFeignService.info(skuId);
                Map<String, Object> data = (Map<String, Object>) info.get("skuInfo");
                if (info.getCode()==0){
                    skuEntity.setSkuName((String) data.get("skuName"));
                }
            }catch (Exception e){

            }


            wareSkuDao.insert(skuEntity);
        }else {
            wareSkuDao.addStock(skuId,wareId,skuNum);
        }


    }

}