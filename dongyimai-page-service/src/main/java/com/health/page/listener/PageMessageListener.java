package com.health.page.listener;

import com.health.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class PageMessageListener implements MessageListener {

    @Autowired
    private PageService pageService;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage= (TextMessage)message;
        try {
            String text = textMessage.getText();
            System.out.println("接收到消息："+text);
            boolean b = pageService.getHtml(Long.parseLong(text));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
