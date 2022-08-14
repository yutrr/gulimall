package com.xie.gulimall.product.vo;

import lombok.Data;

/**
 * @title: AttrRespVo
 * @Author Xie
 * @Date: 2022/8/11 16:52
 * @Version 1.0
 */
@Data
public class AttrRespVo extends AttrVo{
    /**
     * "catelogName": "手机/数码/手机", //所属分类名字
     * 			"groupName": "主体", //所属分组名字
     */

    private String catelogName;
    private String groupName;

    private Long[] catelogPath;

}
