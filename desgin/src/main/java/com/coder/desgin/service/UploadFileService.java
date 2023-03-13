package com.coder.desgin.service;

import com.coder.desgin.entity.mysql.UploadFile;

import java.util.List;

public interface UploadFileService {

    /**
     * 根据fileId查找检测文件的记录
     * @param fileId 文件id
     * @return 返回文件的检测记录
     */
    UploadFile selectById(String fileId);

    /**
     * 按照要求搜索文件
     * @param userId 用户Id
     * @param detectId 项目Id
     * @param fileType 文件类型
     * @return 返回满足条件的文件
     */
    List<UploadFile> selectFiles(String userId, String detectId, String fileType);
}
