package com.coder.desgin.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.UUID;

/**
 * 获取验证码
 * @Author coder
 * @Date 2022/11/12 14:31
 * @Description
 */
@Data
@ApiModel(value = "验证信息类--普通类")
public class ValidationInfo {

    /**
     * 验证码信息
     */
    private String message;

    /**
     * 创建的时间
     */
    private Long createTime;

    /**
     * 发送的邮箱
     */
    private String email;

    /**
     * 邮件的类型, 可以是forgetPwd和register
     */
    private String type;

    public ValidationInfo(String email, String type) {
        this.email = email;
        this.message = UUID.randomUUID().toString().substring(0, 6);
        this.createTime = System.currentTimeMillis();
        this.type = type;
    }
}
