package com.coder.desgin.mq.service;

import com.coder.common.util.JavaEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @Author coder
 * @Date 2023/3/2 17:28
 * @Description 专门用来处理发送email消息的消费者
 */
@Component
@Slf4j
@RabbitListener(bindings = @QueueBinding(value = @Queue(value="${deepfake.rq.email.queue}", autoDelete = "false"),
        exchange = @Exchange(value="${deepfake.ex}"), key = "email"))
public class JavaEmailRQService {

    private final JavaEmail javaEmail;

    @Value("${rabbitmq.params.split}")
    private String paramSplit;
    public JavaEmailRQService(JavaEmail javaEmail) {
        this.javaEmail = javaEmail;
    }

    @RabbitHandler
    public void getEmail(String msg) {
        String[] messages = msg.split(paramSplit);
        if (messages[0].equals("html")) {
            javaEmail.sendHtmlMsg(messages[1], messages[2]);
        } else {
            try {
                javaEmail.sendMessageWithFile(messages[1], messages[2]);
            } catch (Exception e) {
                log.warn("发往" + messages[1] + "的附件" + messages[2] + "发送失败");
            }
        }
    }
}
