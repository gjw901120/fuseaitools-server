package com.fuse.ai.server.manager.feign.fallback;

import com.fuse.common.core.exception.SystemErrorException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ErrorFallback implements ErrorDecoder {
    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        // 记录所有响应的详细信息（用于排查问题）

         try {
             log.debug("Feign response: methodKey={}, status={}, headers={}",
                     methodKey, response.status(), response.headers());
             return null;
         } catch (Exception e) {
             // 如果是网络连接、超时等真正的异常，才抛出
             if (e instanceof FeignException) {
                 log.error("Feign invocation error: methodKey={}", methodKey, e);
                 return new SystemErrorException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR.getMessage());
             }
             return e;
         }
    }
}