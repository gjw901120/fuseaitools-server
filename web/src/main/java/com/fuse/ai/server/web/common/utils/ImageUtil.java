package com.fuse.ai.server.web.common.utils;

import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

@Component
public class ImageUtil {

    /**
     * 获取图标Base64（Spring Boot版本）
     */
    public String getImageBase64(String imageUrl) {
        try {
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (compatible; IconFetcher/1.0)");
            headers.set("Accept", "image/*");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            // 发送请求
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    imageUrl,
                    HttpMethod.GET,
                    entity,
                    byte[].class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "HTTP request failed: " + response.getStatusCode());
            }

            byte[] iconData = response.getBody();
            String contentType = response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);

            if (iconData == null || iconData.length == 0) {
                throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "Image data is empty");
            }

            // 转换为Base64
            String base64 = Base64Utils.encodeToString(iconData);

            // 直接返回完整Data URI
            return "data:" + contentType + ";base64," + base64;

        } catch (Exception e) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR,"Failed to get image from URL: " + imageUrl + ", error: " + e.getMessage(), e);
        }
    }
}
