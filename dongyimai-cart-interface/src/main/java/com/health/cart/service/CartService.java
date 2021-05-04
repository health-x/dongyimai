package com.health.cart.service;

import com.health.entity.Cart;

import java.util.List;

public interface CartService {

    /**
     * 添加商品到购物车
     * @param cartList 要添加的商品信息
     * @param itemId   购买的商品
     * @param num   购买的数量
     * @return  原有的购物车数据
     */
    public List<Cart> addGoodsToCart(List<Cart> cartList,Long itemId,Integer num);


    /**
     * 从redis中获取购物车
     * @param username
     * @return
     */
    public List<Cart> findCartListFromRedis(String username);

    /**
     * 将购物车保存到redis
     * @param username
     * @param cartList
     */
    public void saveToRedis(String username,List<Cart> cartList);

    /**
     * 合并购物车数据（cookie和redis）
     */
    public List<Cart> mergeCartList(List<Cart> cartList1,List<Cart> cartList2);

}
