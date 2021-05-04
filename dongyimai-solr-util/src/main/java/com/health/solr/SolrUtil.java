package com.health.solr;

import com.alibaba.fastjson.JSON;
import com.github.promeg.pinyinhelper.Pinyin;
import com.health.mapper.TbItemMapper;
import com.health.pojo.TbItem;
import com.health.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SolrUtil {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private TbItemMapper itemMapper;

    public void importData(){
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");
        List<TbItem> itemList = itemMapper.selectByExample(example);
        System.out.println("------------开始------------");
        //遍历全部的商品数据
        for (TbItem item : itemList) {
            System.out.println(item.getTitle());
            //创建新的map用于存储转换后的拼音字段
            Map<String, String> pinYin = new HashMap<String, String>();
            //读取规格数据 {网络：3g，内存：16g}
            Map<String,String> map = JSON.parseObject(item.getSpec(), Map.class);
            for (String key : map.keySet()) {
                //{wangluo：3g，neicun：16g}
                pinYin.put(Pinyin.toPinyin(key,"").toLowerCase(),map.get(key));
            }
            item.setSpecMap(pinYin);
        }
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
        System.out.println("------------结束------------");
    }

    public void deleteAll(){
        Query query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    public static void main(String[] args) {
        //从spring容器中获取上下文中存储的对象
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
        //solrUtil.deleteAll();
        solrUtil.importData();
    }
}
