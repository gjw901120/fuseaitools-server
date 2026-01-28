package com.fuse.ai.server.web.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.HashMap;

/**
 * 飞书机器人消息工具类
 * 仅包含发送文本消息功能
 */
@Slf4j
public class FeishuMessageUtil {

    private static final RestTemplate restTemplate = new RestTemplate();

    /**
     * 飞书机器人API基础地址
     */
    private static final String FEISHU_BOT_API = "https://open.feishu.cn/open-apis/bot/v2/hook/";

    //异常
    private static final String EXCEPTION_WEBHOOK_KEY = "https://open.feishu.cn/open-apis/bot/v2/hook/566015bf-606c-4603-9ba3-eff3992f9660";

    //充值 && 订阅
    private static final String RECHARGE_SUBSCRIBE_WEBHOOK_KEY = "https://open.feishu.cn/open-apis/bot/v2/hook/1c445e1e-43e8-4509-beed-900ccd5b029c";

    /**
     * 发送文本消息到飞书机器人
     *
     * @param webhookKey 机器人webhook key（完整URL或key）
     * @param content    消息内容
     * @return 是否发送成功
     */
    public static boolean sendTextMessage(String webhookKey, String content) {
        return sendTextMessage(webhookKey, content, false, (String[]) null);
    }

    public static boolean sendExceptionMessage(String content) {
        return sendTextMessage(EXCEPTION_WEBHOOK_KEY, content, false, (String[]) null);
    }

    public static boolean sendRechargeSubscribeMessage(String content) {
        return sendTextMessage(RECHARGE_SUBSCRIBE_WEBHOOK_KEY, content, false, (String[]) null);
    }

    /**
     * 发送文本消息到飞书机器人（支持@功能）
     *
     * @param webhookKey 机器人webhook key
     * @param content    消息内容
     * @param atAll      是否@所有人
     * @param atUserIds  需要@的用户ID列表（可选）
     * @return 是否发送成功
     */
    public static boolean sendTextMessage(String webhookKey, String content, boolean atAll, String... atUserIds) {
        // 参数校验
        if (StringUtils.isBlank(webhookKey)) {
            log.error("飞书机器人webhook key为空");
            return false;
        }

        if (StringUtils.isBlank(content)) {
            log.error("飞书消息内容为空");
            return false;
        }

        try {
            // 构建消息
            Map<String, Object> message = buildTextMessage(content, atAll, atUserIds);

            // 发送消息
            return sendToFeishu(webhookKey, message);

        } catch (Exception e) {
            log.error("发送飞书消息失败, webhookKey: {}, content: {}", webhookKey, content, e);
            return false;
        }
    }

    /**
     * 构建文本消息JSON
     */
    private static Map<String, Object> buildTextMessage(String content, boolean atAll, String[] atUserIds) {
        Map<String, Object> message = new HashMap<>();
        message.put("msg_type", "text");

        // 构建消息内容
        StringBuilder textContent = new StringBuilder(content);

        // 添加@所有人
        if (atAll) {
            textContent.append("\n<at user_id=\"all\">所有人</at>");
        }

        // 添加@特定用户
        if (atUserIds != null && atUserIds.length > 0) {
            for (String userId : atUserIds) {
                if (StringUtils.isNotBlank(userId)) {
                    textContent.append(String.format("\n<at user_id=\"%s\">用户</at>", userId));
                }
            }
        }

        Map<String, String> contentMap = new HashMap<>();
        contentMap.put("text", textContent.toString());

        message.put("content", contentMap);
        return message;
    }

    /**
     * 实际发送HTTP请求到飞书
     */
    private static boolean sendToFeishu(String webhookKey, Map<String, Object> message) {
        try {
            // 构建完整URL（支持传入完整URL或仅key）
            String webhookUrl = webhookKey;
            if (!webhookKey.startsWith("http")) {
                webhookUrl = FEISHU_BOT_API + webhookKey;
            }

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 创建请求实体
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(message, headers);

            // 发送POST请求
            ResponseEntity<Map> response = restTemplate.exchange(
                    webhookUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            // 处理响应
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null) {
                    Integer code = (Integer) responseBody.get("code");
                    String msg = (String) responseBody.get("msg");

                    if (code != null && code == 0) {
                        log.debug("飞书消息发送成功");
                        return true;
                    } else {
                        log.error("飞书消息发送失败, code: {}, msg: {}", code, msg);
                        return false;
                    }
                }
            }

            log.error("飞书消息发送失败, HTTP状态码: {}", response.getStatusCode());
            return false;

        } catch (Exception e) {
            log.error("飞书消息发送异常", e);
            return false;
        }
    }

    /**
     * 简单发送消息（无需@功能）
     *
     * @param webhookUrl 完整的webhook URL
     * @param message    消息内容
     * @return 是否发送成功
     */
    public static boolean sendSimpleMessage(String webhookUrl, String message) {
        if (StringUtils.isBlank(webhookUrl) || StringUtils.isBlank(message)) {
            return false;
        }
        return sendTextMessage(webhookUrl, message);
    }
}