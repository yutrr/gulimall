package com.xie.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.common.utils.PageUtils;
import com.xie.gulimall.product.entity.CategoryBrandRelationEntity;

import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author xiehaijun
 * @email xiehaijun@gmail.com
 * @date 2022-07-07 17:58:59
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

