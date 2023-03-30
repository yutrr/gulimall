package com.xie.gulimall.order.listener;

import com.rabbitmq.client.Channel;
import com.xie.common.to.mq.SeckillOrderTo;
import com.xie.gulimall.order.entity.OrderEntity;
import com.xie.gulimall.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @title: OrderSeckillListener
 * @Author Xie
 * @Date: 2023/3/30 20:21
 * @Version 1.0
 */
@Slf4j
@RabbitListener(queues = "order.seckill.order.queue")
@Component
public class OrderSeckillListener {
    @Autowired
    OrderService orderService;

    @RabbitHandler
    public void listen(SeckillOrderTo seckillOrderTo, Channel channel, Message message) throws IOException {
        log.info("准备创建秒杀单的详细信息...");
        try {
            orderService.createSeckillOrder(seckillOrderTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }

    }
}
