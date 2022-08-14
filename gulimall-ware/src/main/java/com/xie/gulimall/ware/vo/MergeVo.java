package com.xie.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @title: MergeVo
 * @Author Xie
 * @Date: 2022/8/14 16:19
 * @Version 1.0
 */
@Data
public class MergeVo {
    //purchaseId: 1, //整单id
    //items:[1,2,3,4] //合并项集合
    private Long purchaseId;
    private List<Long> items;
}
