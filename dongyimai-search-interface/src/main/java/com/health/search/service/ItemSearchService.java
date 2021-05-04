package com.health.search.service;

import com.health.pojo.TbItem;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {
    /**
     * 搜索
     */
    public Map<String,Object> search(Map searchMap);

    /**
     * 导入数据
     */
    public void importData(List<TbItem> list);

    /**
     * 删除索引库数据
     */
    public void deleteByGoodId(List goodsId);
}
