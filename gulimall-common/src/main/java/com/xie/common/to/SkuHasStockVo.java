package com.xie.common.to;


/**
 * @title: SkuHasStockVo
 * @Author Xie
 * @Date: 2022/9/20 20:55
 * @Version 1.0
 */
public class SkuHasStockVo
{
    private Long skuId;
    private Boolean hasStock;

    @Override
    public String toString() {
        return "SkuHasStockVo{" +
                "skuId=" + skuId +
                ", hasStock=" + hasStock +
                '}';
    }

    public SkuHasStockVo() {
    }

    public SkuHasStockVo(Long skuId, Boolean hasStock) {
        this.skuId = skuId;
        this.hasStock = hasStock;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Boolean getHasStock() {
        return hasStock;
    }

    public void setHasStock(Boolean hasStock) {
        this.hasStock = hasStock;
    }
}
