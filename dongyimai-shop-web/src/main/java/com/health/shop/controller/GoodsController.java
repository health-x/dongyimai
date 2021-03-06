package com.health.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.entity.Goods;
import com.health.pojo.TbGoods;
import com.health.sellergoods.service.GoodsService;
import com.health.entity.PageResult;
import com.health.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * InnoDB free: 5120 kBcontroller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}

	/**
	 * 增加
	 *
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods) {
		//设置商品还未提交审核
		goods.getGoods().setAuditStatus("0");
		//当前商品是哪个商家的
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		goods.getGoods().setSellerId(name);
		try {
			goodsService.add(goods);
			return new Result(true, "增加成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败!");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		//增加验证--》当前操作是否为当前登录的商家
		Goods one = goodsService.findOne(goods.getGoods().getId());
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		if (!one.getGoods().getSellerId().equals(sellerId)||!goods.getGoods().getSellerId().equals(sellerId)){
			return new Result(false,"你干嘛呢");
		}
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			goodsService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		//获取商家id
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		goods.setSellerId(sellerId);
		return goodsService.findPage(goods, page, rows);
	}

	@RequestMapping("/findSpecList")
	public List<Map> findSpecList(Long id){
		return goodsService.findSpecList(id);
	}
}
