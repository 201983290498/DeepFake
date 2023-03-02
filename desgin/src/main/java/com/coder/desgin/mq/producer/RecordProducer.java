package com.coder.desgin.mq.producer;

import com.alibaba.fastjson.JSON;
import com.coder.common.util.Md5Util;
import com.coder.desgin.entity.mysql.UploadFile;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Author coder
 * @Date 2023/3/2 18:20
 * @Description 记录添加的发布者,这里先不解耦, 直接将所有的信息发出去, 暂时也不解耦处理
 */
@Component
public class RecordProducer {

    private final AmqpTemplate amqpTemplate;

    @Value("${deepfake.ex}")
    private String exchangeName;

    @Value("${rabbitmq.params.split}")
    private String paramSplit;


    public RecordProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendRecordMsg(String filePath, UploadFile file, Object result) {
        String msg = filePath + paramSplit + file.getFileMd5() + paramSplit + file.getFileName() + paramSplit + file.getFileSize().toString() + paramSplit + file.getFileType() + paramSplit + JSON.toJSONString(result);
        amqpTemplate.convertAndSend(exchangeName, "record", msg);
        String md5;
        if (!filePath.substring(filePath.lastIndexOf(".")+1).equals("zip")) {
            md5 = Md5Util.getMd5(new File(filePath));
        } else {
            md5 = file.getFileMd5();
        }
        amqpTemplate.convertAndSend(exchangeName, "redis", md5 + paramSplit + JSON.toJSONString(result));
    }
}
