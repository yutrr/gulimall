package com.example.seckill.controller;

import com.example.seckill.service.SeckillService;
import com.example.seckill.to.SeckillSkuRedisTo;
import com.xie.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @title: SekillController
 * @Author Xie
 * @Date: 2023/3/24 19:47
 * @Version 1.0
 */
@Controller
public class SeckillController {

    @Autowired
    SeckillService seckillService;

    /**
     * 返回当前时间可以参与的秒杀商品信息
     * @return
     */
    @ResponseBody
    @GetMapping("/currentSeckillSkus")
    public R getCurrentSeckillSkus(){

       List<SeckillSkuRedisTo> vos= seckillService.getCurrentSeckillSkus();
        return  R.ok().setData(vos);
    }

    /**
     * 根据skuId查询商品是否参加秒杀活动
     */
    @ResponseBody
    @GetMapping(value = "/sku/seckill/{skuId}")
    public R getSkuSeckilInfo(@PathVariable("skuId") Long skuId) {
        SeckillSkuRedisTo to = seckillService.getSkuSeckilInfo(skuId);
        return R.ok().setData(to);
    }

    /**
     * 商品进行秒杀(秒杀开始)
     * 查看表 oms_order_item
     */
    @GetMapping(value = "/kill")
    public String seckill(@RequestParam("killId") String killId,
                          @RequestParam("key") String key,
                          @RequestParam("num") Integer num,
                          Model model) {

        String orderSn = null;
        try {
            //1、判断是否登录
            orderSn = seckillService.kill(killId,key,num);
            model.addAttribute("orderSn",orderSn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
