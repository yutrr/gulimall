package com.xie.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.common.utils.PageUtils;
import com.xie.gulimall.coupon.entity.HomeAdvEntity;

import java.util.Map;

/**
 * 首页轮播广告
 *
 * @author xiehaijun
 * @email xiehaijun@gmail.com
 * @date 2022-07-07 18:11:44
 */
public interface HomeAdvService extends IService<HomeAdvEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

