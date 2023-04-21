package com.coder.desgin.mq.producer;

import com.alibaba.fastjson.JSON;
import com.coder.desgin.entity.TempFileInfoVO;
import com.coder.desgin.entity.mysql.UploadFile;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public void sendRecordMsg(String filePath, UploadFile file, String detectResult) {
        String msg = filePath + paramSplit + file.getFileMd5() + paramSplit + file.getFileName() + paramSplit + file.getFileSize().toString() + paramSplit + file.getFileType() + paramSplit + detectResult + paramSplit + file.getUserId() + paramSplit + file.getMode();
        amqpTemplate.convertAndSend(exchangeName, "record", msg);
    }

    public void sendRecordMsg(String filePath, TempFileInfoVO file, String detectResult) {
        String msg = filePath + paramSplit + file.getUniqueIdentifier() + paramSplit + file.getName() + paramSplit + file.getSize().toString() + paramSplit + file.getFileType() + paramSplit + detectResult + paramSplit + file.getDetectId();
        amqpTemplate.convertAndSend(exchangeName, "record", msg);
    }

    /**
     * @param detectIds 需要删除的项目ids
     * @Description 生产者方的删除项目
     */
    public void deleteRecords(List<String> detectIds) {
        String msg = JSON.toJSONString(detectIds);
        amqpTemplate.convertAndSend(exchangeName, "deleteRecords", msg);
    }

    /**
     * 添加记录
     * @param fileId 文件Id
     * @param userId 用户Id
     */
    public void sendRecordMsg(Long fileId, String userId, String mode) {
        String msg = fileId + paramSplit + userId + paramSplit + mode;
        amqpTemplate.convertAndSend(exchangeName, "record_project", msg);
    }
}
