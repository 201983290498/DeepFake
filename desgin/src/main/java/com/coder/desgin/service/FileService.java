package com.coder.desgin.service;

import com.alibaba.fastjson.JSONObject;
import com.coder.desgin.entity.BaseFile;
import com.coder.desgin.entity.ImgDetectorResult;
import com.coder.desgin.entity.mysql.UploadFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @author coder
 */
public interface FileService {

    /**
     * 检测压缩文件zip
     * @param file 普通的压缩文件, 不会产生中间文件, 直接生成文件夹
     * @param request request请求，获取项目地址
     * @return 返回检测文档的url地址
     * @throws  IOException 检测文本生成错误
     */
    String detectZip(BaseFile file, HttpServletRequest request) throws IOException;

    /**
     * 检测zip文件
     * @param filePath zip文件夹
     * @param file 文件的基本信息
     * @return 返回检测url
     * @throws IOException 检测文本生成错误
     */
    String detectZip(String filePath, UploadFile file) throws IOException;


    /**
     * 对文件夹进行检测
     * @param dir 文件夹路径
     * @return 返回检测文本的结果
     */
    String detectDir(String dir, String mode);

    /**
     * 检测图片文件
     * @param file 压缩文件
     * @param request request请求，获取项目地址
     * @return 返回检测框的结果
     */
    ImgDetectorResult detectImg(BaseFile file, HttpServletRequest request);

    /**
     * 负责解析flask传来的字符串
     * @param jsonMap 代提取的字符串
     * @return 检测框
     */
    List<ImgDetectorResult> getAnalysisResult(JSONObject jsonMap);


    /**
     * 检测已知md5值的文件是都已经有了检测结果
     * @param md5 文件的md5
     * @return  返回检测结果
     */
    String checkMd5(String md5);

    /**
     * 插入一条记录, 更新所有的数据库
     * @param filePath 文件名称
     * @param baseFile 文件基本信息
     * @param result 检测结果json
     * @throws FileNotFoundException 检测文本生成出现问题
     */
    void insertRecord(String filePath, UploadFile baseFile, Object result) throws FileNotFoundException;
}
