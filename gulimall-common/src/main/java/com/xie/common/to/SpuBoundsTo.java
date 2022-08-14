package com.xie.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @title: SpuBoundsTo
 * @Author Xie
 * @Date: 2022/8/13 16:01
 * @Version 1.0
 */

public class SpuBoundsTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;

    public SpuBoundsTo(Long spuId, BigDecimal buyBounds, BigDecimal growBounds) {
        this.spuId = spuId;
        this.buyBounds = buyBounds;
        this.growBounds = growBounds;
    }

    public SpuBoundsTo() {
    }

    @Override
    public String toString() {
        return "SpuBoundsTo{" +
                "spuId=" + spuId +
                ", buyBounds=" + buyBounds +
                ", growBounds=" + growBounds +
                '}';
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public BigDecimal getBuyBounds() {
        return buyBounds;
    }

    public void setBuyBounds(BigDecimal buyBounds) {
        this.buyBounds = buyBounds;
    }

    public BigDecimal getGrowBounds() {
        return growBounds;
    }

    public void setGrowBounds(BigDecimal growBounds) {
        this.growBounds = growBounds;
    }
}
