package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.*;
import com.fuse.ai.server.manager.enums.*;
import com.fuse.ai.server.manager.manager.*;
import com.fuse.ai.server.web.common.enums.RedisKeysEnum;
import com.fuse.ai.server.web.common.utils.BrevoEmailSender;
import com.fuse.ai.server.web.common.utils.EmailSenderUtil;
import com.fuse.ai.server.web.common.utils.JwtTokenUtil;
import com.fuse.ai.server.web.common.utils.RedisUtil;
import com.fuse.ai.server.web.model.dto.request.user.LoginByEmailDTO;
import com.fuse.ai.server.web.model.dto.request.user.SendEmailCodeDTO;
import com.fuse.ai.server.web.model.dto.request.user.UpdateUserDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.LoginResponse;
import com.fuse.ai.server.web.model.vo.CreditsDetailVO;
import com.fuse.ai.server.web.model.vo.UserDetailVO;
import com.fuse.ai.server.web.service.UserService;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.SystemErrorType;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import com.fuse.common.core.exception.error.UserErrorType;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EmailSenderUtil emailSenderUtil;

    @Autowired
    private UserManager userManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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

    @Autowired
    private BrevoEmailSender brevoEmailSender;

    @Autowired
    private BillManager billManager;

    @Autowired
    private UserCreditsManager userCreditsManager;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private SubscriptionConfigManager subscriptionConfigManager;

    @Autowired
    private SubscriptionManager subscriptionManager;

    @Autowired
    private ModelsManager modelsManager;

    @Autowired
    private ModelsCategoryManager modelsCategoryManager;

    @Autowired
    private UserModelRecordsManager userModelRecordsManager;


    @Override
    public Boolean sendEmailCode(SendEmailCodeDTO sendEmailCodeDTO, HttpServletRequest request) {
        //验证防盗刷逻辑
        checkAntiSpam(sendEmailCodeDTO.getEmail(), request);

        //  生成验证码
        String code = generateCode();

        //  存储到Redis
        String key = RedisKeysEnum.EMAIL_CODE.format(sendEmailCodeDTO.getEmail());

        //  发送邮件
        redisUtil.set(key, code, codeExpireMinutes, TimeUnit.MINUTES);

        //  更新发送计数
        updateSendCounters(sendEmailCodeDTO.getEmail(), request);

        brevoEmailSender.sendEmail(sendEmailCodeDTO.getEmail(), code, codeExpireMinutes);

        return true;
    }

    @Override
    public LoginResponse loginByEmail(LoginByEmailDTO loginByEmailDTO) {
        String key = RedisKeysEnum.EMAIL_CODE.format(loginByEmailDTO.getEmail());
        String storedCode = (String) redisUtil.get(key);
        if (!loginByEmailDTO.getCode().equals(storedCode)) {
            throw new BaseException(UserErrorType.VERIFICATION_CODE_ERROR, "The verification code is incorrect");
        }
        redisUtil.delete(key);
        User user = userManager.selectByEmail(loginByEmailDTO.getEmail());
        if (user == null) {
            user = User.create(
                    "",
                    loginByEmailDTO.getEmail(),
                    "",
                    "",
                    AuthTypeEnum.fromJson(AuthTypeEnum.EMAIL.getCode()),
                    0,
                    0,
                    SubscriptionPackageEnum.NONE,
                    "",
                    0
            );
            userManager.insert(user);
        } else {
            userManager.updateById(user);
        }

        UserJwtDTO userJwtDTO = UserJwtDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .avatar("")
                .email(user.getEmail())
                .build();

        return LoginResponse.create(jwtTokenUtil.generateToken(userJwtDTO));
    }

    @Override
    public LoginResponse loginByGoogle(String code) {

//        User user = userManager.selectByEmail(loginByGoogleDTO.getEmail());

        GoogleTokenResponse tokenResponse = exchangeCodeForTokens(code);


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
                AuthTypeEnum.fromJson(AuthTypeEnum.GOOGLE.getCode()),
                0,
                0,
                SubscriptionPackageEnum.NONE,
                "",
                0
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
        try {
            // 1. 创建HTTP传输和JSON工厂
            NetHttpTransport httpTransport = new NetHttpTransport();
            GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            // 2. 使用Google官方API构建令牌请求
            return new GoogleAuthorizationCodeTokenRequest(
                    httpTransport,
                    jsonFactory,
                    "https://oauth2.googleapis.com/token",
                    googleClientId,
                    googleClientSecret,
                    authorizationCode,
                    "http://127.0.0.1:8080/api/user/login/google/callback") // 请确认与前端完全一致
                    .execute();

        } catch (IOException e) {
            log.error("Failed to exchange code for Google tokens", e);
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR,
                    "Google token exchange failed: " + e.getMessage());
        }
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
    public UserDetailVO detail(UserJwtDTO userJwtDTO, String timeZone) {
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

    @Override
    public CreditsDetailVO creditsDetail(Integer userId, Integer page, Integer size) {
        page = (page == null || page <= 0) ? 1 : page;
        size = (size == null || size <= 0) ? 10 : size;

        CreditsDetailVO creditsDetailVO = new CreditsDetailVO();
        List<CreditsDetailVO.CreditsDetail> creditsDetails = new ArrayList<>();
        CreditsDetailVO.SubscriptionDetail subscriptionDetail = new CreditsDetailVO.SubscriptionDetail();
        CreditsDetailVO.RechargeDetail rechargeDetail = new CreditsDetailVO.RechargeDetail();

        User user = userManager.selectById(userId);

        creditsDetailVO.setIsSubscription(0);
        creditsDetailVO.setIsRecharge(0);
        if(user.getIsSubscription() == 1) {
            Order order = orderManager.selectByUserIdAndType(userId, OrderTypeEnum.SUBSCRIPTION);
            if(order != null) {
                creditsDetailVO.setIsSubscription(1);
                SubscriptionConfig subscriptionConfig = subscriptionConfigManager.getDetailById(order.getConfigId());
                UserCredits userCredits = userCreditsManager.getDetailByUserIdAndType(userId, 2); // 2: Subscription
                Subscription subscription = subscriptionManager.selectByOrderId(order.getId());
                subscriptionDetail.setCredits(subscriptionConfig.getTotalCredits());
                subscriptionDetail.setRemainingCredits(userCredits.getCredits());
                subscriptionDetail.setRatio(userCredits.getCredits().divide(subscriptionConfig.getTotalCredits(), 2, RoundingMode.HALF_UP));
                subscriptionDetail.setStartDate(subscription.getStartDate());
                subscriptionDetail.setEndDate(subscription.getEndDate());
                subscriptionDetail.setPackageType(SubscriptionPackageEnum.of(subscriptionConfig.getSubscriptionPackage()).getDescription());
                subscriptionDetail.setDiscount(subscriptionConfig.getDiscount());
                subscriptionDetail.setType(SubscriptionTypeEnum.of(subscriptionConfig.getType()).getDescription());
            }
        }

        if(user.getIsTopUp() == 1) {
            Order order = orderManager.selectByUserIdAndType(userId, OrderTypeEnum.TOP_UP);
            if(order != null) {
                creditsDetailVO.setIsRecharge(1);
                UserCredits userCredits = userCreditsManager.getDetailByUserIdAndType(userId, 1);
                rechargeDetail.setRemainingCredits(userCredits.getCredits());
            }
        }

        List<Models> modelsList = modelsManager.getAll();
        Map<Integer, String> modelMap = modelsList.stream()
                .collect(Collectors.toMap(Models::getId, Models::getName));
        List<UserModelRecords> userModelRecordsList = userModelRecordsManager.getListByUserId(userId);
        Map<String, UserModelRecords> recordsMap = userModelRecordsList.stream()
                .collect(Collectors.toMap(
                        UserModelRecords::getUuid,  // key 映射函数
                        Function.identity()
                ));
        List<ModelsCategory> categoryList = modelsCategoryManager.getAll();
        Map<Integer, String> modelsIdToCategoryNameMap = modelsList.stream()
                .collect(Collectors.toMap(
                        Models::getId,
                        model -> {
                            // 查找对应的分类名称
                            return categoryList.stream()
                                    .filter(category -> category.getId().equals(model.getCategoryId()))
                                    .findFirst()
                                    .map(ModelsCategory::getName)
                                    .orElse("");
                        },
                        (oldValue, newValue) -> oldValue  // 处理重复键
                ));

        List<Bill> bills = billManager.getListByUserId(userId, page, size);
        if(!bills.isEmpty()) {
            for (Bill bill :bills) {
                CreditsDetailVO.CreditsDetail creditsDetail = new CreditsDetailVO.CreditsDetail();
                //处理veo3特殊逻辑
                String model = modelMap.get(bill.getModelId()) == null ? "" : modelMap.get(bill.getModelId());
                if(VeoModelEnum.VEO3.getCode().equals(model) || VeoModelEnum.VEO3_FAST.getCode().equals(model)) {
                    String generationType = "";
                    if (recordsMap.get(bill.getRecordId()).getOriginalData() != null) {
                        Map<String, Object> dataMap = (Map<String, Object>) recordsMap.get(bill.getRecordId()).getOriginalData();
                        generationType = (String) dataMap.get("generationType");
                    }
                    creditsDetail.setModel(generationType);
                } else {
                    creditsDetail.setModel(model);
                }
                creditsDetail.setCredits(bill.getSubscriptionDeductCredits());
                creditsDetail.setDiscountCredits(bill.getRechargeDeductCredits().add(bill.getSubscriptionDeductCredits()));
                creditsDetail.setStatus(bill.getStatus().getDescription());
                creditsDetail.setTitle(recordsMap.get(bill.getRecordId()).getTitle());
                creditsDetail.setModelCategory(modelsIdToCategoryNameMap.get(bill.getModelId()));
                creditsDetail.setDiscount(bill.getDiscount());
                creditsDetail.setCompletedDate(bill.getGmtModified().plusHours(user.getTimeZoneOffset()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                creditsDetail.setRecordId(bill.getRecordId());
                creditsDetails.add(creditsDetail);
            }
        }


        creditsDetailVO.setSubscriptionDetail(subscriptionDetail);
        creditsDetailVO.setRechargeDetail(rechargeDetail);
        creditsDetailVO.setCreditsDetails(creditsDetails);

        return creditsDetailVO;
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

        String messageId = emailSenderUtil.sendEmail(
                to,
                "Your App - Email Verification Code",
                String.format(
                        "Your verification code is: %s\n" +
                                "Valid for: %d minutes\n" +
                                "Please do not share this code with anyone.\n\n" +
                                "If you did not request this, please ignore this email.",
                        code, codeExpireMinutes
                )
        );
        log.info("Email sent successfully. Message ID: {}", messageId);
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
        Object value = redisUtil.get(key);
        return value != null ? Integer.parseInt(value.toString()) : null;
    }

}
