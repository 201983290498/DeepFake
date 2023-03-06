package com.coder.desgin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coder.common.util.RespMessageUtils;
import com.coder.desgin.entity.mysql.DetectRecord;
import com.coder.desgin.entity.mysql.UploadFile;
import com.coder.desgin.service.DetectProjectService;
import com.coder.desgin.service.UploadFileService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;


/**
 * todo 做一个资源拦截的工作
 * @Author coder
 * @Date 2023/2/20 0:04
 * @Description
 */
@Data
@Slf4j
@Controller
@RequestMapping("/detectProject")
public class DetectProjectController {

    private final DetectProjectService projectService;

    private final UploadFileService uploadFileService;

    public DetectProjectController(DetectProjectService projectService, UploadFileService uploadFileService) {
        this.projectService = projectService;
        this.uploadFileService = uploadFileService;
    }

    @PostMapping("/records")
    @ResponseBody
    public String getAllDetectionRecords(String userId, Integer pageNum, Integer pageSize) {
        log.info(userId + "正在查询所有个人检测记录");
        if (pageNum == null){
            pageNum = 1;
        }
        IPage<DetectRecord> detectRecordIPage = projectService.selectRecordsByUserId(userId, pageNum, pageSize);
        return RespMessageUtils.SUCCESS(detectRecordIPage);
    }

    /**
     * 根据fileId找检测记录, 获取到的检测记录
     * @param fileId 文件Id
     * @return 返回uploadFile
     */
    @PostMapping("/detectedFile")
    public String getDetectedFile(String fileId) {
        UploadFile uploadFile = uploadFileService.selectById(fileId);
        return RespMessageUtils.SUCCESS(uploadFile);
    }

    /**
     * @param userId 用户id
     * @param page 页码
     * @return 返回浏览记录的列表
     * @Description 获取用户最近的检测记录, 每页固定是10条记录
     */
    @PostMapping("/recent/images")
    @ResponseBody
    public String getRecentDetectedImage(@RequestParam("userId") String userId, @RequestParam("page") Integer page) {
        List<String> recordLinks = new LinkedList<>();
        IPage<DetectRecord> recentDetectedImages = projectService.getRecentDetectedImages(userId, page);
        for(DetectRecord records: recentDetectedImages.getRecords()){
            recordLinks.add(records.getFileLocation());
        }
        return RespMessageUtils.SUCCESS(recordLinks);
    }
}
