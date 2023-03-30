package com.example.seckill.secheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @title: HelloSchedule
 * @Author Xie
 * @Date: 2023/3/20 19:30
 * @Version 1.0
 */

/**
 * @EnableScheduling  开启定时任务功能
 */
@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class HelloSchedule {

    /**
     * 1、在Spring中表达式是6位组成，不允许第七位的年份
     * 2、在周几的的位置,1-7代表周一到周日
     * 3、定时任务不该阻塞。默认是阻塞的
     * 3.2.2 整合异步任务
     * 3.2.2.0 自动配置类TaskExecutionProperties
     * 定时任务异步执行@EnableAsync  @Async
     */
    /*@Async
    @Scheduled(cron ="* * * * * ?" )
    public void hello(){
        log.info("hrllo");
    }*/
}
