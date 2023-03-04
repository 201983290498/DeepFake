package com.coder.desgin.service;

import com.coder.desgin.entity.mysql.UploadFile;

public interface UploadFileService {

    /**
     * 根据fileId查找检测文件的记录
     * @param fileId 文件id
     * @return 返回文件的检测记录
     */
    UploadFile selectById(String fileId);
}
