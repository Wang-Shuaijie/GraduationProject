package com.wangshjm.blog.activemq;

import com.wangshjm.blog.utils.SmsUtil;
import org.springframework.jms.annotation.JmsListener;

import javax.jms.MapMessage;
import javax.jms.Message;

/**
 * 监听到activemq的数据
 */
public class MqConsumer {
    @JmsListener(destination = "login_msg")
    public void receiveMessage(Message message){
        MapMessage mapMessage = (MapMessage) message;
        // 调用SMS服务发送短信   SmsSystem阿里大于发送短信给客户手机实现类
        try {
            // 大于发送短信, Map来自ActiveMQ 生成者
            SmsUtil.sendMessages( mapMessage.getString("code"), mapMessage.getString("telephone") );
            System.out.println( "-----发送消息成功..."+mapMessage.getString("code"));
        } catch (Exception e) {//JMS
            e.printStackTrace();
        }
    }
}
