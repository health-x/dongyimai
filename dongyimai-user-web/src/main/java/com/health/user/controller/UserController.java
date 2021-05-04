package com.health.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.pojo.TbUser;
import com.health.user.service.UserService;
import com.health.entity.PageResult;
import com.health.entity.Result;
import com.health.utils.PhoneFormatCheckUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户表controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Reference
	private UserService userService;

	/**
	 * 增加
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbUser user,String code){
		boolean b = userService.checkCode(user.getPhone(), code);
		if (b==false){
			return new Result(false,"验证码不正确！");
		}
		try {
			userService.add(user);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}

	/**
	 * 发送短信验证码
	 */
	@RequestMapping("/sendCode")
	public Result sendCode(String phone){
		//判断手机号格式是否正确
		if (!PhoneFormatCheckUtils.isPhoneLegal(phone)){
			return new Result(false,"手机号格式有误！");
		}
		try {
			userService.createCode(phone);
			return new Result(true,"发送成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"发送失败！");
		}
	}

	/**
	 * 获取登录用户名
	 */
	@RequestMapping("/findUserName")
	public Map findUsername(){
		Map map = new HashMap();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		map.put("loginName",username);
		return map;
	}
	
}
