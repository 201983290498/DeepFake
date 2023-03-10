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
import com.coder.desgin.entity.mysql.*;
import com.coder.desgin.mq.producer.JavaEmailProducer;
import com.coder.desgin.mq.producer.RecordProducer;
import com.coder.desgin.service.DetectProjectService;
import com.coder.desgin.service.ImageService;
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


    private final JavaEmailProducer javaEmailProducer;

    private final ImageService imageService;

    private final UserDao userDao;

    public DetectProjectServiceImpl(DetectProjectDao detectProjectDao, RecordProducer recordProducer, JavaEmailProducer javaEmailProducer, ImageService imageService, UserDao userDao) {
        this.detectProjectDao = detectProjectDao;
        this.recordProducer = recordProducer;
        this.javaEmailProducer = javaEmailProducer;
        this.imageService = imageService;
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
     * ?????????????????????????????????
     *
     * @param userId ?????????id
     * @param pageNum ??????
     */
    @Override
    public IPage<DetectRecord> getRecentDetectedImages(String userId, Integer pageNum) {
        // todo ??????QueryWrapper?????????
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
     * @param detectIds ?????????????????????Id
     * @Description ????????????
     */
    @Override
    @Transactional
    public void deleteList(List<String> detectIds) {
        recordProducer.deleteRecords(detectIds);
    }

    /**
     * @param userId    ??????Id
     * @param detectIds ??????Ids
     * @return ??????????????????????????????
     * @Description ????????????????????????????????????
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
     * ??????????????????????????????
     *
     * @param userId     ??????Id
     * @param current    ?????????????????????
     * @param pageSize   ???????????????
     * @param field      ???????????????
     * @param value      ??????????????????
     * @param ordered    ??????????????????
     * @param orderField ???????????????
     * @return ??????????????????????????????
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
     * ??????
     * todo ??????updateWrapper????????? mybatis-plus????????????,
     * 1. updateById,??????id??????, ????????????????????????????????????
     * 2. UpdateWrapper????????????, ?????????????????????entity
     * 3. ??????UpdateWrapper ????????????, 1. entity??????null, ??????update-wrapper???set???eq???????????????????????????
     * 4. ??????UpdateWrapper ????????????, 1. entity????????????????????????????????????????????????, ??????update-wrapper???eq????????????
     * @param project  ???????????????
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
     * ??????????????????
     *
     * @param project ?????????????????????
     * @return ????????????????????????
     */
    @Override
    public DetectProject insertProject(DetectProject project) {
        detectProjectDao.insert(project);
        return project;
    }

    /**
     * ????????????????????????????????????
     * 1.????????????????????????, ???????????????; 2.?????????????????????, ?????????????????????
     * @param detectId ??????Id
     * @return ?????????????????????Id
     */
    @Override
    public String postFinalResultTxt(Long detectId) {
        DetectProject detectProject = detectProjectDao.selectById(detectId);
        if ((detectProject.getProjectResultUrl() != null) && (!detectProject.getProjectResultUrl().equals(""))) {
            return detectProject.getProjectResultUrl();
        }
        List<UploadFile> projectFiles = detectProjectDao.selectFiles(detectId);
        String filePath = "./" + UUID.randomUUID().toString().substring(0,10) + ".txt";
        String line;
        try {
            Writer writer = new FileWriter(filePath);
            LinkedList<String> results = new LinkedList<>();
            for (UploadFile projectFile : projectFiles) {
                String fileResults = projectFile.getFileResults();
                if (fileResults.startsWith("https")) { // ?????????????????????
                    results.add(projectFile.getFileName() + ":\n");
                    String detectUrl = fileResults;
                    InputStreamReader inputStreamReader = imageService.downloadByUrl(detectUrl);
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    while ((line = reader.readLine()) != null) {
                        line = line.endsWith("\n") ? line : line + "\n";
                        results.add(line);
                    }
                    inputStreamReader.close();
                    reader.close();
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
            Image image = imageService.insertOneByFile(new File(filePath));
            detectProject.setProjectResultUrl(image.getImageUrl());
            detectProject.setFinishTime(new Date());
            detectProjectDao.updateById(detectProject);
            User user = userDao.selectById(detectProject.getUserId());
            javaEmailProducer.sendEmailMsg("file", user.getEmail(), new File(filePath).getAbsolutePath());
            return image.getImageUrl();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ???????????????????????????
     *
     * @param userId   ??????Id
     * @param detectId ??????Id
     * @return ?????????????????????
     */
    @Override
    public String getProjectResults(String userId, Long detectId) {
        QueryWrapper<DetectProject> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("detect_id", detectId);
        if (detectProjectDao.selectCount(wrapper) == 0) {
            return  null;
        } else {
            return postFinalResultTxt(detectId);
        }
    }
}
