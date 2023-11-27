package com.xie.gulimall.order.dao;

import com.xie.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 *
 * @author xiehaijun
 * @email xiehaijun@gmail.com
 * @date 2022-07-07 18:06:25
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {

}
