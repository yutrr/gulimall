package com.xie.gulimall.product.vo;

import lombok.Data;

/**
 * @title: BrandVo
 * @Author Xie
 * @Date: 2022/8/12 18:50
 * @Version 1.0
 */
public class BrandVo {
    /**
     * "brandId": 0,
     * "brandName": "string",
     */
    private Long brandId;
    private String brandName;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
