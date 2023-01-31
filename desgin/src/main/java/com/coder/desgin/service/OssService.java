package com.coder.desgin.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OssService {

    /**
     * 上传文件到oss
     * @param file 文件
     * @return
     * @throws IOException
     */
    String uploadFile(MultipartFile file) throws IOException;
}
