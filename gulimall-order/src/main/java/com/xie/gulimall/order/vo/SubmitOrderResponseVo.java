package com.xie.gulimall.order.vo;

import com.xie.gulimall.order.entity.OrderEntity;
import lombok.Data;

/**
 * @title: SubmitOrderResponseVo
 * @Author Xie
 * @Date: 2023/1/31 21:42
 * @Version 1.0
 */
/**
 * 提交订单返回结果
 * @author zr
 * @date 2021/12/23 22:17
 */
@Data
public class SubmitOrderResponseVo {
    private OrderEntity order;

    /** 错误状态码 0成功**/
    private Integer code;
}
