package com.health.sellergoods.service;

import com.health.pojo.TbSeller;
import com.health.entity.PageResult;

import java.util.List;

/**
 * InnoDB free: 5120 kB服务层接口
 * @author Administrator
 *
 */
public interface SellerService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbSeller> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbSeller seller);
	
	
	/**
	 * 修改
	 */
	public void update(TbSeller seller);
	

	/**
	 * 根据ID获取实体
	 * @param sellerId
	 * @return
	 */
	public TbSeller findOne(String sellerId);
	
	
	/**
	 * 批量删除
	 * @param sellerIds
	 */
	public void delete(String [] sellerIds);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbSeller seller, int pageNum,int pageSize);

	/**
	 * 商家审核
	 */
	public void updateStatus(String sellerId,String status);

}
