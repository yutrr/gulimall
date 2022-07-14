package com.xie.gulimall.order.dao;

import com.xie.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author xiehaijun
 * @email xiehaijun@gmail.com
 * @date 2022-07-07 18:06:25
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
