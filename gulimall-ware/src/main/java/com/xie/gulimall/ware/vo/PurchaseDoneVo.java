package com.xie.gulimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @title: PurchaseDoneVo
 * @Author Xie
 * @Date: 2022/8/14 20:08
 * @Version 1.0
 */
@Data
public class PurchaseDoneVo {
    @NotNull
    private Long id;//采购单id

    private List<PurchaseItemDoneVo> items;
}
