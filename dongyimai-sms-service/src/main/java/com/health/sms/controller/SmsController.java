package com.health.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.*;

@RestController
public class SmsController {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination queueSmsDestination;

    @RequestMapping("/sendMessage")
    public String sendMsg(String mobile,String param){
        jmsTemplate.send(queueSmsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("mobile", mobile);
                mapMessage.setString("param", param);
                return mapMessage;
            }
        });
        return "send OK!!!";
    }
}
