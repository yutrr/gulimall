package com.xie.gulimall.ware.vo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

/**
 * @title: SkuHasStockVo
 * @Author Xie
 * @Date: 2022/9/20 20:55
 * @Version 1.0
 */
@Data
public class SkuHasStockVo
{
    private Long skuId;
    private Boolean hasStock;
}
