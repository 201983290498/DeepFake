package com.coder.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Date;

/**
 * @Author coder
 * @Date 2023/2/12 17:29
 * @Description
 */

@Data
@Slf4j
@Component
@Scope("singleton")
public class TokenUtil {
    /* 有效时长 */
    private static final long EXPIRE_TIME = 20*60*1000;
    // 密钥
    private static final String TOKEN_SECRET = "ben";

    private final RedisUtil redisUtil;

    public TokenUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 签名生成
     *
     * @param username 用户名
     * @return 返回签名
     */
    public String sign(String username) {
        String token = null;
        Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        token = JWT.create()
                .withIssuer("auth0")
                .withClaim("username", username)
                .withClaim("time", System.currentTimeMillis())
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
        log.info(username + ", token: " + token);
        redisUtil.set(username, token, EXPIRE_TIME/1000);
        return token;
    }

    /**
     * 验证token是否有效
     * @param token token验证
     * @return 返回签名是否有效
     */
    public boolean verify(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET))
                .withIssuer("auth0").build();
        DecodedJWT jwt = verifier.verify(token);
        String username = jwt.getClaim("username").asString();
        String newToken = (String)redisUtil.get(username);
        if (newToken != null && token.equals(newToken)) {
            log.info(username + "认证通过");
            log.info("过期时间" + jwt.getExpiresAt().toString());
            return true;
        }else {
            return false;
        }
    }
}
