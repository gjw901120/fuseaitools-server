package com.fuse.ai.server.web.common.utils;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 敏感词过滤工具类
 * 基于 houbb/sensitive-word + LDNOOBW 英文词库
 */
@Slf4j
@Component
public class SensitiveWordUtil {

    private static SensitiveWordBs wordBs;

    private static final String WORD_LIST_PATH = "ldnoobw-en.txt";

    /**
     * 初始化敏感词过滤器
     */
    @PostConstruct
    public void init() {
        log.info("正在初始化敏感词过滤器...");
        wordBs = buildFilter();
        log.info("敏感词过滤器初始化完成");
    }

    /**
     * 构建过滤器配置
     */
    private SensitiveWordBs buildFilter() {
        return SensitiveWordBs.newInstance()
                // 加载系统默认中文词库 + LDNOOBW 英文词库
                .wordDeny(WordDenys.chains(
                        WordDenys.system(),
                        new LdnoobwWordDeny()
                ))
                // 忽略大小写
                .ignoreCase(true)
                // 忽略全半角
                .ignoreWidth(true)
                .init();
    }

    /**
     * 检测文本是否包含敏感词
     */
    public static boolean contains(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        return wordBs.contains(text);
    }

    /**
     * 查找所有敏感词
     */
    public static List<String> findAll(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return wordBs.findAll(text);
    }

    /**
     * 替换敏感词
     */
    public static String replace(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return wordBs.replace(text);
    }

    /**
     * 替换敏感词（自定义替换字符）
     */
    public static String replaceWith(String text, char replacement) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return wordBs.replace(text, replacement);
    }

    /**
     * 校验文本是否安全
     * @param text 待校验文本
     * @return 校验结果，包含是否安全、违规词列表
     */
    public static ValidationResult validate(String text) {
        ValidationResult result = new ValidationResult();

        if (text == null || text.isEmpty()) {
            result.setValid(true);
            return result;
        }

        List<String> badWords = findAll(text);
        if (badWords.isEmpty()) {
            result.setValid(true);
        } else {
            result.setValid(false);
            result.setBadWords(badWords);
        }

        return result;
    }

    /**
     * 校验结果封装类
     */
    public static class ValidationResult {
        private boolean valid;
        private List<String> badWords;

        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }

        public List<String> getBadWords() { return badWords; }
        public void setBadWords(List<String> badWords) { this.badWords = badWords; }

        public String getErrorMessage() {
            if (valid) return null;
            return String.format("The input content contains prohibited words: %s,Please make the correction and try again",
                    String.join(", ", badWords));
        }
    }

    /**
     * LDNOOBW 英文词库加载器
     */
    private static class LdnoobwWordDeny implements com.github.houbb.sensitive.word.api.IWordDeny {

        @Override
        public List<String> deny() {
            List<String> words = new ArrayList<>();

            try {
                ClassPathResource resource = new ClassPathResource(WORD_LIST_PATH);
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim().toLowerCase();
                        if (!line.isEmpty() && !line.startsWith("#")) {
                            words.add(line);
                        }
                    }
                }
                log.info("加载 LDNOOBW 英文词库成功，共 {} 个敏感词", words.size());
            } catch (Exception e) {
                log.warn("加载 LDNOOBW 英文词库失败: {}，仅使用默认中文词库", e.getMessage());
            }

            return words;
        }
    }
}