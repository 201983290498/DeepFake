package com.coder.desgin.mq.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coder.common.util.RedisUtil;
import com.coder.desgin.dao.DetectProjectDao;
import com.coder.desgin.dao.FileDao;
import com.coder.desgin.dao.ProjectFileDao;
import com.coder.desgin.entity.mysql.DetectProject;
import com.coder.desgin.entity.mysql.Image;
import com.coder.desgin.entity.mysql.ProjectFile;
import com.coder.desgin.entity.mysql.UploadFile;
import com.coder.desgin.service.DetectProjectService;
import com.coder.desgin.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author coder
 * @Date 2023/3/2 18:27
 * @Description
 */
@Component
@Slf4j
public class RecordRQService {
    @Value("${rabbitmq.params.split}")
    private String paramSplit;

    private final ImageService imageService;

    public final ProjectFileDao projectFileDao;

    private final FileDao fileDao;

    private final DetectProjectDao projectDao;

    private final DetectProjectService projectService;

    private final RedisUtil redisUtil;

    public RecordRQService(FileDao fileDao, DetectProjectDao projectDao, ImageService imageService, ProjectFileDao projectFileDao, DetectProjectService projectService, RedisUtil redisUtil) {
        this.fileDao = fileDao;
        this.projectDao = projectDao;
        this.imageService = imageService;
        this.projectFileDao = projectFileDao;
        this.projectService = projectService;
        this.redisUtil = redisUtil;
    }

    // todo 一个项目可能有多条检测记录, 对应不同的文件,这里只有一条检测记录, 有些不合理
    @Transactional
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value="${deepfake.rq.record.queue}", autoDelete = "false"), exchange = @Exchange(value="${deepfake.ex}"), key = "record"))
    public void getEmail(String msg) throws FileNotFoundException {
        String[] messages = msg.split(paramSplit);
        String md5;
        Image image;
        String projectLevel;
        // 1.上传图片或者压缩包的地址
        // 如果不是zip文件
        if (!messages[0].substring(messages[0].lastIndexOf(".")+1).equals("zip") && !messages[0].equals("on computer server")) {
            File uploadFile = new File(messages[0]);
            image = imageService.insertOneByFile(uploadFile);  // 检测图片上传在本地的位置
            md5 = image.getMd5();
            projectLevel = "image";
        } else {
            image = imageService.insertOne(new Image(messages[0], messages[1])); // 压缩包上传在本地的位置
            md5 = messages[1];
            projectLevel = "zip";
        }
        // 2. 创建项目
        DetectProject detectProject;
        if (messages.length == 8) {
            detectProject = new DetectProject(new Date(System.currentTimeMillis()),"deepfake image detection", messages[6],messages[7]);
            detectProject.setProjectLevel(projectLevel);
            projectDao.insert(detectProject);
        } else {
            detectProject = projectDao.selectById(Integer.valueOf(messages[6]));
        }
        // 3.创建文件
        UploadFile file1 = new UploadFile(messages[2], Integer.valueOf(messages[3]), messages[4], md5, image.getImageId(), messages[5], detectProject.getMode());
        file1.setFileLocation(image.getImageUrl());
        // 创建对应的检测文件
        fileDao.insert(file1);
        // 4.创建并联条目
        projectFileDao.insert(new ProjectFile(detectProject.getDetectId(), file1.getFileId()));
        // 5. 生成结果文档
        if (redisUtil.hasKey(detectProject.getDetectId().toString())) {
            Integer fileNum = (Integer) redisUtil.get(detectProject.getDetectId().toString());
            if (fileNum == 1) {
                redisUtil.del(detectProject.getDetectId().toString());
                projectService.postFinalResultTxt(detectProject.getDetectId());
            } else {
                redisUtil.set(detectProject.getDetectId().toString(), fileNum - 1);
            }
        }
    }

    /**
     * @param msg 删除项目
     */
    @Transactional
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value="${deepfake.rq.record.deleteQueue}", autoDelete = "false"), exchange = @Exchange(value = "${deepfake.ex}"), key="deleteRecords"))
    public void deleteProjectList(String msg){
        List<String> ids = (List<String>) JSON.parse(msg);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("detect_id", ids);
        // 2. 查找文件
        List<ProjectFile> temFiles = projectFileDao.selectList(wrapper);
        if (temFiles.size() != 0) {
            List<Long> fileIds = new LinkedList<>();
            for (ProjectFile file: temFiles) {
                fileIds.add(file.getFileId());
            }
            wrapper.clear();
            wrapper.in("file_id", fileIds);
            List<UploadFile> files = fileDao.selectList(wrapper);
            // 3. 删除结果文件, 检测文件暂时不删除
            List<String> results = new LinkedList<>();
            for (UploadFile file: files) {
                String fileResults = file.getFileResults();
                if (fileResults.startsWith("\"http")) {
                    results.add((String) JSON.parse(fileResults));
                }
            }
            if (results.size() > 0) {
                imageService.deleteByUrl(results);
            }
            // 4. 删除记录和对应的检测记录
            projectFileDao.delete(wrapper);
            wrapper.clear();
            wrapper.in("file_id", fileIds);
            fileDao.delete(wrapper);
        }
        projectDao.deleteBatchIds(ids);
    }
}
