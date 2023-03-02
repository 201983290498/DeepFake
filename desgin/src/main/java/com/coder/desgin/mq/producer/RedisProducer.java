package com.coder.desgin.mq.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author coder
 * @Date 2023/3/2 18:41
 * @Description
 */
@Component
public class RedisProducer {
    private final AmqpTemplate amqpTemplate;

    @Value("${deepfake.ex}")
    private String exchangeName;

    @Value("${rabbitmq.params.split}")
    private String paramSplit;

    public RedisProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendMsg(String key , String value, Long timeout){
        if (timeout == null)
            amqpTemplate.convertAndSend(exchangeName, "redis", key+paramSplit+value);
        else
            amqpTemplate.convertAndSend(exchangeName, "redis", key+paramSplit+value+ timeout);
    }
}
