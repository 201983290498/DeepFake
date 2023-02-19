package com.coder.desgin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.desgin.dao.DetectProjectDao;
import com.coder.desgin.entity.mysql.DetectRecord;
import com.coder.desgin.service.DetectProjectService;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.List;

/**
 * @Author coder
 * @Date 2023/2/19 14:17
 * @Description
 */
@Service
public class DetectProjectServiceImpl implements DetectProjectService {

    private final DetectProjectDao detectProjectDao;

    public DetectProjectServiceImpl(DetectProjectDao detectProjectDao) {
        this.detectProjectDao = detectProjectDao;
    }


    @Override
    public List<DetectRecord> selectRecordsByUserId(String userId, Integer pageNum) {
        QueryWrapper wrapper = new QueryWrapper();
        Page<DetectRecord> page = new Page<>(pageNum, 20);
        wrapper.eq("user_id", userId);
        IPage<DetectRecord> iPage = detectProjectDao.selectRecords(page,  wrapper);
        return iPage.getRecords();
    }

    @Override
    public List<DetectRecord> selectAllRecords(Integer pageNum) {
        Page<DetectRecord> page = new Page<>(pageNum, 20);
        return detectProjectDao.selectAllRecords(page, null).getRecords();
    }
}
