package com.xie.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.common.utils.PageUtils;
import com.xie.gulimall.order.entity.OrderReturnReasonEntity;

import java.util.Map;

/**
 * 退货原因
 *
 * @author xiehaijun
 * @email xiehaijun@gmail.com
 * @date 2022-07-07 18:06:25
 */
public interface OrderReturnReasonService extends IService<OrderReturnReasonEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

