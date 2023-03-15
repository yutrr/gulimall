package com.xie.gulimall.order;

import com.xie.gulimall.order.entity.OrderEntity;
import com.xie.gulimall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.UUID;

@Slf4j
@SpringBootTest
class GulimallOrderApplicationTests {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

/*    @Test
    public void sendMessage(){
        //        public void convertAndSend(String exchange,     交换机
//                String routingKey,                      路由键
//                Object message,                         发送的消息
//                MessagePostProcessor messagePostProcessor) 消息序列化器
        for (int i = 0; i <10 ; i++) {
            if (i%2==0){
                OrderReturnReasonEntity orderReturnReasonEntity = new OrderReturnReasonEntity();
                orderReturnReasonEntity.setId(1L);
                orderReturnReasonEntity.setName("哈哈"+i);
                orderReturnReasonEntity.setCreateTime(new Date());
                rabbitTemplate.convertAndSend("hello-java-exchange","test.binding",orderReturnReasonEntity);
            }else {
                OrderEntity entity=new OrderEntity();
                entity.setOrderSn(UUID.randomUUID().toString());
                rabbitTemplate.convertAndSend("hello-java-exchange","test.binding",entity);
            }
        }

        log.info("消息发送完成{}");
    }*/

    @Test
    void contextLoads() throws IOException {
        BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
        String readLine = bf.readLine();
        String[] s = readLine.split(" ");
        int length = s[s.length - 1].length();

        System.out.println(length);
    }



    /**
     * 1.如何创建Exchange,Queue,Binding
     *    1),使用AmqpAdmin进行创建
     *  2.如何收发消息
     */
    //创建交换机
    @Test
    public void createExchange(){
        //amqpAdmin
        //Exchange,declareExchange--声明一个交换机
        // public DirectExchange(String name,     交换机名称
        // boolean durable,                        是否持久化
        // boolean autoDelete,                      是否自动删除
        // Map<String, Object> arguments)           参数
        DirectExchange directExchange = new DirectExchange("hello-java-exchange",true,false);
        amqpAdmin.declareExchange(directExchange);
        log.info("Exchange[{}]创建成功","hello-java-exchange");
    }

    //创建队列
    @Test
    void createQueue() {
//        public Queue(String name,   队列名称
//        boolean durable,            是否持久化
//        boolean exclusive,            是否是排他队列(只能被一个consumer的连接占用)
//        boolean autoDelete,          是否自动删除
//        @Nullable Map<String, Object> arguments)  参数
        Queue queue = new Queue("hello-java-queue",true,false,false);
        amqpAdmin.declareQueue(queue);
        log.info("queue[{}]创建成功","hello-java-queue");
    }

    //创建绑定
    @Test
    void createBinding() {
//	public Binding(String destination                      【目的地，队列name或 交换机name(如果作为路由的话)】
//                Binding.DestinationType destinationType,  【目的地类型 queue还是exchange(路由)】
//                String exchange,                          【交换机】
//                String routingKey,                        【路由键】
//                @Nullable Map<String, Object> arguments)  【自定义参数】
        //将exchange指定的交换机和destination目的地进行绑定，使用routingKey作为指定的路由键
        Binding binding = new Binding("hello-java-queue", Binding.DestinationType.QUEUE,"hello-java-exchange","test.binding",null);
        amqpAdmin.declareBinding(binding);
        log.info("binding[{}]绑定成功","test-binding");
    }

}
