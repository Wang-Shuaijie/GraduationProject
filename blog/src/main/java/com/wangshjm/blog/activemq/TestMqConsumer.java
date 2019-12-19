package com.wangshjm.blog.activemq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;

@Component
public class TestMqConsumer {
    @JmsListener(destination = "login_msg")
    public void receiveMessage(Message message){
        MapMessage mapMessage = (MapMessage) message;
    }
}
