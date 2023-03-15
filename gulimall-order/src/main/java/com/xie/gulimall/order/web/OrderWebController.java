package com.xie.gulimall.order.web;

import com.xie.common.exception.NoStockException;
import com.xie.gulimall.order.service.OrderService;
import com.xie.gulimall.order.vo.OrderConfirmVo;
import com.xie.gulimall.order.vo.OrderSubmitVo;
import com.xie.gulimall.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

/**
 * @title: OrderWebController
 * @Author Xie
 * @Date: 2023/1/5 21:33
 * @Version 1.0
 */
@Controller
public class OrderWebController {

    @Autowired
    OrderService orderService;

    @GetMapping("/toTrade")
    public String toTrade(Model model, HttpServletRequest request) throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo=orderService.confirmOrder();
        model.addAttribute("orderConfirmData",confirmVo);
        //展示订单确认的数据
        return "confirm";
    }


    @RequestMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo submitVo, Model model, RedirectAttributes attributes){
      try {
          SubmitOrderResponseVo responseVo= orderService.submitOrder(submitVo);
          Integer code = responseVo.getCode();
          if (code==0){
              model.addAttribute("order",responseVo.getOrder());
              return "pay";
          }else {
              String msg="下单失败;";
              switch (code){
                  case 1:
                      msg+="防重令牌校验失败";
                      break;
                  case 2:
                      msg+="商品价格发生变化";
                      break;
              }
              attributes.addFlashAttribute("msg",msg);
              return "redirect:http://order.gulimall.com/toTrade";
          }
      }catch (Exception e){
          if (e instanceof NoStockException){
              String msg="下单失败，商品无库存";
              attributes.addFlashAttribute("msg",msg);
          }
          return "redirect:http://order.gulimall.com/toTrade";
      }
      }
}
