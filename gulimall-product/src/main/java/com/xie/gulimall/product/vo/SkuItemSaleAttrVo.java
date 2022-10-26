package com.xie.gulimall.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @title: SkuItemSaleAttrVo
 * @Author Xie
 * @Date: 2022/11/7 22:01
 * @Version 1.0
 */
@Data
public class SkuItemSaleAttrVo {
    private Long attrId;
    private String attrName;
    private List<AttrValueWithSkuIdVo> attrValues;
}
