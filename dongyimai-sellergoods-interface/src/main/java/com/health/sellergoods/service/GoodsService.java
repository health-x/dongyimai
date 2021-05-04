package com.health.sellergoods.service;

import com.health.entity.Goods;
import com.health.pojo.TbGoods;
import com.health.entity.PageResult;
import com.health.pojo.TbItem;

import java.util.List;
import java.util.Map;

/**
 * InnoDB free: 5120 kB服务层接口
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(Goods goods);
	
	
	/**
	 * 修改
	 */
	public void update(Goods goods);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public Goods findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbGoods goods, int pageNum,int pageSize);

	/**
	 * 根据模板id查询规格列表
	 */
	public List<Map> findSpecList(Long id);

	/**
	 * 商品审核
	 */
	public void updateStatus(Long[] ids,String status);

	/**
	 * 根据商品id和状态查询item表信息
	 * @param goodsId
	 * @param status
	 */
	public List<TbItem> findItems(Long[] goodsId,String status);

	//void findSpecList(Long[] ids, String status);
}
