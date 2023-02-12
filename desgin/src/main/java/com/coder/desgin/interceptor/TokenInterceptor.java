package com.coder.desgin.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.coder.common.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author coder
 * @Date 2023/2/12 19:52
 * @Description
 */
@Component
@Scope("singleton")
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    final TokenUtil tokenUtil;

    public TokenInterceptor(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception{
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("token");
        if (token != null) {
            boolean result = tokenUtil.verify(token);
            if (result) {
                return true;
            }
        }
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            JSONObject json = new JSONObject();
            json.put("result", false);
            json.put("msg", "token verify fail");
            json.put("code", "500");
            response.getWriter().append(json.toJSONString());
            log.warn("认证失败, 未通过拦截器");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
            return false;
        }
        return false;
    }
}
