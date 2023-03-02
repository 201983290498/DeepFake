package com.coder.desgin.mq.service;

import com.coder.common.util.Md5Util;
import com.coder.desgin.dao.DetectProjectDao;
import com.coder.desgin.dao.FileDao;
import com.coder.desgin.dao.ProjectFileDao;
import com.coder.desgin.entity.mysql.DetectProject;
import com.coder.desgin.entity.mysql.Image;
import com.coder.desgin.entity.mysql.ProjectFile;
import com.coder.desgin.entity.mysql.UploadFile;
import com.coder.desgin.mq.producer.RedisProducer;
import com.coder.desgin.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;

/**
 * @Author coder
 * @Date 2023/3/2 18:27
 * @Description
 */
@Component
@Slf4j
@RabbitListener(bindings = @QueueBinding(value = @Queue(value="${deepfake.rq.record.queue}", autoDelete = "false"),
        exchange = @Exchange(value="${deepfake.ex}", type= ExchangeTypes.DIRECT, durable = "true"), key = "record"))
public class RecordRQService {
    @Value("${rabbitmq.params.split}")
    private String paramSplit;

    private final ImageService imageService;

    public final ProjectFileDao projectFileDao;

    private final FileDao fileDao;

    private final DetectProjectDao projectDao;

    public RecordRQService(FileDao fileDao, RedisProducer redis, DetectProjectDao projectDao, ImageService imageService, ProjectFileDao projectFileDao) {
        this.fileDao = fileDao;
        this.projectDao = projectDao;
        this.imageService = imageService;
        this.projectFileDao = projectFileDao;
    }

    @RabbitHandler
    public void getEmail(String msg) throws FileNotFoundException {
        String[] messages = msg.split(paramSplit);
        DetectProject detectProject = new DetectProject(new Date(System.currentTimeMillis()),"deepfake image detection");
        String md5;
        // 1.上传图片, 上传图片或者文件
        Image image;
        // 如果不是zip文件
        if (!messages[0].substring(messages[0].lastIndexOf(".")+1).equals("zip")) {
            File uploadFile = new File(messages[0]);
            image = imageService.insertOne(uploadFile);
            md5 = Md5Util.getMd5(uploadFile);
        } else {
            image = imageService.insertOne(new Image(messages[0]));
            md5 = messages[1];
        }
        // 创建默认项目
        projectDao.insert(detectProject);
        UploadFile file1 = new UploadFile(messages[2], Integer.valueOf(messages[3]), messages[4], md5, image.getImageId(), messages[5]);
        // 创建对应的检测文件
        fileDao.insert(file1);
        // 创建并联条目
        projectFileDao.insert(new ProjectFile(detectProject.getDetectId(), file1.getFileId()));
        // 更新redis
    }
}
