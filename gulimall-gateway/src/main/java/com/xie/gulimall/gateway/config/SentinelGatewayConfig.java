package com.xie.gulimall.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.fastjson.JSON;
import com.xie.common.exception.BizCodeEnume;
import com.xie.common.utils.R;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @title: SentinelGatewayConfig
 * @Author Xie
 * @Date: 2023/4/17 21:19
 * @Version 1.0
 */
@Configuration
public class SentinelGatewayConfig {
    // TODO 响应式编程
    public SentinelGatewayConfig() {
        // 网关限流了请求，就会调用此回调
        GatewayCallbackManager.setBlockHandler((serverWebExchange, throwable) -> {
            R error = R.error(BizCodeEnume.TOO_MANY_REQUEST.getCode(), BizCodeEnume.TOO_MANY_REQUEST.getMsg());
            String errJson = JSON.toJSONString(error);
            return ServerResponse.ok().body(Mono.just(errJson), String.class);
        });
    }
}
