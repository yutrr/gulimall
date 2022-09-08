package com.xie.common.to;


import java.math.BigDecimal;
import java.util.List;

/**
 * @title: SkuReductionTo
 * @Author Xie
 * @Date: 2022/8/13 16:19
 * @Version 1.0
 */

public class SkuReductionTo {
    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;

    public SkuReductionTo() {
    }

    public SkuReductionTo(Long skuId, int fullCount, BigDecimal discount, int countStatus, BigDecimal fullPrice, BigDecimal reducePrice, int priceStatus, List<MemberPrice> memberPrice) {
        this.skuId = skuId;
        this.fullCount = fullCount;
        this.discount = discount;
        this.countStatus = countStatus;
        this.fullPrice = fullPrice;
        this.reducePrice = reducePrice;
        this.priceStatus = priceStatus;
        this.memberPrice = memberPrice;
    }

    @Override
    public String toString() {
        return "SkuReductionTo{" +
                "skuId=" + skuId +
                ", fullCount=" + fullCount +
                ", discount=" + discount +
                ", countStatus=" + countStatus +
                ", fullPrice=" + fullPrice +
                ", reducePrice=" + reducePrice +
                ", priceStatus=" + priceStatus +
                ", memberPrice=" + memberPrice +
                '}';
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public int getFullCount() {
        return fullCount;
    }

    public void setFullCount(int fullCount) {
        this.fullCount = fullCount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public int getCountStatus() {
        return countStatus;
    }

    public void setCountStatus(int countStatus) {
        this.countStatus = countStatus;
    }

    public BigDecimal getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(BigDecimal fullPrice) {
        this.fullPrice = fullPrice;
    }

    public BigDecimal getReducePrice() {
        return reducePrice;
    }

    public void setReducePrice(BigDecimal reducePrice) {
        this.reducePrice = reducePrice;
    }

    public int getPriceStatus() {
        return priceStatus;
    }

    public void setPriceStatus(int priceStatus) {
        this.priceStatus = priceStatus;
    }

    public List<MemberPrice> getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(List<MemberPrice> memberPrice) {
        this.memberPrice = memberPrice;
    }
}
