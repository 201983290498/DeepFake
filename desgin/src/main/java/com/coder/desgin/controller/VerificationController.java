package com.coder.desgin.controller;

import com.coder.common.util.RespMessageUtils;
import com.coder.common.util.login.VerificationCodeFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author coder
 * @Date 2023/3/8 19:13
 * @Description 验证码的发送和检验
 */
@RestController
@Slf4j
@Api(tags ={"7.认证相关接口"})
public class VerificationController {

    private final VerificationCodeFactory verificationCodeFactory;

    public VerificationController(VerificationCodeFactory verificationCodeFactory) {
        this.verificationCodeFactory = verificationCodeFactory;
    }

    /**
     * 生成验证码
     * @param email 需要发送的网络邮箱
     * @return 返回是否发送验证码
     */
    @PostMapping("/verification/genMsg")
    @ApiOperation("生成认证--发邮件验证码")
    public String generateMsg(String email, String type, Long expireTime){
        verificationCodeFactory.sendValidationInfo(email, type, expireTime);
        return RespMessageUtils.SUCCESS("验证码已发送, 请注意查收!");
    }

    /**
     * 验证码校验
     * @param email 需要校验的邮箱
     * @param message 需要检验的信息
     * @return 返回验证码是否正确
     */
    @PostMapping("/verification/checkMsg")
    @ApiOperation("核验身份--验证邮件验证码")
    public String checkMsg(String email, String message, String type) {
        if (!verificationCodeFactory.checkValidationInfo(email, message, type)) {
            return RespMessageUtils.ERROR("验证码错误");
        } else {
            return RespMessageUtils.SUCCESS();
        }
    }

}
