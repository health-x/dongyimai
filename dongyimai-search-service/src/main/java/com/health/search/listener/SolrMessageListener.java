package com.health.search.listener;

import com.alibaba.fastjson.JSON;
import com.github.promeg.pinyinhelper.Pinyin;
import com.health.pojo.TbItem;
import com.health.search.service.ItemSearchService;
import com.health.search.service.impl.ItemSearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SolrMessageListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        System.out.println("接收到消息，导入数据");
        try {
            TextMessage textMessage = (TextMessage) message;
            List<TbItem> list = JSON.parseArray(textMessage.getText(), TbItem.class);

            for (TbItem item : list) {
                System.out.println(item.getTitle());
                Map map = JSON.parseObject(item.getSpec(), Map.class);
                item.setSpecMap(map);
            }
            itemSearchService.importData(list);
            System.out.println("导入成功！");
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
