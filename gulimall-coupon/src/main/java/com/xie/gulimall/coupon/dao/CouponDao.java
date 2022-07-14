package com.xie.gulimall.coupon.dao;

import com.xie.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author xiehaijun
 * @email xiehaijun@gmail.com
 * @date 2022-07-07 18:11:44
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
