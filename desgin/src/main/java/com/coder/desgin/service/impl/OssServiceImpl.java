package com.coder.desgin.service.impl;

import com.aliyun.oss.OSSClient;
import com.coder.desgin.config.OSSConfiguration;
import com.coder.desgin.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;

@Service
public class OssServiceImpl implements OssService {

    private final OSSConfiguration ossProperty;

    public OssServiceImpl(OSSConfiguration ossProperty) {
        this.ossProperty = ossProperty;
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        OSSClient ossClient = new OSSClient(ossProperty.getEndpoint(), ossProperty.getAccessKeyId(), ossProperty.getAccessKeySecret());
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String finalFileName = System.currentTimeMillis() + " " + new SecureRandom().nextInt(0x0400) + suffixName;
        return null;
    }
}
