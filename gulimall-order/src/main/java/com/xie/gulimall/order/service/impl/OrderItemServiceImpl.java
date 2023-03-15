package com.xie.gulimall.order.service.impl;

import com.rabbitmq.client.Channel;
import com.xie.gulimall.order.entity.OrderEntity;
import com.xie.gulimall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.common.utils.PageUtils;
import com.xie.common.utils.Query;

import com.xie.gulimall.order.dao.OrderItemDao;
import com.xie.gulimall.order.entity.OrderItemEntity;
import com.xie.gulimall.order.service.OrderItemService;

@Slf4j
@RabbitListener(queues = {"hello-java-queue"})
@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 参数可以写一下类型
     * 1.Message message:原生消息详细信息。头+体
     * 2.T<发送的消息的类型>  OrderReturnReasonEntity content
     *
     * 3.Channel channel:当前传输数据的通道
     *
     * Queue:可以很多人都来监听。只要收到消息。队列删除消息，而且只能有一个人收到消息
     * 场景：
     *   1），订单服务启动多个;同一个消息，只能有一个客户端收到
     *   2），只有一个消息完全处理完，方法运行结束，我们就可以接收到下一个消息
     * @param message
     */
    //@RabbitListener(queues = {"hello-java-queue"})
    @RabbitHandler
    void receiveMessage(Message message,
                        OrderReturnReasonEntity content,
                        Channel channel){
        byte[] body = message.getBody();
        //消息头属性信息
        //System.out.println("信道"+channel);
        MessageProperties properties = message.getMessageProperties();
        System.out.println("收到消息:"+message+"==>内容:"+content);
        long deliveryTag = properties.getDeliveryTag();
            try {
                if (deliveryTag%2==0) {
                    //签收货物，非批量模式
                    //public void basicNack(long deliveryTag,  //channel内按顺序自增
                    //boolean multiple)                         //是否批量确认
                    channel.basicAck(deliveryTag, false);
                    System.out.println("签收了货物 = " + deliveryTag);
                }else {
                    //public void basicNack(long deliveryTag, //channel内按顺序自增
                    //boolean multiple,                       //是否批量退货
                    //boolean requeue)                        //确认后是否重新入队 false丢弃
                    channel.basicNack(deliveryTag,false,false);//手动ack确认拒绝消息
                    //channel.basicReject();
                    System.out.println("拒绝签收了货物 = " + deliveryTag);
                }
            } catch (IOException e){
                    //网络中断
                    e.printStackTrace();
            }
        log.info("信道："+channel);
        byte[] body1 = message.getBody();
    }

    //@RabbitHandler
    void receiveMessage2(OrderEntity content){
        System.out.println("收到消息:"+content);
    }

}