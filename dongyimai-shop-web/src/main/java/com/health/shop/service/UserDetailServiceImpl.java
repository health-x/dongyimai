package com.health.shop.service;

import com.health.pojo.TbSeller;
import com.health.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailServiceImpl implements UserDetailsService {

    private SellerService sellerService;

    //set方式注入
    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //构建权限集合
        List<GrantedAuthority> roles = new ArrayList();
        roles.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        //获取商家
        TbSeller seller = sellerService.findOne(username);
        if (null!=seller){
            //判断当前商家是审核通过的商家
            if("1".equals(seller.getStatus())){
                return new User(username,seller.getPassword(),roles);
            }else {
                return null;
            }
        }
        return null;
    }
}
