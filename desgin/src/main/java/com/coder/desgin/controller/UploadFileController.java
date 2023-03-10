package com.coder.desgin.controller;

import com.coder.common.util.RespMessageUtils;
import com.coder.desgin.entity.mysql.UploadFile;
import com.coder.desgin.service.UploadFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author coder
 * @Date 2023/3/10 15:43
 * @Description
 */
@Api(tags = {"8. 检测文件相关接口"})
@RestController
@RequestMapping("/detectProject")
public class UploadFileController {

    private final UploadFileService uploadFileService;

    public UploadFileController(UploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
    }

    /**
     * 根据fileId找检测记录, 获取到的检测记录
     * @param fileId 文件Id
     * @return 返回uploadFile
     */
    @ApiOperation(value = "查检测文件", notes = "根据检测文件Id查检测文件")
    @PostMapping("/detectedFile")
    public String getDetectedFile(String fileId) {
        UploadFile uploadFile = uploadFileService.selectById(fileId);
        return RespMessageUtils.SUCCESS(uploadFile);
    }
}
