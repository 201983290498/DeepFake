package com.coder.desgin.mq.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author coder
 * @Date 2023/3/2 17:22
 * @Description 专门用来发送email的消息生产者
 */
@Component
public class JavaEmailProducer {

    private final AmqpTemplate amqpTemplate;

    @Value("${deepfake.ex}")
    private String exchangeName;

    @Value("${rabbitmq.params.split}")
    private String paramSplit;


    public JavaEmailProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    /**
     * 消息队列的生产者
     * @param msg 待发送的信息
     */
    public void sendEmailMsg (String type, String email, String msg) {
        msg = type + paramSplit + email + paramSplit + msg;
        amqpTemplate.convertAndSend(exchangeName, "email", msg);
    }
}
