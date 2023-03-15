package com.xie.gulimall.order.listener;

import com.rabbitmq.client.Channel;
import com.xie.gulimall.order.entity.OrderEntity;
import com.xie.gulimall.order.service.OrderService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @title: OrderCloseListener
 * @Author Xie
 * @Date: 2023/2/28 20:08
 * @Version 1.0
 *
 */
/**
 * 定时关闭订单
 *
 */
@Service
@RabbitListener(queues = "order.release.order.queue")
public class OrderCloseListener {

    @Autowired
    OrderService orderService;

    @RabbitHandler
    public void listen(OrderEntity orderEntity, Channel channel, Message message) throws IOException {
        System.out.println("收到过期订单消息,准备关闭订单:------>"+orderEntity.getOrderSn());
        try {
            orderService.closeOrder(orderEntity);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }

    }
}
