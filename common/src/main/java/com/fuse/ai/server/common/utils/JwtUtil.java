package com.fuse.ai.server.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.SystemErrorType;
import com.fuse.common.core.exception.error.UserErrorType;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.Date;

@Slf4j
public class JwtUtil {

    /**
     * 用户ID的KEY
     */
    private static final String USER_ID_KEY = "uid";

    /**
     * ISSUER
     */
    private static final String ISSUER = "fuse";

    public static String generateUserToken(BigInteger userId, String jwtSecretKey) {
        Algorithm algorithm = Algorithm.HMAC512(jwtSecretKey);

        Date issuedAt = new Date();

        try {
            return JWT.create()
                    .withClaim(USER_ID_KEY, userId.toString())
                    .withIssuer(ISSUER)
                    .withIssuedAt(issuedAt)
                    .sign(algorithm);
        } catch (JWTCreationException e){
            log.error("生成JWT token失败", e);
            throw new BaseException(SystemErrorType.SYSTEM_EXECUTION_ERROR, "generate JWT token fail");
        }
    }

    public static long verifyAndGetUserId(String token, String jwtSecretKey) {
        return Long.parseLong(verifyAndGetStringValue(token, USER_ID_KEY, jwtSecretKey));
    }

    public static BigInteger getUserId(String token) {
        return new BigInteger(getStringValue(token));
    }

    private static String verifyAndGetStringValue(String token, String name, String jwtSecretKey) {
        try {
            DecodedJWT decodedJWT = verify(token,jwtSecretKey);
            return decodedJWT.getClaim(name).asString();
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid user token[" + token + "] fail:" + e.getMessage(), e);
        }
    }

    private static DecodedJWT verify(String token, String jwtSecretKey) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(jwtSecretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build(); //Reusable verifier instance
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new BaseException(UserErrorType.USER_IDENTIFICATION_INVALID, "Invalid token");
        }
    }

    private static String getStringValue(String token) {
        try {
            return JWT.decode(token).getClaim(JwtUtil.USER_ID_KEY).asString();
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid user token:" + token, e);
        }
    }

}
