package com.xie.gulimall.order.vo;

import lombok.Data;

/**
 * @title: SkuStockVo
 * @Author Xie
 * @Date: 2023/1/10 21:44
 * @Version 1.0
 */
@Data
public class SkuStockVo {
    private Long skuId;
    private Boolean hasStock;
}
