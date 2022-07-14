package com.xie.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.common.utils.PageUtils;
import com.xie.gulimall.order.entity.OrderReturnApplyEntity;

import java.util.Map;

/**
 * 订单退货申请
 *
 * @author xiehaijun
 * @email xiehaijun@gmail.com
 * @date 2022-07-07 18:06:25
 */
public interface OrderReturnApplyService extends IService<OrderReturnApplyEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

