package com.health.search.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.promeg.pinyinhelper.Pinyin;
import com.health.pojo.TbContent;
import com.health.pojo.TbItem;
import com.health.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(timeout = 3000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> search(Map searchMap) {
        Map<String,Object> map = new HashMap<String,Object>();
        //按照关键字高亮显示结果
        map.putAll(this.searchList(searchMap));
        //按照关键字查询分类
        List categoryList = this.searchCategoryList(searchMap);
        map.put("categoryList",categoryList);

        //查询品牌和规格数据
        String categoryName = (String) searchMap.get("category");
        if (!"".equals(categoryName)){
            map.putAll(this.searchBrandAndSpec(categoryName));
        } else {
            if (categoryList.size() >0 ){
                Map brandAndSpec = this.searchBrandAndSpec((String) categoryList.get(0));
                map.putAll(brandAndSpec);
            }
        }

        return map;
    }

    @Override
    public void importData(List<TbItem> list) {
        for (TbItem item : list) {
            Map<String,String> map = JSON.parseObject(item.getSpec(), Map.class);
            Map pinYin = new HashMap();
            for (String key : map.keySet()) {
                pinYin.put("item_spec"+Pinyin.toPinyin(key,"").toLowerCase(),map.get(key));
            }
            item.setSpecMap(pinYin);
        }
        solrTemplate.saveBean(list);
        solrTemplate.commit();
    }

    @Override
    public void deleteByGoodId(List goodsId) {
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_goodsid").in(goodsId);
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }


    private Map searchList(Map searchMap){
        Map<String,Object> map= new HashMap<String ,Object>();
        //1.创建高亮条件对象
        SimpleHighlightQuery query =new SimpleHighlightQuery();
        //2.创建高亮选项对象
        HighlightOptions options = new HighlightOptions();
        //3.设置需要高亮的前缀
        options.addField("item_title");
        options.setSimplePrefix("<em style='color:red'>");
        options.setSimplePostfix("</em>");
        query.setHighlightOptions(options);

        //按照关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //按照分类进行查询
        if (!"".equals(searchMap.get("category"))){
            Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        if (!"".equals(searchMap.get("brand"))){
            Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        //按照规格过滤
        if (searchMap.get("spec")!=null){
            Map<String,String > specMap = (Map) searchMap.get("spec");
            for (String key : specMap.keySet()) {
                Criteria filterCriteria = new Criteria("item_spec_" + Pinyin.toPinyin(key, "").toLowerCase()).is(specMap.get(key));
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
        //按照价格区间进行过滤
        if (!"".equals(searchMap.get("price"))){
            String[] prices = ((String)searchMap.get("price")).split("-");
            //如果区间起点不等0
            if (!prices[0].equals("0")){
                Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(prices[0]);
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
            if (!prices[1].equals("*")){
                Criteria filterCriteria = new Criteria("item_price").lessThanEqual(prices[1]);
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
        //分页
        Integer pageNo = (Integer) searchMap.get("pageNo");
        if (null==pageNo){
            pageNo = 1;
        }
        Integer pageSize = (Integer) searchMap.get("pageSize");
        if (null==pageSize){
            pageSize = 10;
        }
        query.setOffset((pageNo-1)*pageSize);  //从第几条记录开始
        query.setRows(pageSize);    //显示多少条

        //按照价格排序
        String sortValue = (String) searchMap.get("sort");
        String sortField = (String) searchMap.get("sortField");
        if(sortValue!=null&&!sortValue.equals("")){
            if (sortValue.equals("ASC")){
                Sort sort = new Sort(Sort.Direction.ASC, "item_" + sortField);
                query.addSort(sort);
            }
            if (sortValue.equals("DESC")){
                Sort sort = new Sort(Sort.Direction.DESC, "item_" + sortField);
                query.addSort(sort);
            }
        }

        HighlightPage<TbItem> page=solrTemplate.queryForHighlightPage(query,TbItem.class);
        List<HighlightEntry<TbItem>> highlighted = page.getHighlighted();

        for (HighlightEntry<TbItem> highlightEntry : highlighted){
            TbItem item = highlightEntry.getEntity();
            if (highlightEntry.getHighlights().size()>0 && highlightEntry.getHighlights().get(0).getSnipplets().size()>0){
                item.setTitle(highlightEntry.getHighlights().get(0).getSnipplets().get(0));
            }
        }
        map.put("rows",page.getContent());
        //总页数
        map.put("totalPages", page.getTotalPages());
        //总记录数
        map.put("total",page.getTotalElements());
        return map;
    }
    private List searchCategoryList(Map searchMap){
        List list = new ArrayList();
        //按照关键字搜索
        Query query = new SimpleQuery();

        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        //查询结果按照分类进行
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query,TbItem.class);
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
        Page<GroupEntry<TbItem>> entryPage = groupResult.getGroupEntries();
        List<GroupEntry<TbItem>> content = entryPage.getContent();
        for (GroupEntry<TbItem> entry : content) {
            list.add(entry.getGroupValue());
            System.out.println(entry.getGroupValue());
        }
        return list;
    }
    private Map searchBrandAndSpec(String category){

        Map map = new HashMap();
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        if (null !=typeId){
            //根据模板id查询品牌数据
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
            map.put("brandList",brandList);
            //根据模板Id查询规格数据
            List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
            map.put("specList",specList);
        }
        return map;
    }
}
