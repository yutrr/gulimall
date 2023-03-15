package com.xie.gulimall.cart.controller;

import com.xie.gulimall.cart.service.CartService;
import com.xie.gulimall.cart.vo.CartVo;
import com.xie.gulimall.cart.vo.CartItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @title: CartController
 * @Author Xie
 * @Date: 2022/12/12 21:44
 * @Version 1.0
 */
@Controller
public class CartController {

    @Autowired
    CartService cartService;


    /**
     * 订单服务调用：【购物车页面点击确认订单时】
     * 返回所有选中的商品项【从redis中取】
     * 并且要获取最新的商品价格信息，而不是redis中的数据
     *
     * 获取当前用户的购物车所有商品项
     */
    @GetMapping(value = "/currentUserCartItems")
    @ResponseBody
    public List<CartItemVo> getCurrentCartItems() {
        List<CartItemVo> cartItemVo = cartService.getUserCartItems();
        return cartItemVo;
    }

    /**
     * 去购物车页面的请求
     * 浏览器有一个cookie;user-key；标识用户身份，一个月后过期
     * 如果第一次使用jd的购物车功能，都会给一个临时的用户身份
     * 浏览器以后保存，每次访问都会带上这个cookie;
     *
     * 登录：session有
     * 没登录：按照cookie里面带来的user-key来做
     * 第一次：如果没有临时用户，要给她创建一个user-key
     * @return
     */
    @GetMapping("/cart.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {


        //1.快速得到用户信息，id,user-key
        //UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        CartVo cartVo = cartService.getCart();
        model.addAttribute("cart",cartVo);
        return "cartList";
    }

    /**
     * 添加商品到购物车
     * attributes.addFlashAttribute():将数据放在session中，可以在页面中取出，但是只能取一次
     * attributes.addAttribute():将数据放在url后面
     * @return
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId")Long skuId,
                            @RequestParam("num")Integer num,
                            RedirectAttributes attributes) throws ExecutionException, InterruptedException {
        cartService.addToCart(skuId,num);
       attributes.addAttribute("skuId",skuId);// 给重定向请求用的【参数会拼接在下面请求之后】【转发会在请求域中】
        return "redirect:http://cart.gulimall.com/addToCartSuccessPage.html";
    }

    /**
     * 跳转到添加购物车成功页面【防止重复提交】
     */
    @GetMapping("/addToCartSuccessPage.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId,
                                       Model model){
        //重定向到成功页面。再次查询购物车数据即可
        CartItemVo cartItemVo = cartService.getCartItem(skuId);
        model.addAttribute("cartItem",cartItemVo);
        return "success";
    }

    /**
     * 更改选中状态
     */
    @GetMapping(value = "/checkItem")
    public String checkItem(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "checked") Integer checked) {
        cartService.checkItem(skuId,checked);
        return "redirect:http://cart.gulimall.com/cart.html";
    }


    /**
     * 改变商品数量
     */
    @GetMapping(value = "/countItem")
    public String countItem(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "num") Integer num) {
        cartService.changeItemCount(skuId,num);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

    /**
     * 删除商品信息
     */
    @GetMapping(value = "/deleteItem")
    public String deleteItem(@RequestParam("skuId") Integer skuId) {
        cartService.deleteIdCartInfo(skuId);
        return "redirect:http://cart.gulimall.com/cart.html";
    }
}
