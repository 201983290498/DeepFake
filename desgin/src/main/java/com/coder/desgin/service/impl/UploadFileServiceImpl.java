package com.coder.desgin.service.impl;

import com.coder.desgin.dao.FileDao;
import com.coder.desgin.entity.mysql.UploadFile;
import com.coder.desgin.service.UploadFileService;
import org.springframework.stereotype.Service;

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
}
