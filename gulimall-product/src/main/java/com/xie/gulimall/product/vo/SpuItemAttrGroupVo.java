package com.xie.gulimall.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @title: SpuItemAttrGroupVo
 * @Author Xie
 * @Date: 2022/11/7 22:00
 * @Version 1.0
 */
@ToString
@Data
public class SpuItemAttrGroupVo {
    private String groupName;
    private List<Attr> attrs;
}
