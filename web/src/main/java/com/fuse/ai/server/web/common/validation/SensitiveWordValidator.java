package com.fuse.ai.server.web.common.validation;

import com.fuse.ai.server.web.common.utils.SensitiveWordUtil;
import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 敏感词校验器（增强版：支持单词边界匹配）
 * 配合 @SensitiveWordCheck 注解使用
 */
@Slf4j
public class SensitiveWordValidator implements ConstraintValidator<SensitiveWordCheck, String> {

    private SensitiveWordCheck annotation;

    // 预编译单词边界正则
    private static final Pattern WORD_BOUNDARY_PATTERN = Pattern.compile("\\b\\w+\\b");

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

        // 执行敏感词检测（带单词边界过滤）
        SensitiveWordUtil.ValidationResult rawResult = SensitiveWordUtil.validate(value);

        // 如果没有敏感词，直接通过
        if (rawResult.isValid()) {
            return true;
        }

        // 🔑 核心：过滤掉不是完整单词的匹配结果
        List<String> filteredBadWords = filterNonWordBoundary(value, rawResult.getBadWords());

        // 过滤后没有敏感词了，通过校验
        if (filteredBadWords.isEmpty()) {
            log.debug("原始检测到敏感词 {}，但经过单词边界过滤后已通过", rawResult.getBadWords());
            return true;
        }

        // 如果有敏感词且配置了替换，则执行替换
        if (annotation.replace()) {
            log.warn("检测到敏感词: {}，将进行替换", filteredBadWords);
            return true; // 校验通过，但需要在 Controller 中执行替换
        }

        // 否则，抛出校验异常
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                annotation.message() + " Irregular word: " + String.join(", ", filteredBadWords)
        ).addConstraintViolation();

        return false;
    }

    /**
     * 过滤掉不是独立单词的敏感词
     * 例如：敏感词库有 "ass"，文本 "glass" 会被过滤掉
     *
     * @param text 原始文本
     * @param badWords 原始检测到的敏感词列表
     * @return 真正作为独立单词出现的敏感词
     */
    private List<String> filterNonWordBoundary(String text, List<String> badWords) {
        if (badWords == null || badWords.isEmpty()) {
            return badWords;
        }

        String lowerText = text.toLowerCase();

        return badWords.stream()
                .filter(word -> isExactWordMatch(lowerText, word.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * 精确单词匹配（检查是否为独立单词）
     *
     * @param text 原始文本（已转小写）
     * @param word 敏感词（已转小写）
     * @return true 如果敏感词作为独立单词出现
     */
    private boolean isExactWordMatch(String text, String word) {
        int index = 0;
        while ((index = text.indexOf(word, index)) != -1) {
            // 检查前一个字符
            boolean leftBoundary = index == 0 || !isWordCharacter(text.charAt(index - 1));
            // 检查后一个字符
            int endIndex = index + word.length();
            boolean rightBoundary = endIndex >= text.length() || !isWordCharacter(text.charAt(endIndex));

            if (leftBoundary && rightBoundary) {
                return true;
            }
            index++;
        }
        return false;
    }

    /**
     * 判断是否为单词字符（字母、数字、下划线、连字符等）
     * 可以根据需要调整
     */
    private boolean isWordCharacter(char c) {
        // 字母、数字、下划线、连字符都算作单词的一部分
        return Character.isLetterOrDigit(c) || c == '_' || c == '-';
    }
}