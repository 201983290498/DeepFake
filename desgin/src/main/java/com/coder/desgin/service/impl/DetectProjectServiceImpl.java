package com.coder.desgin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.common.util.StringUtil;
import com.coder.desgin.dao.DetectProjectDao;
import com.coder.desgin.dao.ProjectFileDao;
import com.coder.desgin.entity.dto.DetectProjectDTO;
import com.coder.desgin.entity.mysql.DetectRecord;
import com.coder.desgin.mq.producer.RecordProducer;
import com.coder.desgin.service.DetectProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Date;
/**
 * @Author coder
 * @Date 2023/2/19 14:17
 * @Description
 */
@Service
public class DetectProjectServiceImpl implements DetectProjectService {

    private static final Integer PAGE_SIZE = 20;

    private final DetectProjectDao detectProjectDao;

    private final RecordProducer recordProducer;

    public DetectProjectServiceImpl(DetectProjectDao detectProjectDao, RecordProducer recordProducer) {
        this.detectProjectDao = detectProjectDao;
        this.recordProducer = recordProducer;
    }


    @Override
    public IPage<DetectRecord> selectRecordsByUserId(String userId, Integer pageNum, Integer pageSize) {
        QueryWrapper wrapper = new QueryWrapper();
        Page<DetectRecord> page = new Page<>(pageNum, pageSize);
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("create_time");
        IPage<DetectRecord> iPage = detectProjectDao.selectRecords(page,  wrapper);
        return iPage;
    }


    @Override
    public List<DetectRecord> selectAllRecords(Integer pageNum) {
        Page<DetectRecord> page = new Page<>(pageNum, 20);
        return detectProjectDao.selectRecords(page, null).getRecords();
    }

    @Override
    public List<DetectRecord> selectRecordsBySql(Integer pageNum) {
        return detectProjectDao.selectRecordsBySql((pageNum-1)*PAGE_SIZE);
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
        wrapper.eq("project_level", "image");
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("create_time");
        Page<DetectRecord> page = new Page<>(pageNum, 10);
        return detectProjectDao.selectRecords(page, wrapper);
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
        if (ordered) {
            wrapper2.orderByDesc(StringUtil.camelCaseToUnderlineCase(orderField));
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
}
