package com.fuse.ai.server.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

public class RedisUtil {

    public static String getKey(String... words) {
        return StringUtils.joinWith(":", words);
    }
}
