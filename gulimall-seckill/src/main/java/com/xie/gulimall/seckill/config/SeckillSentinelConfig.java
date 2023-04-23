package com.xie.gulimall.seckill.config;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.xie.common.exception.BizCodeEnume;
import com.xie.common.utils.R;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @title: SeckillSentinelConfig
 * @Author Xie
 * @Date: 2023/4/11 20:53
 * @Version 1.0
 */

/**
 * 如果非要用WebCallbackManager，加一套依赖
 * <dependency>
 *     <groupId>com.alibaba.csp</groupId>
 *     <artifactId>sentinel-web-servlet</artifactId>
 *  </dependency>
 */
//2.2.0之前版本写法
@Configuration
public class SeckillSentinelConfig {

    public SeckillSentinelConfig(){
        WebCallbackManager.setUrlBlockHandler((httpServletRequest, httpServletResponse, e) -> {
            R error = R.error(BizCodeEnume.TOO_MANY_REQUEST.getCode(), BizCodeEnume.TOO_MANY_REQUEST.getMsg());
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write(JSON.toJSONString(error));
        });
    }
}

/*@Component
public class SentinelUrlBlockHandler implements BlockExceptionHandler {

    *//**
     * 自定义限流返回信息
     *//*
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException ex) throws IOException {
        // 降级业务处理
        R error = R.error(BizCodeEnume.TOO_MANY_REQUEST.getCode(), BizCodeEnume.TOO_MANY_REQUEST.getMsg());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write(JSON.toJSONString(error));
    }
}*/

