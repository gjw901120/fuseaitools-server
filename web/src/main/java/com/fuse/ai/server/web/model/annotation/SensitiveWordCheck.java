package com.fuse.ai.server.web.model.annotation;

import com.fuse.ai.server.web.common.validation.SensitiveWordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 敏感词校验注解
 * 标注在需要过滤的字段上
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = SensitiveWordValidator.class)
public @interface SensitiveWordCheck {

    /**
     * 是否启用校验
     */
    boolean enabled() default true;

    /**
     * 是否替换敏感词（true=替换为***，false=抛出异常）
     */
    boolean replace() default false;

    Class<? extends Payload>[] payload() default {};


    /**
     * 自定义错误消息
     */
    String message() default "The input content contains prohibited words. Please modify it and try again.";

    Class<?>[] groups() default {};

    /**
     * 替换字符（默认*）
     */
    char replacement() default '*';
}