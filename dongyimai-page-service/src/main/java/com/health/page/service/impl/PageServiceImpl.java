package com.health.page.service.impl;

import com.health.mapper.TbGoodsDescMapper;
import com.health.mapper.TbGoodsMapper;
import com.health.mapper.TbItemCatMapper;
import com.health.mapper.TbItemMapper;
import com.health.page.service.PageService;
import com.health.pojo.TbGoods;
import com.health.pojo.TbGoodsDesc;
import com.health.pojo.TbItem;
import com.health.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成静态页面实现类
 */
@Service
public class PageServiceImpl implements PageService {

    @Value("${pageDir}")
    private String pageDir;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemCatMapper  itemCatMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public boolean getHtml(Long goodsId) {
        try {
            Configuration configuration = freeMarkerConfig.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            //准备数据
            Map data = new HashMap();
            //商品表信息
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(goodsId);
            data.put("tbGoods",tbGoods);
            // 商品扩展信息
            TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
            data.put("tbGoodsDesc",tbGoodsDesc);

            //商品分类
            String itemCat1 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id()).getName();
            String itemCat2 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id()).getName();
            String itemCat3 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName();

            data.put("itemCat1",itemCat1);
            data.put("itemCat2",itemCat2);
            data.put("itemCat3",itemCat3);

            //sku列表
            TbItemExample example=new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andStatusEqualTo("1");//状态为有效
            criteria.andGoodsIdEqualTo(goodsId);//指定SPU ID
            example.setOrderByClause("is_default desc");//按照状态降序，保证第一个为默认
            List<TbItem> itemList = itemMapper.selectByExample(example);
            data.put("itemList", itemList);

            //文件写入对象
            FileWriter fileWriter = new FileWriter(pageDir+goodsId+".html");
            template.process(data,fileWriter);
            fileWriter.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteHtml(Long[] goodsIds) {
        try {
            for (Long goodsId : goodsIds) {
                new File(pageDir+goodsId+".html").delete();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
