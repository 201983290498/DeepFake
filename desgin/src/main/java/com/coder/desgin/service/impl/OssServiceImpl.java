package com.coder.desgin.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.coder.desgin.config.OssConfiguration;
import com.coder.desgin.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * @author coder
 */
@Service
@Slf4j
public class OssServiceImpl implements OssService {

    private final OSSClient ossClient;
    private final OssConfiguration ossProperty;

    public OssServiceImpl(OSSClient ossClient, OssConfiguration ossProperty) {
        this.ossClient = ossClient;
        this.ossProperty = ossProperty;
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String finalFileName = System.currentTimeMillis() + " " + new SecureRandom().nextInt(0x0400) + suffixName;
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("image/jpg");
        PutObjectRequest putObjectRequest = new PutObjectRequest(ossProperty.getBucketName(), finalFileName, file.getInputStream(), meta);
        putObjectRequest.setProcess("true");
        PutObjectResult result = ossClient.putObject(putObjectRequest);
        if (result.getResponse().isSuccessful()){
            return formUrl(finalFileName);
        } else {
            return null;
        }
    }

    @Override
    public String uploadFile(String fileName, InputStream imageInputStream){
        ObjectMetadata meta = new ObjectMetadata();
        fileName = fileName.hashCode() + "=" + fileName;
        meta.setContentType("image/jpg");
        PutObjectRequest putObjectRequest = new PutObjectRequest(ossProperty.getBucketName(), fileName, imageInputStream, meta);
        putObjectRequest.setProcess("true");
        PutObjectResult result = ossClient.putObject(putObjectRequest);
        if (result.getResponse().isSuccessful()){
            return formUrl(fileName);
        } else {
            return null;
        }
    }

    @Override
    public String uploadFile(String fileName, byte[] image) {
        return uploadFile(fileName, new ByteArrayInputStream(image));
    }

    @Override
    public String uploadFile(String fileName, String url) throws IOException {
        return uploadFile(fileName, new URL(url).openStream());
    }


    @Override
    public Boolean createBucket(String bucketName){
        ossClient.createBucket(bucketName);
        try {
            // 创建存储空间。
            ossClient.createBucket(bucketName);
            return true;
        } catch (OSSException oe) {
            log.warn("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.warn("Error Message:" + oe.getErrorMessage());
            log.warn("Error Code:" + oe.getErrorCode());
            log.warn("Request ID:" + oe.getRequestId());
            log.warn("Host ID:" + oe.getHostId());
            return false;
        } catch (ClientException ce) {
            log.warn("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.warn("Error Message:" + ce.getMessage());
            return false;
        }
    }

    @Override
    public Boolean downloadFile(String url, String filePath) {
        try {
            ossClient.getObject(new GetObjectRequest(ossProperty.getBucketName(), getObjectNameFromUrl(url)), new File(filePath));
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public InputStreamReader downloadFile(String url1) {
        try {
//            OSSObject object = ossClient.getObject(new GetObjectRequest(ossProperty.getBucketName(), getObjectNameFromUrl(url)));
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(6000);
            urlConnection.setReadTimeout(6000);
            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("文件读取失败");
            }
            InputStream inputStream = urlConnection.getInputStream();
            return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e){
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean deleteFile(String url) {
        try {
            System.out.println("the deleteFile " + url + ossClient.doesObjectExist(new GetObjectRequest(ossProperty.getBucketName(), getObjectNameFromUrl(url))));
            ossClient.deleteObject(new GetObjectRequest(ossProperty.getBucketName(), getObjectNameFromUrl(url)));
            return true;
        } catch (Exception e){
            return false;
        }
    }

    private String formUrl(String fileName){
        return "https://" + ossProperty.getBucketName()+"."+ ossProperty.getEndpoint()+"/"+fileName;
    }
    private String getObjectNameFromUrl(String url){
        return url.substring(("https://" + ossProperty.getBucketName()+"."+ ossProperty.getEndpoint()+"/").length() + 1);
    }

}
