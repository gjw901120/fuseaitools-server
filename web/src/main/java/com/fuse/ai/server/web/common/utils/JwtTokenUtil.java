package com.fuse.ai.server.web.common.utils;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret:yourSuperSecretKeyHereAtLeast32BytesLong}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 默认24小时
    private Long expiration;

    private SecretKey getSigningKey() {
        // 确保密钥长度满足HS256算法要求（至少32字节）
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 为 UserDetailVO 生成 Token
     */
    public String generateToken(UserJwtDTO userJwtDTO) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", userJwtDTO.getName());
        claims.put("avatar", userJwtDTO.getAvatar());
        claims.put("email", userJwtDTO.getEmail()); // 邮箱作为普通Claim
        // 使用用户ID作为Token的主题(Subject)
        return doGenerateToken(claims, userJwtDTO.getId().toString());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) // 设置自定义信息
                .setSubject(subject) // 通常用邮箱作为主题
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 验证 Token 是否有效 (仅验证完整性和过期时间)
     * 适用于过滤器中的初步校验
     */
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token); // 如果能成功解析且未过期，则有效
            return true;
        } catch (ExpiredJwtException e) {
            // Token 已过期
            throw new RuntimeException("Token已过期", e);
        } catch (JwtException | IllegalArgumentException e) {
            // Token 无效、被篡改或格式错误
            throw new RuntimeException("Token无效", e);
        }
    }

    /**
     * 从 Token 中提取 UserDetailVO - 从Subject取ID
     */
    public UserJwtDTO extractUserDetailVO(String token) {
        final Claims claims = extractAllClaims(token);

        return UserJwtDTO.builder()
                // Subject 现在是ID的字符串形式
                .id(Integer.valueOf(claims.getSubject()))
                .email(claims.get("email", String.class))
                .name(claims.get("name", String.class))
                .avatar(claims.get("avatar", String.class))
                .build();
    }

    /**
     * 通用提取方法 (保留，便于提取单个字段，如邮箱)
     */
    public Integer extractUserID(String token) {
        return Integer.parseInt(extractClaim(token, Claims::getSubject)); // 现在返回的是ID字符串
    }


    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}