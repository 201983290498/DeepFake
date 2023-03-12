package com.coder.desgin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coder.common.util.RedisUtil;
import com.coder.common.util.RespMessageUtils;
import com.coder.common.util.TokenUtil;
import com.coder.common.util.login.VerificationCodeFactory;
import com.coder.desgin.entity.Constants;
import com.coder.desgin.entity.dto.DetectProjectDTO;
import com.coder.desgin.entity.mysql.DetectProject;
import com.coder.desgin.entity.mysql.User;
import com.coder.desgin.service.DetectProjectService;
import com.coder.desgin.service.UserService;
import io.swagger.annotations.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * todo 做一个资源拦截的工作 暂时没有解决
 * @Author coder
 * @Date 2023/2/20 0:04
 * @Description
 */
@Api(tags = {"3.检测项目接口"})
@Data
@Slf4j
@RestController
@RequestMapping("/detectProject")
public class DetectProjectController {

    private final DetectProjectService projectService;


    private final TokenUtil tokenUtil;

    private final VerificationCodeFactory verificationCodeFactory;

    private final UserService userService;

    private final RedisUtil redisUtil;


    public DetectProjectController(DetectProjectService projectService, TokenUtil tokenUtil, VerificationCodeFactory verificationCodeFactory, UserService userService, RedisUtil redisUtil) {
        this.projectService = projectService;
        this.tokenUtil = tokenUtil;
        this.verificationCodeFactory = verificationCodeFactory;
        this.userService = userService;
        this.redisUtil = redisUtil;
    }

    /**
     * @param userId 用户Id
     * @param current 当前页
     * @param pageSize 页面大小
     * @return 用户的项目记录
     * @Description : 分页查询用户的项目记录
     */
    @ApiOperation(value = "查询项目", notes = "查询用户的所有项目记录")
    @ApiImplicitParams({@ApiImplicitParam(name="userId", defaultValue="default"), @ApiImplicitParam(name="current", defaultValue="1"), @ApiImplicitParam(name="pageSize", defaultValue = "10")})
    @ApiResponse(code = 200, message = "检测成功", response = IPage.class)
    @PostMapping("/projects")
    public String getProjects(@RequestParam("userId") String userId, @RequestParam(value = "current", defaultValue = "1") Integer current, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        log.info(userId + "正在查询所有个人项目, 页码" + current + "; 页大小为" + pageSize);
        IPage<DetectProjectDTO> detectProjectDTOIPage = projectService.selectProjects(userId, current, pageSize);
        return RespMessageUtils.SUCCESS(detectProjectDTOIPage);
    }


    @ApiOperation(value="条件查询检测项目", notes="条件查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "field", defaultValue = "projectName"), @ApiImplicitParam(name = "value", defaultValue="deepfake"), @ApiImplicitParam(name = "ordered", defaultValue = "true"), @ApiImplicitParam(name="userId", defaultValue="default"), @ApiImplicitParam(name="current", defaultValue="1"), @ApiImplicitParam(name="pageSize", defaultValue = "10"), @ApiImplicitParam(name = "orderField", defaultValue="createTime")})
    @ApiResponse(code = 200, message = "检测成功", response = DetectProjectDTO.class)
    @PostMapping("project/similarSearch")
    public String getProjects(@RequestParam("userId") String userId, @RequestParam(value = "current", defaultValue = "1") Integer current, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, @RequestParam("field") String field, @RequestParam("value") String value, @RequestParam(value = "ordered", defaultValue = "true") Boolean ordered, @RequestParam(value = "orderField", defaultValue = "createTime") String orderField) {
        log.info(userId + "正在模糊查询个人项目, 页码" + current + "; 页大小为" + pageSize + "; 查询条件" + field + ": " + value + " ordered" + ordered);
        IPage<DetectProjectDTO> detectProjectDTOIPage = projectService.selectSimilarProjects(userId, current, pageSize, field, value, ordered, orderField);
        return RespMessageUtils.SUCCESS(detectProjectDTOIPage);
    }

    @ApiOperation(value = "删项目", notes = "根据列表删除项目")
    @ApiImplicitParams({@ApiImplicitParam(name = "email", defaultValue = "1023668958@qq.com"), @ApiImplicitParam(name = "token", defaultValue = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhdXRoMCIsInRpbWUiOjE2NzgyNjk0OTUwNjEsImV4cCI6MTY3ODM1NTg5NSwidXNlcm5hbWUiOiJjN2Y0ZmE1MjM0OTVlYmIxOGE3Mjk0NTVjZGQxMWY1NyJ9.FGC1oZYdqHEsLm5ufV21lGMZIzz2KS7s4i9jS1yGkHU"), @ApiImplicitParam(name = "validationInfo"), @ApiImplicitParam(name="detectIds")})
    @PostMapping("/project/delete")
    @ApiResponse(code = 200, message = "检测成功", response = RespMessageUtils.class)
    public String deleteProjectList(String userId,String email, String token, String validationInfo,String detectIds) {
        List<String> detectList = Arrays.asList(detectIds.split(","));
        if (tokenUtil.verify(token)) {
            if (!verificationCodeFactory.checkValidationInfo(email, validationInfo, Constants.DELETE) ) {
                return RespMessageUtils.ERROR("验证码错误");
            } else {
                // 验证邮箱和删除记录的相关性
                User user = new User();
                user.setUserId(userId);
                user.setEmail(email);
                if(!userService.checkAccountAndEmail(user)) {
                    return RespMessageUtils.ERROR("您邮箱对应的账号出现异常, 账号已经冻结, 请注意检测邮箱是否泄露!");
                } else if (!projectService.confirmOwnership(userId, detectList)) {
                    return RespMessageUtils.ERROR("您的账号" + userId + "正在进行非法操作! 暂时冻结响应邮箱的验证操作验证功能, 请注意检测邮箱是否泄露!");
                }
            }
            projectService.deleteList(detectList);
            return RespMessageUtils.SUCCESS();
        } else {
            return RespMessageUtils.ERROR("登入信息已过期, 请重新登入再做尝试.");
        }
    }

    @ApiOperation(value = "更新项目", notes = "根据列表删除项目")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId", defaultValue = "c7f4fa523495ebb18a729455cdd11f57"), @ApiImplicitParam(name = "detectId", defaultValue= "1789"), @ApiImplicitParam(name="projectName", defaultValue= "changeName") })
    @PostMapping("/project/update")
    @ApiResponse(code = 200, message = "检测成功", response = RespMessageUtils.class)
    public String updateProject(DetectProject project) {
        if (projectService.updateById(project)) {
            return RespMessageUtils.SUCCESS();
        } else {
            return RespMessageUtils.ERROR("账号异常, 您需要修改的项目名不属于对应的账号!");
        }
    }

    @ApiOperation(value = "增项目", notes = "新增项目")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId", defaultValue = "c7f4fa523495ebb18a729455cdd11f57"), @ApiImplicitParam(name = "mode", defaultValue = "speed"), @ApiImplicitParam(name = "projectName", defaultValue = "deepfake image detection"), @ApiImplicitParam(name = "projectLevel", defaultValue = "project"), @ApiImplicitParam(name = "fileNum", defaultValue = "0")})
    @ApiResponse(code = 200, message = "检测成功", response = RespMessageUtils.class)
    @PostMapping("/project/insert")
    public String createProject(DetectProject project,@RequestParam(value = "fileNum", defaultValue = "0") Integer fileNum) {
        DetectProject newProject = projectService.insertProject(project);
        if (fileNum != 0) {
            redisUtil.set(newProject.getDetectId().toString(), fileNum);
        }
        if (newProject.getDetectId() != null ) {
            return RespMessageUtils.SUCCESS(newProject);
        } else {
            return RespMessageUtils.ERROR("服务器异常, 暂时无法新增项目");
        }
    }

    @ApiOperation(value = "查项目--查结果", notes = "对于一些没有项目检测文本的生成项目检测文本")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId", defaultValue = "c7f4fa523495ebb18a729455cdd11f57"), @ApiImplicitParam(name = "detectId", defaultValue = "1792")})
    @ApiResponse(code = 200, message = "检测成功", response = RespMessageUtils.class)
    @PostMapping("/project/results")
    public String getProjectResults(String userId, Long detectId) {
        String url = projectService.getProjectResults(userId, detectId);
        if (url != null && (!url.equals(""))) {
            return RespMessageUtils.SUCCESS(url);
        } else {
            return RespMessageUtils.ERROR();
        }
    }
}