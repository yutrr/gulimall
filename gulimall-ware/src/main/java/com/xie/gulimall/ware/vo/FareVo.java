package com.xie.gulimall.ware.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @title: FareVo
 * @Author Xie
 * @Date: 2023/1/30 22:17
 * @Version 1.0
 */
@Data
public class FareVo {
    private MemberAddressVo address;
    private BigDecimal fare;
}
