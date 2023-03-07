package com.coder.desgin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coder.common.util.RespMessageUtils;
import com.coder.desgin.entity.dto.DetectProjectDTO;
import com.coder.desgin.entity.mysql.DetectRecord;
import com.coder.desgin.entity.mysql.UploadFile;
import com.coder.desgin.service.DetectProjectService;
import com.coder.desgin.service.UploadFileService;
import io.swagger.annotations.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;


/**
 * todo 做一个资源拦截的工作
 * @Author coder
 * @Date 2023/2/20 0:04
 * @Description
 */
@Api(tags = {"检测项目相关接口"})
@Data
@Slf4j
@RestController
@RequestMapping("/detectProject")
public class DetectProjectController {

    private final DetectProjectService projectService;

    private final UploadFileService uploadFileService;

    public DetectProjectController(DetectProjectService projectService, UploadFileService uploadFileService) {
        this.projectService = projectService;
        this.uploadFileService = uploadFileService;
    }

    @PostMapping("/records")
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
    public String getRecentDetectedImage(@RequestParam("userId") String userId, @RequestParam("page") Integer page) {
        List<String> recordLinks = new LinkedList<>();
        IPage<DetectRecord> recentDetectedImages = projectService.getRecentDetectedImages(userId, page);
        for(DetectRecord records: recentDetectedImages.getRecords()){
            recordLinks.add(records.getFileLocation());
        }
        if (recordLinks.size() == 0) {
            return RespMessageUtils.ERROR();
        } else {
            return RespMessageUtils.SUCCESS(recordLinks);
        }
    }

    /**
     * @param userId 用户Id
     * @param current 当前页
     * @param pageSize 页面大小
     * @return 用户的项目记录
     * @Description : 分页查询用户的项目记录
     */
    @ApiOperation(value = "查询项目", notes = "查询用户的所有项目记录")
    @ApiImplicitParams({@ApiImplicitParam(name="userId", value="default"), @ApiImplicitParam(name="current", value="1"), @ApiImplicitParam(name="pageSize", value = "10")})
    @ApiResponse(code = 200, message = "检测成功", response = DetectProjectDTO.class)
    @PostMapping("/projects")
    public String getProjects(@RequestParam("userId") String userId, @RequestParam(value = "current", defaultValue = "1") Integer current, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        log.info(userId + "正在查询所有个人检测记录");
        IPage<DetectProjectDTO> detectProjectDTOIPage = projectService.selectProjects(userId, current, pageSize);
        return RespMessageUtils.SUCCESS(detectProjectDTOIPage);
    }
}
