package com.fuse.ai.server.web.common.enums;

public enum RedisKeysEnum {

    // 验证码相关
    EMAIL_CODE("email_code:%s", 5 * 60L),           // 5分钟过期
    EMAIL_CODE_SEND_COUNT("email_send_count:%s:%s", 24 * 60 * 60L), // 邮箱发送次数
    IP_SEND_COUNT("ip_send_count:%s:%s", 24 * 60 * 60L), // IP发送次数
    GLOBAL_SEND_COUNT("global_send_count:%s", 24 * 60 * 60L), // 全局发送次数

    // 防刷相关
    EMAIL_ATTEMPT_COUNT("email_attempt:%s:%s", 10 * 60L), // 验证尝试次数
    IP_BLOCK("ip_block:%s", 30 * 60L);                 // IP封禁


    private final String pattern;
    private final Long defaultTtl;

    RedisKeysEnum(String pattern, Long defaultTtl) {
        this.pattern = pattern;
        this.defaultTtl = defaultTtl;
    }

    public String format(Object... args) {
        return String.format(pattern, args);
    }

    public Long getDefaultTtl() {
        return defaultTtl;
    }
}
