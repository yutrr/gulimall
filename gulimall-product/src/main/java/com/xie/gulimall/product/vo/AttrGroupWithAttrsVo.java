package com.xie.gulimall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.xie.gulimall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @title: AttrGroupWithAttrVo
 * @Author Xie
 * @Date: 2022/8/12 20:56
 * @Version 1.0
 */
@Data
public class AttrGroupWithAttrsVo {
    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;
}
