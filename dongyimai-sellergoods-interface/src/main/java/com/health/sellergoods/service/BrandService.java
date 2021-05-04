package com.health.sellergoods.service;

import com.health.entity.PageResult;
import com.health.pojo.TbBrand;

import java.util.List;
import java.util.Map;

public interface BrandService {
    public List<TbBrand> findAll();

    //分页
    public PageResult findPage(int pageNum, int pageSize);

    /**
     * 品牌新增
     */
    public void add(TbBrand brand);

    //根据查询
    public TbBrand findOne(Long id);

    //修改数据
    public void update(TbBrand brand);

    //删除数据
    public void delete(Long[] ids);

    //条件查询
    public PageResult findPage(TbBrand brand,int pageNum,int pageSize);

    public List<Map> findBrandList();
}

