package com.coder.desgin.mq.producer;

import com.alibaba.fastjson.JSON;
import com.coder.common.util.Md5Util;
import com.coder.desgin.entity.mysql.UploadFile;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

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

    public void sendRecordMsg(String filePath, UploadFile file, Object detectResult) {
        String msg = filePath + paramSplit + file.getFileMd5() + paramSplit + file.getFileName() + paramSplit + file.getFileSize().toString() + paramSplit + file.getFileType() + paramSplit + JSON.toJSONString(detectResult) + paramSplit + file.getUserId() + paramSplit + file.getMode();
        amqpTemplate.convertAndSend(exchangeName, "record", msg);
    }

    /**
     * todo 待检测
     * @param detectIds 需要删除的项目ids
     * @Description 生产者方的删除项目
     */
    public void deleteRecords(List<String> detectIds) {
        String msg = JSON.toJSONString(detectIds);
        amqpTemplate.convertAndSend(exchangeName, "deleteRecords", msg);
    }

}
