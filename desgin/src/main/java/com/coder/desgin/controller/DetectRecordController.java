package com.coder.desgin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coder.common.util.RespMessageUtils;
import com.coder.desgin.entity.dto.DetectProjectDTO;
import com.coder.desgin.entity.mysql.DetectRecord;
import com.coder.desgin.entity.mysql.UploadFile;
import com.coder.desgin.service.DetectProjectService;
import com.coder.desgin.service.UploadFileService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author coder
 * @Date 2023/3/10 15:45
 * @Description
 */
@Api(tags = {"9. 检测记录相关接口"})
@RestController
@RequestMapping("/detectProject")
@Slf4j
public class DetectRecordController {
    private final DetectProjectService projectService;

    public DetectRecordController(DetectProjectService projectService) {
        this.projectService = projectService;
    }

    @ApiOperation(value="条件查询--查检测记录", notes="条件查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "field", defaultValue = "projectName"), @ApiImplicitParam(name = "value", defaultValue="deepfake"), @ApiImplicitParam(name = "ordered", defaultValue = "true"), @ApiImplicitParam(name="userId", defaultValue="default"), @ApiImplicitParam(name="current", defaultValue="1"), @ApiImplicitParam(name="pageSize", defaultValue = "10"), @ApiImplicitParam(name = "orderField", defaultValue="createTime")})
    @ApiResponse(code = 200, message = "检测成功", response = DetectProjectDTO.class)
    @PostMapping("record/similarSearch")
    public String getRecords(String userId, @RequestParam(value = "current", defaultValue = "1") Integer current, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, String field, String value, @RequestParam(value = "ordered", defaultValue = "true") Boolean ordered, @RequestParam(value = "orderField", defaultValue = "createTime") String orderField) {
        log.info(userId + "正在模糊查询个人检测记录, 页码" + current + "; 页大小为" + pageSize + "; 查询条件" + field + ": " + value + " ordered" + ordered);
        IPage<DetectRecord> detectProjectDTOIPage = projectService.selectSimilarRecords(userId, current, pageSize, field, value, ordered, orderField);
        return RespMessageUtils.SUCCESS(detectProjectDTOIPage);
    }

    /**
     * @param userId 用户id
     * @param page 页码
     * @return 返回浏览记录的列表
     * @Description 获取用户最近的检测记录, 每页固定是10条记录
     */
    @ApiOperation(value = "查检测图片", notes = "查用户最近的检测图片")
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


    @ApiOperation("查记录")
    @PostMapping("/records")
    public String getAllDetectionRecords(String userId, Integer pageNum, Integer pageSize) {
        log.info(userId + "正在查询所有个人检测记录");
        if (pageNum == null){
            pageNum = 1;
        }
        IPage<DetectRecord> detectRecordIPage = projectService.selectRecordsByUserId(userId, pageNum, pageSize);
        return RespMessageUtils.SUCCESS(detectRecordIPage);
    }

}
