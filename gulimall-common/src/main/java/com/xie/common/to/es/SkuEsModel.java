package com.xie.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @title: SkuEsModel
 * @Author Xie
 * @Date: 2022/9/17 0:47
 * @Version 1.0
 */
public class SkuEsModel {
    private Long skuId;
    private Long spuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;//销量
    private Boolean hasStock;//是否拥有库存
    private Long hotScore;//热度评分
    private Long brandId;//品牌id
    private Long catalogId;//分类id
    private String brandName;//品牌名
    private String brandImg;//图片
    private String catalogName;//分类名
    private List<Attrs> attrs;

    @Override
    public String toString() {
        return "SkuEsModel{" +
                "skuId=" + skuId +
                ", spuId=" + spuId +
                ", skuTitle='" + skuTitle + '\'' +
                ", skuPrice=" + skuPrice +
                ", skuImg='" + skuImg + '\'' +
                ", saleCount=" + saleCount +
                ", hasStock=" + hasStock +
                ", hotScore=" + hotScore +
                ", brandId=" + brandId +
                ", catalogId=" + catalogId +
                ", brandName='" + brandName + '\'' +
                ", brandImg='" + brandImg + '\'' +
                ", catalogName='" + catalogName + '\'' +
                ", attrs=" + attrs +
                '}';
    }

    public SkuEsModel() {
    }

    public SkuEsModel(Long skuId, Long spuId, String skuTitle, BigDecimal skuPrice, String skuImg, Long saleCount, Boolean hasStock, Long hotScore, Long brandId, Long catalogId, String brandName, String brandImg, String catalogName, List<Attrs> attrs) {
        this.skuId = skuId;
        this.spuId = spuId;
        this.skuTitle = skuTitle;
        this.skuPrice = skuPrice;
        this.skuImg = skuImg;
        this.saleCount = saleCount;
        this.hasStock = hasStock;
        this.hotScore = hotScore;
        this.brandId = brandId;
        this.catalogId = catalogId;
        this.brandName = brandName;
        this.brandImg = brandImg;
        this.catalogName = catalogName;
        this.attrs = attrs;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getSkuTitle() {
        return skuTitle;
    }

    public void setSkuTitle(String skuTitle) {
        this.skuTitle = skuTitle;
    }

    public BigDecimal getSkuPrice() {
        return skuPrice;
    }

    public void setSkuPrice(BigDecimal skuPrice) {
        this.skuPrice = skuPrice;
    }

    public String getSkuImg() {
        return skuImg;
    }

    public void setSkuImg(String skuImg) {
        this.skuImg = skuImg;
    }

    public Long getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Long saleCount) {
        this.saleCount = saleCount;
    }

    public Boolean getHasStock() {
        return hasStock;
    }

    public void setHasStock(Boolean hasStock) {
        this.hasStock = hasStock;
    }

    public Long getHotScore() {
        return hotScore;
    }

    public void setHotScore(Long hotScore) {
        this.hotScore = hotScore;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandImg() {
        return brandImg;
    }

    public void setBrandImg(String brandImg) {
        this.brandImg = brandImg;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public List<Attrs> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<Attrs> attrs) {
        this.attrs = attrs;
    }


    public static class Attrs {
        private Long attrId;
        private String attrName;
        private String attrValue;

        @Override
        public String toString() {
            return "Attrs{" +
                    "attrId=" + attrId +
                    ", attrName='" + attrName + '\'' +
                    ", attrValue='" + attrValue + '\'' +
                    '}';
        }

        public Long getAttrId() {
            return attrId;
        }

        public void setAttrId(Long attrId) {
            this.attrId = attrId;
        }

        public String getAttrName() {
            return attrName;
        }

        public void setAttrName(String attrName) {
            this.attrName = attrName;
        }

        public String getAttrValue() {
            return attrValue;
        }

        public void setAttrValue(String attrValue) {
            this.attrValue = attrValue;
        }

        public Attrs() {
        }

        public Attrs(Long attrId, String attrName, String attrValue) {
            this.attrId = attrId;
            this.attrName = attrName;
            this.attrValue = attrValue;
        }
    }
}
