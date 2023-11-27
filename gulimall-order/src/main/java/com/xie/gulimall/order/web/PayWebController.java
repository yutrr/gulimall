package com.xie.gulimall.order.web;

import com.alipay.api.AlipayApiException;
import com.xie.gulimall.order.config.AlipayTemplate;
import com.xie.gulimall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @title: PayWebController
 * @Author Xie
 * @Date: 2023/3/10 22:03
 * @Version 1.0
 */
@Controller
public class PayWebController {

    @Autowired
    AlipayTemplate alipayTemplate;

    @Autowired
    OrderService orderService;

    /**
     * 用户下单:支付宝支付
     * 1、让支付页让浏览器展示
     * 2、支付成功以后，跳转到用户的订单列表页
     *
     * @param orderSn
     * @return
     * @throws AlipayApiException
     */
    @ResponseBody
    @GetMapping(value = "/aliPayOrder", produces = "text/html")
    public String payOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {
        AlipayTemplate.PayVo payVo = orderService.getOrderPay(orderSn);
        // 支付宝返回一个页面【支付宝账户登录的html页面】
        String pay = alipayTemplate.pay(payVo);
        System.out.println(pay);
        return pay;
    }
}
