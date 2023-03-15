package com.xie.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.common.to.SkuHasStockVo;
import com.xie.common.to.mq.OrderTo;
import com.xie.common.to.mq.StockLockedTo;
import com.xie.common.utils.PageUtils;
import com.xie.gulimall.ware.entity.WareSkuEntity;
import com.xie.gulimall.ware.vo.WareSkuLockVo;


import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author xiehaijun
 * @email xiehaijun@gmail.com
 * @date 2022-07-07 18:07:53
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    Boolean orderLockStock(WareSkuLockVo lockVo);

    /**
     * 解锁库存
     * @param to
     */
    void unlockStock(StockLockedTo to);

    void unlockStock(OrderTo orderTo);
}

