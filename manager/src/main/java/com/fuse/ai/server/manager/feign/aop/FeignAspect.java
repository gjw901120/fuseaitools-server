package com.fuse.ai.server.manager.feign.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

@Aspect
@Slf4j
@Component
public class FeignAspect {

    @Pointcut("execution( * com.simply.feign.client.*.*(..))")
    public void logCut() {
    }

    @Around("logCut()")
    public Object logCutAround(ProceedingJoinPoint joinPoint) throws Throwable {
        final long startTime = System.currentTimeMillis();
        log.info("Feign调用url: {}, 请求参数: {}", getUrl(joinPoint), JSON.toJSONString(joinPoint.getArgs()));
        Object responseData = joinPoint.proceed();
        log.info("Feign调用耗时: {}ms, 返回结果: {}", System.currentTimeMillis() - startTime, JSON.toJSONString(responseData));
        return responseData;
    }

    /**
     * 获取访问地址
     *
     * @param joinPoint JoinPoint
     * @return String
     */
    private String getUrl(JoinPoint joinPoint) {
        StringBuilder url = new StringBuilder();
        Method methodSignature = ((MethodSignature) joinPoint.getSignature()).getMethod();
        methodSignature.getParameters();
        //
        PostMapping postMapping = methodSignature.getAnnotation(PostMapping.class);
        if (postMapping != null && postMapping.value().length > 0) {
            url.append(postMapping.value()[0]);
        }

        GetMapping getMapping = methodSignature.getAnnotation(GetMapping.class);
        if (getMapping != null && getMapping.value().length > 0) {
            url.append(getMapping.value()[0]);
        }

        RequestMapping request = methodSignature.getAnnotation(RequestMapping.class);
        if (request != null && request.value().length > 0) {
            url.append(request.value()[0]);
        }

        DeleteMapping deleteMapping = methodSignature.getAnnotation(DeleteMapping.class);
        if (deleteMapping != null && deleteMapping.value().length > 0) {
            url.append(deleteMapping.value()[0]);
        }

        PutMapping putMapping = methodSignature.getAnnotation(PutMapping.class);
        if (putMapping != null && putMapping.value().length > 0) {
            url.append(putMapping.value()[0]);
        }

        PatchMapping patchMapping = methodSignature.getAnnotation(PatchMapping.class);
        if (patchMapping != null && patchMapping.value().length > 0) {
            url.append(patchMapping.value()[0]);
        }

        return url.toString();
    }
}
