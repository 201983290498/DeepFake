package com.coder.desgin.controller;

import com.coder.desgin.entity.NormalDetectionFile;
import com.coder.desgin.service.FileService;
import com.coder.desgin.service.impl.NormalDetectionServiceImpl;
import com.coder.desgin.entity.ImgDetectorResult;
import com.coder.desgin.entity.BaseFile;
import com.coder.common.util.RespMessageUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author coder
 * @Date 2022/11/3 13:42
 * @Description
 */
@Slf4j
@Data
@Controller(value = "uploadFileController")
public class FileController {

    private final FileService uploadFileService;
    private final NormalDetectionServiceImpl normalDetectionService;

    private static String ZIP = "zip";

    public FileController(FileService uploadFileService, NormalDetectionServiceImpl normalDetectionService) {
        this.uploadFileService = uploadFileService;
        this.normalDetectionService = normalDetectionService;
    }

    /**
     * @param file 上传的文件
     * @param request 自动注入request请求
     * @return 返回检测结果，或者显示检测结果的保存地址
     */
    @ResponseBody
    @PostMapping("/deepfake/upload")
    public String uploadFile(@RequestBody BaseFile file, HttpServletRequest request) {
        // 处理压缩文件
        if (file.getType().contains(ZIP)) {
            String resultsFile = uploadFileService.detectZip(file, request);
            return RespMessageUtils.SUCCESS(resultsFile);
        } else {// 处理单个文件
            ImgDetectorResult result = uploadFileService.detectImg(file, request);
            return RespMessageUtils.SUCCESS(result);
        }
    }


    @ResponseBody
    @PostMapping("/normal/upload")
    public String uploadFile(@RequestBody NormalDetectionFile file, HttpServletRequest request) {
        if (file.getType().contains(ZIP)) {
            String resultsFile = normalDetectionService.detectZip(file, request);
            return RespMessageUtils.SUCCESS(resultsFile);
        } else {// 处理单个文件
            String resultImgBase64 = normalDetectionService.detectImg(file, request);
            return RespMessageUtils.SUCCESS(resultImgBase64);
        }
    }
}
