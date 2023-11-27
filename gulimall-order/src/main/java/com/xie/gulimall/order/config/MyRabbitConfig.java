package com.xie.gulimall.order.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


/**
 * @title: MyRabbitConfig
 * @Author Xie
 * @Date: 2022/12/27 20:58
 * @Version 1.0
 */
@Configuration
public class MyRabbitConfig {


    // @Autowired 不要自动注入，会循环依赖
    private RabbitTemplate rabbitTemplate;

    @Primary
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setMessageConverter(messageConverter());
        initRabbitTemplate();
        return rabbitTemplate;
    }


    //解决循环依赖的问题，重新创建一个构造器
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 定制RabbitTemplate
     * 1、服务收到消息就会回调
     * 1、spring.rabbitmq.publisher-confirm-type: correlated
     * 2、设置确认回调
     * 2、消息正确抵达队列就会进行回调
     * 1、spring.rabbitmq.publisher-returns: true
     * spring.rabbitmq.template.mandatory: true
     * 2、设置确认回调ReturnCallback
     * <p>
     * 3、消费端确认(保证每个消息都被正确消费，此时才可以broker删除这个消息)
     * spring:
     * rabbitmq:
     * listener:
     * simple:
     * acknowledge-mode: manual 手动签收
     * 1.默认是自动确认的，只要消息接收到，客户端会自动确认，服务端就会移除这个消息
     * 问题：
     * 我们收到很多消息，自动回复给服务器ack,只有一个处理成功，宕机了。发生消息丢失；
     * 消费者手动确认模式。只要我们没有明确告诉MQ,货物被签收。没有Ack，
     * 消息就是一直unacked状态。及时Consumer宕机，会重新变为Ready，下一次有新的Consumer连接进来就发给他
     * 2.如何签收
     * channel.basicAck(deliveryTag, false);签收，业务成功完成就应该签收
     * channel.basicNack(deliveryTag,false,false);拒签，业务失败，拒签
     */
    // @PostConstruct  //MyRabbitConfig对象创建完成以后，执行这个方法
    public void initRabbitTemplate() {

        /**
         * 1、只要消息抵达Broker就ack=true
         * correlationData：当前消息的唯一关联数据(这个是消息的唯一id)
         * ack：消息是否成功收到
         * cause：失败的原因
         */
        //设置确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> System.out.println("confirm...correlationData[" + correlationData + "]==>ack:[" + ack + "]==>cause:[" + cause + "]"));


        /**
         * 只要消息没有投递给指定的队列，就触发这个失败回调
         * message：投递失败的消息详细信息
         * replyCode：回复的状态码
         * replyText：回复的文本内容
         * exchange：当时这个消息发给哪个交换机
         * routingKey：当时这个消息用哪个路邮键
         */
        // 需要修改数据库 消息的状态【后期定期重发消息】
        /*rabbitTemplate.setReturnsCallback(returned -> System.out.println("Fail Message["+returned.getMessage()+"]==>replyCode["+returned.getReplyCode()+"]" +
                "==>replyText["+returned.getReplyText()+"]==>exchange["+returned.getExchange()+"]==>routingKey["+returned.getRoutingKey()+"]"));*/
        //已经过时
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("Fail Message[" + message + "]==>replyCode[" + replyCode + "]" +
                    "==>replyText[" + replyText + "]==>exchange[" + exchange + "]==>routingKey[" + routingKey + "]");
        });
    }

}
