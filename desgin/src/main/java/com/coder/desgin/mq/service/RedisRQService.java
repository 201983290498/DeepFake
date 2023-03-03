package com.coder.desgin.mq.service;

import com.coder.common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author coder
 * @Date 2023/3/2 18:43
 * @Description 负责redis的更新
 */
@Component
@Slf4j
@RabbitListener(bindings = @QueueBinding(value = @Queue(value="${deepfake.rq.redis.queue}", autoDelete = "false"),
        exchange = @Exchange(value="${deepfake.ex}"), key = "redis"))
public class RedisRQService {
    private final RedisUtil redisUtil;

    @Value("${rabbitmq.params.split}")
    private String paramSplit;

    public RedisRQService(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @RabbitHandler
    void getMessage(String msg) {
        String[] messages = msg.split(paramSplit);
        if (messages.length == 3)
            redisUtil.set(messages[0], messages[1], Long.parseLong(messages[2]));
        else
            redisUtil.set(messages[0], messages[1], redisUtil.getEXPIRE_TIME());
    }
}
