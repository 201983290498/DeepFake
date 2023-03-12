package com.coder.desgin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.common.util.StringUtil;
import com.coder.desgin.dao.DetectProjectDao;
import com.coder.desgin.dao.UserDao;
import com.coder.desgin.entity.DetectorRect;
import com.coder.desgin.entity.ImgDetectorResult;
import com.coder.desgin.entity.dto.DetectProjectDTO;
import com.coder.desgin.entity.mysql.DetectProject;
import com.coder.desgin.entity.mysql.DetectRecord;
import com.coder.desgin.entity.mysql.UploadFile;
import com.coder.desgin.entity.mysql.User;
import com.coder.desgin.mq.producer.JavaEmailProducer;
import com.coder.desgin.mq.producer.RecordProducer;
import com.coder.desgin.service.DetectProjectService;
import com.coder.desgin.service.OssService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

/**
 * @Author coder
 * @Date 2023/2/19 14:17
 * @Description
 */
@Service
public class DetectProjectServiceImpl implements DetectProjectService {

    private static final Integer PAGE_SIZE = 10;

    private final DetectProjectDao detectProjectDao;

    private final RecordProducer recordProducer;

    private final OssService ossService;

    private final JavaEmailProducer javaEmailProducer;

    private final UserDao userDao;

    public DetectProjectServiceImpl(DetectProjectDao detectProjectDao, RecordProducer recordProducer, OssService ossService, JavaEmailProducer javaEmailProducer, UserDao userDao) {
        this.detectProjectDao = detectProjectDao;
        this.recordProducer = recordProducer;
        this.ossService = ossService;
        this.javaEmailProducer = javaEmailProducer;
        this.userDao = userDao;
    }


    @Override
    public IPage<DetectRecord> selectRecordsByUserId(String userId, Integer pageNum, Integer pageSize) {
        QueryWrapper wrapper = new QueryWrapper();
        Page<DetectRecord> page = new Page<>(pageNum, pageSize);
        wrapper.eq("user_id", userId);
        QueryWrapper wrapper2 = new QueryWrapper();
        wrapper2.orderByDesc("create_time");
        return detectProjectDao.selectRecords(page, wrapper, wrapper2);
    }

    /**
     * 获取用户最近的检测记录
     *
     * @param userId 用户的id
     * @param pageNum 页码
     */
    @Override
    public IPage<DetectRecord> getRecentDetectedImages(String userId, Integer pageNum) {
        // todo 学习QueryWrapper的使用
        QueryWrapper wrapper = new QueryWrapper();
        QueryWrapper wrapper2 = new QueryWrapper();
        wrapper.eq("project_level", "image");
        wrapper.eq("user_id", userId);
        wrapper2.orderByDesc("create_time");
        Page<DetectRecord> page = new Page<>(pageNum, 10);
        return detectProjectDao.selectRecords(page, wrapper, wrapper2);
    }

    @Override
    public IPage<DetectProjectDTO> selectProjects(String userId, Integer current, Integer pageSize) {
        Page<DetectProjectDTO> page = new Page<>(current, pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        QueryWrapper wrapper2 = new QueryWrapper();
        wrapper.eq("user_id", userId);
        wrapper2.orderByDesc("create_time");
        return detectProjectDao.selectProjects(page, wrapper, wrapper2);
    }

    @Override
    public IPage<DetectProjectDTO> selectSimilarProjects(String userId, Integer current, Integer pageSize, String field, String value, Boolean ordered, String orderField) {
        Page<DetectProjectDTO> page = new Page<>(current, pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id", userId);
        QueryWrapper wrapper2 = new QueryWrapper();
        switch (field) {
            case "projectName":
            case "projectLevel":
            case "mode":
                wrapper.like(StringUtil.camelCaseToUnderlineCase(field), value);
                break;
            case "detectId":
                wrapper.like("cast(detect_id AS CHAR)", value);
                break;
            case "imageQuantity":
                wrapper2.having("count(" + StringUtil.camelCaseToUnderlineCase(field) +") >=" + value );
                break;
            case "createTime":
                wrapper.gt(StringUtil.camelCaseToUnderlineCase(field), new Date(Long.valueOf(value)));
                break;
        }
        if (ordered == null) {
            return detectProjectDao.selectProjects(page, wrapper, wrapper2);
        }
        if (ordered) {
            wrapper2.orderByDesc(StringUtil.camelCaseToUnderlineCase(orderField));
        } else {
            wrapper2.orderByAsc(StringUtil.camelCaseToUnderlineCase(orderField));
        }
        return detectProjectDao.selectProjects(page, wrapper, wrapper2);
    }

    /**
     * @param detectIds 需要删除的项目Id
     * @Description 删除项目
     */
    @Override
    @Transactional
    public void deleteList(List<String> detectIds) {
        recordProducer.deleteRecords(detectIds);
    }

    /**
     * @param userId    用户Id
     * @param detectIds 项目Ids
     * @return 返回是否存在所属关系
     * @Description 检验用户和项目的所属关系
     */
    @Override
    public boolean confirmOwnership(String userId, List<String> detectIds) {
        HashSet<String> ids = new HashSet<>(detectIds);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id", userId);
        wrapper.in("detect_id", ids);
        Integer recordNum = detectProjectDao.selectCount(wrapper);
        return recordNum == detectIds.size();
    }

    /**
     * 条件查询用户检测记录
     *
     * @param userId     用户Id
     * @param current    当前的查询页码
     * @param pageSize   页面的大小
     * @param field      查询的字段
     * @param value      查询字段的值
     * @param ordered    按照什么排序
     * @param orderField 排序的顺序
     * @return 返回满足条件查询的值
     */
    @Override
    public IPage<DetectRecord> selectSimilarRecords(String userId, Integer current, Integer pageSize, String field, String value, Boolean ordered, String orderField) {
        Page<DetectRecord> page = new Page<>(current, pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id", userId);
        QueryWrapper wrapper2 = new QueryWrapper();
        switch (field) {
            case "projectName":
            case "projectLevel":
            case "mode":
                wrapper.like(StringUtil.camelCaseToUnderlineCase(field), value);
                break;
            case "detectFile":
                wrapper2.apply(StringUtil.camelCaseToUnderlineCase(field) + " like '%" + value + "%'");
                break;
            case "createTime":
                wrapper.gt(StringUtil.camelCaseToUnderlineCase(field), new Date(Long.parseLong(value)));
                break;
        }
        if (ordered == null) {
            return detectProjectDao.selectRecords(page, wrapper, wrapper2);
        }
        if (ordered) {
            wrapper2.orderByDesc(StringUtil.camelCaseToUnderlineCase(orderField));
        } else {
            wrapper2.orderByAsc(StringUtil.camelCaseToUnderlineCase(orderField));
        }
        return detectProjectDao.selectRecords(page, wrapper, wrapper2);
    }

    /**
     * 更新
     * todo 学习updateWrapper的使用 mybatis-plus更新字段,
     * 1. updateById,除了id之后, 设置什么字段更新什么字段
     * 2. UpdateWrapper设置字段, 传入需要更新的entity
     * 3. 使用UpdateWrapper 条件更新, 1. entity传入null, 通过update-wrapper的set和eq设置条件和修改的值
     * 4. 使用UpdateWrapper 条件更新, 1. entity传入一个新对象设置需要更新的属性, 通过update-wrapper的eq设置条件
     * @param project  更新的实体
     */
    @Override
    public boolean updateById(DetectProject project) {
        UpdateWrapper<DetectProject> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", project.getUserId());
        updateWrapper.eq("detect_id", project.getDetectId());
        int update = detectProjectDao.update(project, updateWrapper);
        return update == 1;
    }

    /**
     * 新增一个项目
     *
     * @param project 项目的基本信息
     * @return 返回插入后的信息
     */
    @Override
    public DetectProject insertProject(DetectProject project) {
        detectProjectDao.insert(project);
        return project;
    }

    /**
     * 根据检测项目生成检测文件
     *
     * @param detectId 项目Id
     */
    @Override
    public void postFinalResultTxt(Long detectId) {
        List<UploadFile> projectFiles = detectProjectDao.selectFiles(detectId);
        String filePath = "./" + UUID.randomUUID().toString().substring(0,10) + ".txt";
        String line;
        try {
            Writer writer = new FileWriter(filePath);
            LinkedList<String> results = new LinkedList<>();
            for (UploadFile projectFile : projectFiles) {
                String fileResults = projectFile.getFileResults();
                if (fileResults.startsWith("\"https")) { // 如果是检测文件
                    results.add(projectFile.getFileName() + ":\n");
                    String detectUrl = JSON.parseObject(fileResults, String.class);
                    InputStreamReader inputStreamReader = ossService.downloadFile(detectUrl);
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    while ((line = reader.readLine()) != null) {
                        line = line.endsWith("\n") ? line : line + "\n";
                        results.add(line);
                    }
                } else {
                    ImgDetectorResult imgDetectorResult = JSON.parseObject(fileResults, ImgDetectorResult.class);
                    for (DetectorRect rect: imgDetectorResult.getRects()) {
                        results.add(imgDetectorResult.getImageName() + ":    " + rect.toString() + "\n");
                    }
                }
            }
            for (String lin: results) {
                writer.write(lin);
            }
            writer.close();
            DetectProject detectProject = detectProjectDao.selectById(detectId);
            User user = userDao.selectById(detectProject.getUserId());
            javaEmailProducer.sendEmailMsg("file", user.getEmail(), new File(filePath).getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
