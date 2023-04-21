package com.xie.gulimall.product.fallback;

import com.xie.common.exception.BizCodeEnume;
import com.xie.common.utils.R;
import com.xie.gulimall.product.feign.SeckillFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @title: 熔断方法的具体实现，也可以是降级方法的具体实现
 * @Author Xie
 * @Date: 2023/4/13 21:24
 * @Version 1.0
 */
@Component
@Slf4j
public class SeckillFeignServiceFallBack implements SeckillFeignService {
    @Override
    public R getSkuSeckilInfo(Long skuId) {
        log.info("熔断方法调用...getSkuSeckilInfo");
        return R.error(BizCodeEnume.TOO_MANY_REQUEST.getCode(),BizCodeEnume.TOO_MANY_REQUEST.getMsg());
    }
}
