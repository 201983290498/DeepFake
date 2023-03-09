package com.coder.desgin.controller;

import com.alipay.api.internal.util.StringUtils;
import com.coder.common.util.RespMessageUtils;
import com.coder.common.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(tags ={"5.认证相关接口"})
public class TokenController {

    private final TokenUtil tokenUtil;

    public TokenController(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    /**
     * @param token 认证的token
     * @return 认证用户登入是否过期
     * @Description redis保存token的方式, 就是用户名: token
     */
    @PostMapping("/authorize")
    @ResponseBody
    @ApiOperation("认证token")
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
