package com.fuse.ai.server.manager.feign.fallback;

import com.fuse.common.core.exception.SystemErrorException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;

@Configuration
@Slf4j
public class ErrorFallback implements ErrorDecoder {
    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        String request = response.request().toString();
        String body = StreamUtils.copyToString(response.body().asInputStream(), Charset.defaultCharset());
        String errMsg = ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR.getMessage()
                + String.format(": \r\nmethodKey=%s\r\nrequest=%s\r\nresponse=%s", methodKey, request, body);
        log.error(errMsg);
        return new SystemErrorException(errMsg);
    }
}