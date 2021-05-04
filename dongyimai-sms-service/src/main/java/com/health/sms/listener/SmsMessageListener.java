package com.health.sms.listener;

import com.health.sms.service.SendMessage;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Component
public class SmsMessageListener implements MessageListener {

    @Autowired
    private SendMessage sendMessage;

    @Override
    public void onMessage(Message message) {
        if (message instanceof MapMessage){
            MapMessage mapMessage = (MapMessage) message;

            try{
                System.out.println("接收到请求"+mapMessage.getString("mobile"));
                HttpResponse response = sendMessage.sendMessage(mapMessage.getString("mobile"), mapMessage.getString("param"));
                System.out.println("请求结束："+response.getEntity());
            } catch (JMSException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
