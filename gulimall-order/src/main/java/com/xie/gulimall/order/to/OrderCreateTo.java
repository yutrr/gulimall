package com.xie.gulimall.order.to;

import com.xie.gulimall.order.entity.OrderEntity;
import com.xie.gulimall.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @title: OrderCreateTo
 * @Author Xie
 * @Date: 2023/2/1 20:08
 * @Version 1.0
 */
@Data
public class OrderCreateTo {
    private OrderEntity order;  // 订单
    private List<OrderItemEntity> orderItems; // 订单项
    /** 订单计算的应付价格 **/
    private BigDecimal payPrice;
    /** 运费 **/
    private BigDecimal fare;
}
