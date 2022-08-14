package com.xie.gulimall.ware.vo;

import lombok.Data;

/**
 * @title: PurchaseItemDoneV
 * @Author Xie
 * @Date: 2022/8/14 20:11
 * @Version 1.0
 */
@Data
public class PurchaseItemDoneVo {
    //{itemId:1,status:4,reason:""}
    private Long itemId;
    private Integer status;
    private String reason;
}
