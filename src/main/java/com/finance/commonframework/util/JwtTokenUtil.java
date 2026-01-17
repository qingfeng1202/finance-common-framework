package com.finance.commonframework.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT令牌工具类
 * <p>
 * 提供JWT令牌的生成、验证、解析等功能
 * </p>
 *
 * @author qingfeng
 * @since 2025/12/2
 */
@Slf4j
@Component
public class JwtTokenUtil {

    /**
     * JWT密钥
     */
    @Value("${jwt.secret:your-secret-key-for-jwt-token}")
    private String secret;

    /**
     * 访问令牌过期时间（秒），默认2小时
     * -- GETTER --
     * 获取令牌过期时间（秒）
     */
    @Getter
    @Value("${jwt.expire-seconds:7200}")
    private Integer expireSeconds;

    /**
     * 签发者
     */
    @Value("${jwt.issuer:finance-system}")
    private String issuer;

    /**
     * 签名算法
     */
    private Algorithm algorithm;

    /**
     * JWT校验器
     */
    private JWTVerifier verifier;

    /**
     * Claim Key: 用户ID
     */
    private static final String CLAIM_USER_ID = "userId";
    /**
     * Claim Key: 登录账号
     */
    private static final String CLAIM_LOGIN_ACCOUNT = "loginAccount";
    /**
     * Claim Key: 组织ID
     */
    private static final String CLAIM_ORG_ID = "orgId";

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
        log.info("JWT工具类初始化完成，令牌过期时间：{}秒，签发者：{}", expireSeconds, issuer);
    }

    /**
     * 生成访问令牌
     *
     * @param userId       用户ID
     * @param loginAccount 登录账号
     * @param orgId        组织ID
     * @return 访问令牌
     */
    public String generateToken(Integer userId, String loginAccount, Integer orgId) {
        log.debug("生成访问令牌，用户ID：{}", userId);

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireSeconds * 1000);

        String token = JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(now)
                .withExpiresAt(expireDate)
                .withClaim(CLAIM_USER_ID, userId)
                .withClaim(CLAIM_LOGIN_ACCOUNT, loginAccount)
                .withClaim(CLAIM_ORG_ID, orgId)
                .sign(algorithm);

        log.debug("访问令牌生成成功，用户ID：{}，过期时间：{}", userId, expireDate);
        return token;
    }

    /**
     * 校验令牌
     *
     * @param token 令牌
     * @return true-有效，false-无效
     */
    public boolean validateToken(String token) {
        try {
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            log.warn("令牌校验失败：{}", e.getMessage());
            return false;
        }
    }

    /**
     * 解析令牌
     *
     * @param token 令牌
     * @return 解析后的JWT对象，校验失败返回null
     */
    public DecodedJWT parseToken(String token) {
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            log.warn("令牌解析失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public Integer getUserIdFromToken(String token) {
        DecodedJWT jwt = parseToken(token);
        return jwt != null ? jwt.getClaim(CLAIM_USER_ID).asInt() : null;
    }

    /**
     * 从令牌中获取登录账号
     *
     * @param token 令牌
     * @return 登录账号
     */
    public String getLoginAccountFromToken(String token) {
        DecodedJWT jwt = parseToken(token);
        return jwt != null ? jwt.getClaim(CLAIM_LOGIN_ACCOUNT).asString() : null;
    }

    /**
     * 从令牌中获取组织ID
     *
     * @param token 令牌
     * @return 组织ID
     */
    public Integer getOrgIdFromToken(String token) {
        DecodedJWT jwt = parseToken(token);
        return jwt != null ? jwt.getClaim(CLAIM_ORG_ID).asInt() : null;
    }

    /**
     * 判断令牌是否已过期
     *
     * @param token 令牌
     * @return true-已过期，false-未过期
     */
    public boolean isTokenExpired(String token) {
        DecodedJWT jwt = parseToken(token);
        if (jwt == null) {
            return true;
        }
        Date expiration = jwt.getExpiresAt();
        return expiration != null && expiration.before(new Date());
    }

    /**
     * 获取令牌过期时间
     *
     * @param token 令牌
     * @return 过期时间
     */
    public Date getExpirationFromToken(String token) {
        DecodedJWT jwt = parseToken(token);
        return jwt != null ? jwt.getExpiresAt() : null;
    }

    public Long getExpirationFromTokenMillis(String token) {
        return getExpirationFromToken(token) != null ? getExpirationFromToken(token).getTime() : null;
    }

}
