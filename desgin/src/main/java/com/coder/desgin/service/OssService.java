package com.coder.desgin.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author coder
 */
public interface OssService {

    /**
     * 上传文件到oss
     * @param file 文件
     * @return 返回文件url
     * @throws IOException 文件流可能报错
     */
    String uploadFile(MultipartFile file) throws IOException;

    /**
     * 通过输入流上传文件
     * @param fileName 文件名称
     * @param imageInputStream 文件的输入流
     * @return 返回上传图片的url
     */
    String uploadFile(String fileName, InputStream imageInputStream);

    /**
     * @param fileName 文件名
     * @param image 图片的字节
     * @return 上传图片的url
     * @Description 上传图片, 通过文件字节 文件的命名规则文件名的hash值=fileName
     */
    String uploadFile(String fileName, byte[] image);

    /**
     * 上传图片, 通过url
     * @param fileName 图片的名称
     * @param url 图片的url
     * @return 文件的url
     * @throws IOException 上传错误
     */
    String uploadFile(String fileName, String url) throws IOException;

    /**
     * 创建一个新桶
     * @param bucketName 桶的名称
     * @return 是否创建成功
     */
    Boolean createBucket(String bucketName);

    /**
     * 下载文件
     * @param url 文件的url
     * @param filePath 文件的本地路径
     * @return 是否下载成功
     */
    Boolean downloadFile (String url, String filePath);

    /**
     * 下载文件, 获得文件流
     *
     * @param url 文件的url
     * @return 文件的输入流
     */
    InputStreamReader downloadFile(String url);

    /**
     * 删除文件
     * @param url 文件的url
     * @return 是否删除成功
     */
    Boolean deleteFile(String url);
}
