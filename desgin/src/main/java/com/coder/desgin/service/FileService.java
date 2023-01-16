package com.coder.desgin.service;

import com.alibaba.fastjson.JSONObject;
import com.coder.desgin.entity.BaseFile;
import com.coder.desgin.entity.ImgDetectorResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FileService {

    /**
     * 检测压缩文件zip
     * @param file 压缩文件
     * @param request request请求，获取项目地址
     * @return 返回检测文档的地址
     */
    String detectZip(BaseFile file, HttpServletRequest request);

    String detectZip(String filePath);

    /**
     * 对文件夹进行检测
     * @param dir 文件夹路径
     * @return 返回检测文本的结果
     */
    String detectDir(String dir);

    /**
     * 检测图片文件
     * @param file 压缩文件
     * @param request request请求，获取项目地址
     * @return 返回检测框的结果
     */
    ImgDetectorResult detectImg(BaseFile file, HttpServletRequest request);

    /**
     * 负责解析flask传来的字符串
     * @param jsonMap
     * @return
     */
    List<ImgDetectorResult> getAnalysisResult(JSONObject jsonMap);


}
