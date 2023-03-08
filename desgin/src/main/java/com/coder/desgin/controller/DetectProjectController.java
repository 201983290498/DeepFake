package com.coder.desgin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coder.common.util.RespMessageUtils;
import com.coder.common.util.TokenUtil;
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
@Api(tags = {"检测项目接口"})
@Data
@Slf4j
@RestController
@RequestMapping("/detectProject")
public class DetectProjectController {

    private final DetectProjectService projectService;

    private final UploadFileService uploadFileService;

    private final TokenUtil tokenUtil;

    public DetectProjectController(DetectProjectService projectService, UploadFileService uploadFileService, TokenUtil tokenUtil) {
        this.projectService = projectService;
        this.uploadFileService = uploadFileService;
        this.tokenUtil = tokenUtil;
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
    @ApiResponse(code = 200, message = "检测成功", response = IPage.class)
    @PostMapping("/projects")
    public String getProjects(@RequestParam("userId") String userId, @RequestParam(value = "current", defaultValue = "1") Integer current, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        log.info(userId + "正在查询所有个人项目, 页码" + current + "; 页大小为" + pageSize);
        IPage<DetectProjectDTO> detectProjectDTOIPage = projectService.selectProjects(userId, current, pageSize);
        return RespMessageUtils.SUCCESS(detectProjectDTOIPage);
    }
    @ApiOperation(value="对项目记录进行条件查询", notes="条件查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "field", value = "projectName"), @ApiImplicitParam(name = "value", value="deepfake"), @ApiImplicitParam(name = "ordered", value = "true"), @ApiImplicitParam(name="userId", value="default"), @ApiImplicitParam(name="current", value="1"), @ApiImplicitParam(name="pageSize", value = "10"), @ApiImplicitParam(name = "orderField", value="createTime")})
    @ApiResponse(code = 200, message = "检测成功", response = DetectProjectDTO.class)
    @PostMapping("project/similarSearch")
    public String getProjects(@RequestParam("userId") String userId, @RequestParam(value = "current", defaultValue = "1") Integer current, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, @RequestParam("field")String field, @RequestParam("value") String value, @RequestParam(value = "ordered", defaultValue = "true") Boolean ordered, @RequestParam(value = "orderField", defaultValue = "createTime") String orderField) {
        log.info(userId + "正在模糊查询个人项目, 页码" + current + "; 页大小为" + pageSize + "; 查询条件" + field + ": " + value + " ordered" + ordered);
        IPage<DetectProjectDTO> detectProjectDTOIPage = projectService.selectSimilarProjects(userId, current, pageSize, field, value, ordered, orderField);
        return RespMessageUtils.SUCCESS(detectProjectDTOIPage);
    }

    @ApiOperation(value = "删用户", notes = "根据列表删除用户")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "c7f4fa523495ebb18a729455cdd11f57"), @ApiImplicitParam(name = "token", value = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhdXRoMCIsInRpbWUiOjE2NzgyNjk0OTUwNjEsImV4cCI6MTY3ODM1NTg5NSwidXNlcm5hbWUiOiJjN2Y0ZmE1MjM0OTVlYmIxOGE3Mjk0NTVjZGQxMWY1NyJ9.FGC1oZYdqHEsLm5ufV21lGMZIzz2KS7s4i9jS1yGkHU"), @ApiImplicitParam(name = "validationInfo"), @ApiImplicitParam(name="detectIds")})
    @PostMapping("/projects/delete")
    @ApiResponse(code = 200, message = "检测成功", response = RespMessageUtils.class)
    public String deleteProjectList(String userId, String token, String validationInfo,List<String> detectIds) {
        if (tokenUtil.verify(token)) {
            projectService.deleteList(detectIds);
            return RespMessageUtils.SUCCESS();
        } else {
            return RespMessageUtils.ERROR("登入信息已过期, 请重新登入再做尝试.");
        }
    }
}
