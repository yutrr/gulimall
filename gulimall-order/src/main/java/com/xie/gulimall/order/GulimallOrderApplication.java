package com.xie.gulimall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 使用RabbitMQ
 * 1、引入amqp场景;RabbitAutoConfiguration就会自动生效
 * 2、给容器中自动配置了
 * RabbitTemplate、AmqpAdmin、CachingConnectionFactory、RabbitMessagingTemplate
 * 3、@EnableRabbit:(在创建交换机,队列时可以不需要,发送消息可以不需要这个注解，监听消息必须使用这个注解)
 * 主启动类上需要加上`@EnableRabbit`
 * 在需要监听消息的方法上加上`@RabbitListener`,并指明监听队列名称
 *
 * @RabbitListener:类+方法上(监听哪些队列即可)
 * @RabbitHandler:标在方法上 (重载区分不同的消息)
 * <p>
 * 本地事务失效问题
 * 同一个对象内事务方法互调默认失效，原因绕过了代理对象，事务使用代理对象来去控制的
 * 解决：使用代理对象来调用事务方法
 * 1），引用aop-starter;spring-boot-starter-aop;引入了aspectj
 * 2)EnableAspectJAutoProxy;开启 aspectj 动态代理功能。以后所有的动态代理都是aspectj创建的（即使没有接口也可以创建动态代理）
 * 对外暴露代理对象
 * 3）本类互调用对象
 * OrderServiceImpl orderService=(OrderServiceImpl)AopContext.currentProxy();
 * orderService.b();
 * orderService.c();
 * <p>
 * Seata控制分布式事务
 * 1）每一个微服务先必须创建undo_log
 * 2)安装事务协调器；seata-server
 * 3)整合
 * 1.导入依赖 spring-cloud-starter-alibaba-seata  seata-all-0.7.1
 * 2.解压启动seata-server；
 * registry.conf:注册中心配置；修改registry type=nacos
 * file.conf:
 * 3.所有想要用到分布式事物的微服务使用seata DataSourceProxy代理自己的数据源
 * 4.每个微服务，都必须导入file.conf，registry.conf  vgroup_mapping.{application-name}-fescar-service-group = "default"
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableRedisHttpSession
@EnableDiscoveryClient
@EnableFeignClients
@EnableRabbit
@SpringBootApplication
public class GulimallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallOrderApplication.class, args);
    }

}
