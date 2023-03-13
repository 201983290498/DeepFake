package com.coder.desgin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coder.desgin.dao.FileDao;
import com.coder.desgin.entity.mysql.UploadFile;
import com.coder.desgin.service.UploadFileService;
import org.springframework.data.web.config.QuerydslWebConfiguration;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author coder
 * @Date 2023/3/3 23:00
 * @Description
 */
@Service
public class UploadFileServiceImpl implements UploadFileService {

    private final FileDao fileDao;

    public UploadFileServiceImpl(FileDao fileDao) {
        this.fileDao = fileDao;
    }

    public UploadFile selectById(String fileId) {
        return fileDao.selectById(fileId);
    }


    @Override
    public List<UploadFile> selectFiles(String userId, String detectId, String fileType) {
        QueryWrapper<UploadFile> wrapper = new QueryWrapper<>();
        QueryWrapper<UploadFile> wrapper2 = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        if (!detectId.equals("")) {
            wrapper.eq("detect_id", detectId);
        }
        if (!fileType.equals("")) {
            wrapper2.apply("file_type like '%"  + fileType + "%'");
        }
        return fileDao.selectListCondition(wrapper, wrapper2);
    }
}
