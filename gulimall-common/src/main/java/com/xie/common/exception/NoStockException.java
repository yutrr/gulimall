package com.xie.common.exception;

/**
 * @title: NoStockException
 * @Author Xie
 * @Date: 2023/2/3 22:49
 * @Version 1.0
 */
/**
 * 无库存抛出的异常
 * @author zr
 * @date 2021/12/23 22:19
 */
public class NoStockException extends RuntimeException{
    private Long skuId;
    public NoStockException(Long skuId){
        super("商品id:"+skuId+";没有足够的库存了");
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public NoStockException(String msg){
        super(msg);
    }
}
