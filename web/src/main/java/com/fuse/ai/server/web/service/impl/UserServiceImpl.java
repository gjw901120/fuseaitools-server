package com.fuse.ai.server.web.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.fuse.ai.server.manager.entity.User;
import com.fuse.ai.server.manager.enums.AuthTypeEnum;
import com.fuse.ai.server.manager.enums.SubscriptionPackageEnum;
import com.fuse.ai.server.manager.manager.UserManager;
import com.fuse.ai.server.web.common.enums.RedisKeysEnum;
import com.fuse.ai.server.web.common.utils.JwtTokenUtil;
import com.fuse.ai.server.web.common.utils.RedisUtil;
import com.fuse.ai.server.web.model.dto.request.user.*;
import com.fuse.ai.server.web.model.dto.response.LoginResponse;
import com.fuse.ai.server.web.model.vo.UserDetailVO;
import com.fuse.ai.server.web.service.UserService;
import com.simply.common.core.exception.BaseException;
import com.simply.common.core.exception.error.SystemErrorType;
import com.simply.common.core.exception.error.ThirdpartyErrorType;
import com.simply.common.core.exception.error.UserErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserManager userManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${app.code.expire-minutes}")
    private int codeExpireMinutes;

    @Value("${app.code.max-daily-per-email}")
    private int maxDailyPerEmail;

    @Value("${app.code.max-daily-per-ip}")
    private int maxDailyPerIp;

    @Value("${app.code.max-global-daily}")
    private int maxGlobalDaily;

    @Value("${app.google.client-id}")
    private String googleClientId;

    @Value("${app.google.client-secret}")
    private String googleClientSecret;


    @Override
    public Boolean sendEmailCode(SendEmailCodeDTO sendEmailCodeDTO, HttpServletRequest request) {
        //验证防盗刷逻辑
        checkAntiSpam(sendEmailCodeDTO.getEmail(), request);

        User user = userManager.selectByEmail(sendEmailCodeDTO.getEmail());
        //验证发送类型
        if("login".equals(sendEmailCodeDTO.getSendType()) && user == null) {
            throw new BaseException(UserErrorType.USER_ACCOUNT_NOT_EXIST);
        }

        if("register".equals(sendEmailCodeDTO.getSendType()) && user != null) {
            throw new BaseException(UserErrorType.USER_IDENTIFICATION_INVALID, "User account already exist");
        }

        //  生成验证码
        String code = generateCode();

        //  存储到Redis
        String key = RedisKeysEnum.EMAIL_CODE.format(sendEmailCodeDTO.getSendType().concat(sendEmailCodeDTO.getEmail()));

        //  发送邮件
        redisUtil.set(key, code, codeExpireMinutes, TimeUnit.MINUTES);

        //  更新发送计数
        updateSendCounters(sendEmailCodeDTO.getEmail(), request);

        return true;
    }

    @Override
    public Boolean registerByEmail(RegisterByEmailDTO registerByEmailDTO) {

        String key = RedisKeysEnum.EMAIL_CODE.format("register".concat(registerByEmailDTO.getEmail()));
        String storedCode = (String) redisUtil.get(key);
        if (!registerByEmailDTO.getCode().equals(storedCode)) {
            throw new BaseException(UserErrorType.VERIFICATION_CODE_ERROR);
        }

        User user = User.create(
                registerByEmailDTO.getUsername(),
                registerByEmailDTO.getEmail(),
                "",
                 "",
                AuthTypeEnum.EMAIL,
                0,
                0,
                SubscriptionPackageEnum.NONE,
                registerByEmailDTO.getTimeZone() == null || "".equals(registerByEmailDTO.getTimeZone()) ? "UTC" : registerByEmailDTO.getTimeZone()
        );

        userManager.insert(user);

        redisUtil.delete(key);

        return true;
    }

    @Override
    public LoginResponse loginByEmail(LoginByEmailDTO loginByEmailDTO) {
        String key = RedisKeysEnum.EMAIL_CODE.format("login".concat(loginByEmailDTO.getEmail()));
        String storedCode = (String) redisUtil.get(key);
        if (!loginByEmailDTO.getCode().equals(storedCode)) {
            throw new BaseException(UserErrorType.VERIFICATION_CODE_ERROR);
        }
        redisUtil.delete(key);
        User user = userManager.selectByEmail(loginByEmailDTO.getEmail());
        if (user == null) {
            throw new BaseException(UserErrorType.USER_ACCOUNT_NOT_EXIST);
        }
        user.setTimeZone(loginByEmailDTO.getTimeZone());
        userManager.updateById(user);

        UserJwtDTO userJwtDTO = UserJwtDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .avatar("")
                .email(user.getEmail())
                .build();

        return LoginResponse.create(jwtTokenUtil.generateToken(userJwtDTO));
    }

    @Override
    public LoginResponse loginByGoogle(LoginByGoogleDTO loginByGoogleDTO) {

//        User user = userManager.selectByEmail(loginByGoogleDTO.getEmail());

        GoogleTokenResponse tokenResponse = exchangeCodeForTokens(loginByGoogleDTO.getAuthorizationCode());


        GoogleIdToken.Payload payload = verifyIdToken(tokenResponse.getIdToken());

        User user = userManager.selectByThirdPartyId(payload.getSubject());

        UserJwtDTO userJwtDTO;

        //为空写入，在生成登录授权
        if(user == null) {
            User newUser = User.create(
                (String) payload.get("name"),
                "",
                payload.getSubject(),
                (String) payload.get("picture"),
                AuthTypeEnum.GOOGLE,
                0,
                0,
                SubscriptionPackageEnum.NONE,
                loginByGoogleDTO.getTimeZone() == null || "".equals(loginByGoogleDTO.getTimeZone()) ? "UTC" : loginByGoogleDTO.getTimeZone()
            );
            userManager.insert(newUser);
            userJwtDTO = UserJwtDTO.builder()
                    .id(newUser.getId())
                    .name(newUser.getName())
                    .avatar(newUser.getAvatar())
                    .email(newUser.getEmail())
                    .build();
        } else {
             userJwtDTO = UserJwtDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .avatar(user.getAvatar())
                    .email(user.getEmail())
                    .build();
        }


        return LoginResponse.create(jwtTokenUtil.generateToken(userJwtDTO));
    }

    /**
     * 核心方法：用授权码交换Google令牌
     */
    private GoogleTokenResponse exchangeCodeForTokens(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret); // 密钥在此安全使用
        params.add("redirect_uri", "http://localhost:8080"); // 必须与前端完全一致
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<GoogleTokenResponse> response = restTemplate.postForEntity(
                tokenUrl, request, GoogleTokenResponse.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "Google exchange token failed");
        }
        return response.getBody();
    }


    /**
     * 验证Google ID Token并返回用户信息
     */
    public GoogleIdToken.Payload verifyIdToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            // 验证Token
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new BaseException(SystemErrorType.SYSTEM_EXECUTION_ERROR,"Invalid Google ID Token");
            }

            // 提取用户信息
            return idToken.getPayload();

//            return GoogleUserInfo.builder()
//                    .googleId(payload.getSubject())
//                    .email(payload.getEmail())
//                    .emailVerified(Boolean.TRUE.equals(payload.getEmailVerified()))
//                    .name((String) payload.get("name"))
//                    .givenName((String) payload.get("given_name"))
//                    .familyName((String) payload.get("family_name"))
//                    .pictureUrl((String) payload.get("picture"))
//                    .locale((String) payload.get("locale"))
//                    .build();

        } catch (Exception e) {
            log.error("Google Token验证失败", e);
            throw new BaseException(SystemErrorType.SYSTEM_EXECUTION_ERROR, "Google login verification failed");
        }
    }

    @Override
    public UserDetailVO detail(UserJwtDTO userJwtDTO) {
        //TODO 查询user和拼接订阅，余额等信息
        User user = userManager.selectById(userJwtDTO.getId());
        return UserDetailVO.builder()
                .id(userJwtDTO.getId())
                .name(userJwtDTO.getName())
                .avatar(userJwtDTO.getAvatar())
                .email(userJwtDTO.getEmail())
                .build();
    }

    @Override
    public Boolean update(UserJwtDTO userJwtDTO, UpdateUserDTO updateUserDTO) {

        User user = userManager.selectById(userJwtDTO.getId());
        user.setName(updateUserDTO.getUsername());
        user.setAvatar(updateUserDTO.getAvatar());
        return  userManager.updateById(user) > 0;
    }

    /**
     * 完整的防刷逻辑检查
     */
    private void checkAntiSpam(String email, HttpServletRequest request) {
        String ip = getClientIp(request);
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 1. 检查邮箱当日发送次数
        String emailCountKey = RedisKeysEnum.EMAIL_CODE_SEND_COUNT.format(email, today);
        Integer emailCount = getInteger(emailCountKey);
        if (emailCount != null && emailCount >= maxDailyPerEmail) {
            throw new BaseException(UserErrorType.VERIFICATION_CODE_ERROR, "The email has reached its sending limit today");
        }

        // 2. 检查IP当日发送次数
        String ipCountKey = RedisKeysEnum.IP_SEND_COUNT.format(ip, today);
        Integer ipCount = getInteger(ipCountKey);
        if (ipCount != null && ipCount >= maxDailyPerIp) {
            throw new BaseException(UserErrorType.VERIFICATION_CODE_ERROR, "The IP has reached its maximum sending limit today");
        }

        // 3. 检查全局发送次数
        String globalCountKey = RedisKeysEnum.GLOBAL_SEND_COUNT.format(today);
        Integer globalCount = getInteger(globalCountKey);
        if (globalCount != null && globalCount >= maxGlobalDaily) {
            throw new BaseException(UserErrorType.VERIFICATION_CODE_ERROR, "The system has reached its maximum sending limit for today");
        }

        // 4. 检查IP是否被封禁
        String ipBlockKey = RedisKeysEnum.IP_BLOCK.format(ip);
        if (Boolean.TRUE.equals(redisUtil.hasKey(ipBlockKey))) {
            throw new BaseException(UserErrorType.VERIFICATION_CODE_ERROR, "The request is made too frequently. Please try again later");
        }

        // 5. 频率控制：同一邮箱60秒内只能发送一次
        String frequencyKey = "email_frequency:" + email;
        if (Boolean.TRUE.equals(redisUtil.hasKey(frequencyKey))) {
            Long ttl = redisUtil.getExpire(frequencyKey, TimeUnit.SECONDS);
            throw new BaseException(UserErrorType.VERIFICATION_CODE_ERROR, String.format("Please wait for %d seconds and try again", ttl));
        }
        redisUtil.set(frequencyKey, "1", 60, TimeUnit.SECONDS);

    }

    /**
     * 更新发送计数器
     */
    private void updateSendCounters(String email, HttpServletRequest request) {
        String ip = getClientIp(request);
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 邮箱计数
        String emailCountKey = RedisKeysEnum.EMAIL_CODE_SEND_COUNT.format(email, today);
        redisUtil.increment(emailCountKey, 1);
        redisUtil.expire(emailCountKey, 1, TimeUnit.DAYS);

        // IP计数
        String ipCountKey = RedisKeysEnum.IP_SEND_COUNT.format(ip, today);
        redisUtil.increment(ipCountKey, 1);
        redisUtil.expire(ipCountKey, 1, TimeUnit.DAYS);

        // 全局计数
        String globalCountKey = RedisKeysEnum.GLOBAL_SEND_COUNT.format(today);
        redisUtil.increment(globalCountKey, 1);
        redisUtil.expire(globalCountKey, 1, TimeUnit.DAYS);
    }


    private void sendCodeEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject("Your App - Email Verification Code");
        message.setText(String.format(
                "Your verification code is: %s\n" +
                        "Valid for: %d minutes\n" +
                        "Please do not share this code with anyone.\n\n" +
                        "If you did not request this, please ignore this email.",
                code, codeExpireMinutes
        ));

        // Send email asynchronously
        new Thread(() -> {
            try {
                mailSender.send(message);
            } catch (Exception e) {
                log.error("Failed to send email to: {}, Error: {}", to, e.getMessage());
            }
        }).start();
    }

    /**
     * 生成随机数字验证码
     */
    private String generateCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 获取客户端IP（考虑代理情况）
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.split(",")[0].trim();
    }

    private Integer getInteger(String key) {
        String value = redisUtil.get(key).toString();
        return value != null ? Integer.parseInt(value) : null;
    }

}
