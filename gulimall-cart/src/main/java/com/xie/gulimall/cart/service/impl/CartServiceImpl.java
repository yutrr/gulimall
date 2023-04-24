package com.xie.gulimall.cart.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xie.common.constant.CartConstant;
import com.xie.common.utils.R;
import com.xie.gulimall.cart.feign.ProductFeignService;
import com.xie.gulimall.cart.interceptor.CartInterceptor;
import com.xie.gulimall.cart.service.CartService;
import com.xie.gulimall.cart.vo.CartVo;
import com.xie.gulimall.cart.vo.CartItemVo;
import com.xie.gulimall.cart.vo.SkuInfoVo;
import com.xie.gulimall.cart.vo.UserInfoTo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    public CartItemVo addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();


        String res = (String) cartOps.get(skuId.toString());
        if (StringUtils.isEmpty(res)){
            //购物车无此商品
            //2.添加新商品到购物车
            CartItemVo cartItemVo = new CartItemVo();
            CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
                //1.远程查询当前要添加的商品信息
                R skuInfo = productFeignService.getSkuInfo(skuId);
                SkuInfoVo data = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });
                cartItemVo.setCheck(true);
                cartItemVo.setCount(num);
                cartItemVo.setImage(data.getSkuDefaultImg());
                cartItemVo.setSkuId(skuId);
                cartItemVo.setPrice(data.getPrice());
                cartItemVo.setTitle(data.getSkuTitle());
            },executor);

            //3.远程查询sku的组合信息
            CompletableFuture<Void> getSkuSaleAttrValues = CompletableFuture.runAsync(() -> {
                List<String> values = productFeignService.getSkuSaleAttrValues(skuId);
                cartItemVo.setSkuAttr(values);
            }, executor);

            CompletableFuture.allOf(getSkuInfoTask,getSkuSaleAttrValues).get();
            String s = JSON.toJSONString(cartItemVo);
            cartOps.put(skuId.toString(),s);
            return cartItemVo;
        } else {
            //购物车有此商品，修改数量
          CartItemVo cartItemVo = JSON.parseObject(res, CartItemVo.class);
            cartItemVo.setCount(cartItemVo.getCount()+num);
            cartOps.put(skuId.toString(),JSON.toJSONString(cartItemVo));
            return cartItemVo;
        }


    }

    @Override
    public CartItemVo getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String str = (String) cartOps.get(skuId.toString());
        CartItemVo cartItemVo = JSON.parseObject(str, CartItemVo.class);
        return cartItemVo;
    }

    /**
     * 跳转cartList页面
     * 封装购物车类【所有商品，所有商品的价格】
     * 【整合登录状态与未登录状态】
     */
    @Override
    public CartVo getCart() throws ExecutionException, InterruptedException {
        CartVo cartVo = new CartVo();
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        System.out.println(userInfoTo);
        if (userInfoTo.getUserId() != null) {
            //  1）、登录后购物车的key
            String cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserId();
            //  2）、临时购物车的key
            String temptCartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
            //2、如果临时购物车的数据还未进行合并
            List<CartItemVo> tempCartItemVos = getCartItems(temptCartKey);
            if (tempCartItemVos != null) {
                //临时购物车有数据需要进行合并操作
                for (CartItemVo item : tempCartItemVos) {
                    addToCart(item.getSkuId(),item.getCount());
                }
                //清除临时购物车的数据
                clearCartInfo(temptCartKey);
            }
            //3、获取登录后的购物车数据【包含合并过来的临时购物车的数据和登录后购物车的数据】
            List<CartItemVo> cartItemVos = getCartItems(cartKey);
            cartVo.setItems(cartItemVos);
        } else {
            //没登录
            String cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
            //获取临时购物车里面的所有购物项
            List<CartItemVo> cartItemVos = getCartItems(cartKey);
            cartVo.setItems(cartItemVos);
        }
        return cartVo;
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
    public List<CartItemVo> getCartItems(String cartKey){
        BoundHashOperations<String, Object, Object> operations =
                redisTemplate.boundHashOps(cartKey);
        List<Object> values = operations.values();
        if (values!=null&&values.size()>0){
            List<CartItemVo> cartItemsStreamVo = values.stream().map((obj) -> {
                String str = (String) obj;
                CartItemVo cartItemVo = JSON.parseObject(str, CartItemVo.class);
                return cartItemVo;
            }).collect(Collectors.toList());
            return cartItemsStreamVo;
        }
        return null;
    }

    public void clearCartInfo(String cartKey){
        redisTemplate.delete(cartKey);
    }

    @Override
    public void checkItem(Long skuId, Integer check) {
        //查询购物车里面的商品
        CartItemVo cartItemVo = getCartItem(skuId);
        //修改商品状态
        cartItemVo.setCheck(check == 1?true:false);

        //序列化存入redis中
        String redisValue = JSON.toJSONString(cartItemVo);

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
        CartItemVo cartItemVo = getCartItem(skuId);
        cartItemVo.setCount(num);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        //序列化存入redis中
        String redisValue = JSON.toJSONString(cartItemVo);
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
    public List<CartItemVo> getUserCartItems() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo==null){
            return null;
        }else {
            //获取购物车项
            String cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserId();
            //获取所有的
            List<CartItemVo> cartItems = getCartItems(cartKey);
            if (cartKey==null){
                return null;
            }
            //筛选出被选中的购物项
            List<CartItemVo> collect = cartItems.stream()
                    .filter(items -> items.getCheck())
                    .map(item->{
                        //更新为最新的价格（查询数据库）
                        // redis中的价格不是最新的
                       R price= productFeignService.getPrice(item.getSkuId());
                        item.setPrice(new BigDecimal((String)price.get("data")));
                        return item;
                    })
                    .collect(Collectors.toList());
            return cartItems;
        }
    }
}
