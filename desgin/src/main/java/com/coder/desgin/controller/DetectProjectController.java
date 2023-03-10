package com.coder.desgin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coder.common.util.RespMessageUtils;
import com.coder.common.util.TokenUtil;
import com.coder.common.util.login.VerificationCodeFactory;
import com.coder.desgin.entity.Constants;
import com.coder.desgin.entity.dto.DetectProjectDTO;
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


    public DetectProjectController(DetectProjectService projectService, TokenUtil tokenUtil, VerificationCodeFactory verificationCodeFactory, UserService userService) {
        this.projectService = projectService;
        this.tokenUtil = tokenUtil;
        this.verificationCodeFactory = verificationCodeFactory;
        this.userService = userService;
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


    @ApiOperation(value="条件查询检测项目", notes="条件查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "field", value = "projectName"), @ApiImplicitParam(name = "value", value="deepfake"), @ApiImplicitParam(name = "ordered", value = "true"), @ApiImplicitParam(name="userId", value="default"), @ApiImplicitParam(name="current", value="1"), @ApiImplicitParam(name="pageSize", value = "10"), @ApiImplicitParam(name = "orderField", value="createTime")})
    @ApiResponse(code = 200, message = "检测成功", response = DetectProjectDTO.class)
    @PostMapping("project/similarSearch")
    public String getProjects(@RequestParam("userId") String userId, @RequestParam(value = "current", defaultValue = "1") Integer current, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, @RequestParam("field") String field, @RequestParam("value") String value, @RequestParam(value = "ordered", defaultValue = "true") Boolean ordered, @RequestParam(value = "orderField", defaultValue = "createTime") String orderField) {
        log.info(userId + "正在模糊查询个人项目, 页码" + current + "; 页大小为" + pageSize + "; 查询条件" + field + ": " + value + " ordered" + ordered);
        IPage<DetectProjectDTO> detectProjectDTOIPage = projectService.selectSimilarProjects(userId, current, pageSize, field, value, ordered, orderField);
        return RespMessageUtils.SUCCESS(detectProjectDTOIPage);
    }

    @ApiOperation(value = "删项目", notes = "根据列表删除项目")
    @ApiImplicitParams({@ApiImplicitParam(name = "email", value = "c7f4fa523495ebb18a729455cdd11f57"), @ApiImplicitParam(name = "token", value = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhdXRoMCIsInRpbWUiOjE2NzgyNjk0OTUwNjEsImV4cCI6MTY3ODM1NTg5NSwidXNlcm5hbWUiOiJjN2Y0ZmE1MjM0OTVlYmIxOGE3Mjk0NTVjZGQxMWY1NyJ9.FGC1oZYdqHEsLm5ufV21lGMZIzz2KS7s4i9jS1yGkHU"), @ApiImplicitParam(name = "validationInfo"), @ApiImplicitParam(name="detectIds")})
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
}
