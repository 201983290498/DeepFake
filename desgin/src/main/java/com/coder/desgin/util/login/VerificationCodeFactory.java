package com.coder.desgin.util.login;

import com.coder.desgin.util.JavaEmail;
import org.springframework.stereotype.Component;

/**
 * 专门管理验证码的类
 */
@Component
public class VerificationCodeFactory {

    private final JavaEmail emailHandler;

    public VerificationCodeFactory(JavaEmail emailHandler) {
        this.emailHandler = emailHandler;
    }

}
