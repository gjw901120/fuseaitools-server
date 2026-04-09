package com.fuse.ai.server.web.common.validation;

import com.fuse.ai.server.web.common.utils.SensitiveWordUtil;
import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 敏感词校验器
 * 配合 @SensitiveWordCheck 注解使用
 */
@Slf4j
public class SensitiveWordValidator implements ConstraintValidator<SensitiveWordCheck, String> {

    private SensitiveWordCheck annotation;

    @Override
    public void initialize(SensitiveWordCheck constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果值为空，跳过校验（由 @NotBlank 等其他注解处理）
        if (value == null || value.isEmpty()) {
            return true;
        }

        // 执行敏感词检测
        SensitiveWordUtil.ValidationResult result = SensitiveWordUtil.validate(value);

        // 如果没有敏感词，通过校验
        if (result.isValid()) {
            return true;
        }

        // 如果有敏感词且配置了替换，则执行替换
        if (annotation.replace()) {
            // 注意：这里无法直接修改原值，需要在业务层处理
            log.warn("检测到敏感词: {}，将进行替换", result.getBadWords());
            return true; // 校验通过，但需要在 Controller 中执行替换
        }

        // 否则，抛出校验异常
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                annotation.message() + " Irregular word: " + String.join(", ", result.getBadWords())
        ).addConstraintViolation();

        return false;
    }
}