package com.xie.gulimall.cart.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xie.common.constant.CartConstant;
import com.xie.common.utils.R;
import com.xie.gulimall.cart.feign.ProductFeignService;
import com.xie.gulimall.cart.interceptor.CartInterceptor;
import com.xie.gulimall.cart.service.CartService;
import com.xie.gulimall.cart.vo.Cart;
import com.xie.gulimall.cart.vo.CartItem;
import com.xie.gulimall.cart.vo.SkuInfoVo;
import com.xie.gulimall.cart.vo.UserInfoTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @title: CartServiceImpl
 * @Author Xie
 * @Date: 2022/12/12 21:39
 * @Version 1.0
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    ThreadPoolExecutor executor;


    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();


        String res = (String) cartOps.get(skuId.toString());
        if (BeanUtil.isEmpty(res)){
            //购物车无此商品
            //2.添加新商品到购物车
            CartItem cartItem = new CartItem();
            CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
                //1.远程查询当前要添加的商品信息
                R skuInfo = productFeignService.getSkuInfo(skuId);
                SkuInfoVo data = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });
                cartItem.setCheck(true);
                cartItem.setCount(num);
                cartItem.setImage(data.getSkuDefaultImg());
                cartItem.setSkuId(skuId);
                cartItem.setPrice(data.getPrice());
                cartItem.setTitle(data.getSkuTitle());
            },executor);

            //3.远程查询sku的组合信息
            CompletableFuture<Void> getSkuSaleAttrValues = CompletableFuture.runAsync(() -> {
                List<String> values = productFeignService.getSkuSaleAttrValues(skuId);
                cartItem.setSkuAttr(values);
            }, executor);

            CompletableFuture.allOf(getSkuInfoTask,getSkuSaleAttrValues).get();
            String s = JSON.toJSONString(cartItem);
            cartOps.put(skuId.toString(),s);
            return cartItem;
        } else {
            //购物车有此商品，修改数量
          CartItem  cartItem = JSON.parseObject(res, CartItem.class);
            cartItem.setCount(cartItem.getCount()+num);
            cartOps.put(skuId.toString(),JSON.toJSONString(cartItem));
            return cartItem;
        }


    }

    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String str = (String) cartOps.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(str, CartItem.class);
        return cartItem;
    }

    /**
     * 跳转cartList页面
     * 封装购物车类【所有商品，所有商品的价格】
     * 【整合登录状态与未登录状态】
     */
    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        Cart cart = new Cart();
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        System.out.println(userInfoTo);
        if (userInfoTo.getUserId() != null) {
            //  1）、登录后购物车的key
            String cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserId();
            //  2）、临时购物车的key
            String temptCartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
            //2、如果临时购物车的数据还未进行合并
            List<CartItem> tempCartItems = getCartItems(temptCartKey);
            if (tempCartItems != null) {
                //临时购物车有数据需要进行合并操作
                for (CartItem item : tempCartItems) {
                    addToCart(item.getSkuId(),item.getCount());
                }
                //清除临时购物车的数据
                clearCartInfo(temptCartKey);
            }
            //3、获取登录后的购物车数据【包含合并过来的临时购物车的数据和登录后购物车的数据】
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);
        } else {
            //没登录
            String cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
            //获取临时购物车里面的所有购物项
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);
        }
        return cart;
    }

    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        String cartKey="";
        if (userInfoTo.getUserId()!=null){
            //gulimall:cart:1
            cartKey= CartConstant.CART_PREFIX+userInfoTo.getUserId();
        }else {
            cartKey=CartConstant.CART_PREFIX+userInfoTo.getUserKey();//临时用户
        }

        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        return operations;
    }

    /**
     * 获取购物车里面的数据【根据key，包装成List<CartItemVo>】
     * key=【gulimall:cart:2 或 gulimall:cart:lkajkashjghj2989dsj】
     * @param cartKey
     * @return
     */
    public List<CartItem> getCartItems(String cartKey){
        BoundHashOperations<String, Object, Object> operations =
                redisTemplate.boundHashOps(cartKey);
        List<Object> values = operations.values();
        if (values!=null&&values.size()>0){
            List<CartItem> cartItemsStream = values.stream().map((obj) -> {
                String str = (String) obj;
                CartItem cartItem = JSON.parseObject(str, CartItem.class);
                return cartItem;
            }).collect(Collectors.toList());
            return cartItemsStream;
        }
        return null;
    }

    public void clearCartInfo(String cartKey){
        redisTemplate.delete(cartKey);
    }

    @Override
    public void checkItem(Long skuId, Integer check) {
        //查询购物车里面的商品
        CartItem cartItem = getCartItem(skuId);
        //修改商品状态
        cartItem.setCheck(check == 1?true:false);

        //序列化存入redis中
        String redisValue = JSON.toJSONString(cartItem);

        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.put(skuId.toString(),redisValue);
    }

    /**
     * 修改购物车项数量
     * @param skuId
     * @param num
     */
    @Override
    public void changeItemCount(Long skuId, Integer num) {
        //查询购物车里面的商品
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        //序列化存入redis中
        String redisValue = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(),redisValue);
    }

    /**
     * 删除购物项
     * @param skuId
     */
    @Override
    public void deleteIdCartInfo(Integer skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.delete(skuId.toString());
    }

    @Override
    public List<CartItem> getUserCartItems() {
        return null;
    }
}
