package com.coder.desgin.controller;

import com.coder.common.util.RespMessageUtils;
import com.coder.desgin.entity.dto.DetectProjectDTO;
import com.coder.desgin.entity.mysql.UploadFile;
import com.coder.desgin.service.UploadFileService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

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

    @ApiOperation("查记录--检测文件")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId", defaultValue = "c7f4fa523495ebb18a729455cdd11f57"), @ApiImplicitParam(name = "detectId", defaultValue = ""), @ApiImplicitParam(name = "fileType", value = "image")})
    @ApiResponse(code = 200, message = "检测成功", response = DetectProjectDTO.class)
    @PostMapping("/files")
    public String getAllRecords(@RequestParam("userId") String userId, @RequestParam(value = "detectId", required = false, defaultValue = "") String detectId, @RequestParam(value = "fileType", required = false, defaultValue = "") String fileType) {
        List<UploadFile> files = uploadFileService.selectFiles(userId, detectId, fileType);
        if (files == null || files.size() == 0) {
            return RespMessageUtils.SUCCESS(new LinkedList<UploadFile>());
        } else {
            return RespMessageUtils.SUCCESS(files);
        }
    }
}
