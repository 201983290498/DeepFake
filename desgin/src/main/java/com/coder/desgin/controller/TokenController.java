package com.coder.desgin.controller;

import com.alipay.api.internal.util.StringUtils;
import com.coder.common.util.RespMessageUtils;
import com.coder.common.util.TokenUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TokenController {

    private final TokenUtil tokenUtil;

    public TokenController(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @PostMapping("/authorize")
    @ResponseBody
    public String checkToken(String token) {
        if (StringUtils.isEmpty(token) || token.equals("null")) {
            return RespMessageUtils.ERROR();
        }
        if (token.lastIndexOf(" ") > 0) {
            token = token.substring(token.lastIndexOf(" ") + 1);
        }
        boolean verify = tokenUtil.verify(token);
        return (verify? RespMessageUtils.SUCCESS(): RespMessageUtils.ERROR());
    }
}
