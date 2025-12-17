package com.fuse.ai.server.manager.model.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Midjourney 任务详情
 */
@Data
public class MidjourneyTaskResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务类型
     */
    private Action action;

    /**
     * 可执行的操作按钮
     */
    private List<Button> buttons;

    /**
     * 任务描述信息
     */
    private String description;

    /**
     * 任务失败原因
     */
    private String failReason;

    /**
     * 任务完成时间戳
     */
    private Long finishTime;

    /**
     * 任务唯一标识符
     */
    private String id;

    /**
     * 生成图片的URL
     */
    private String imageUrl;

    /**
     * 任务进度信息
     */
    private String progress;

    /**
     * 原始提示词
     */
    private String prompt;

    /**
     * 英文提示词
     */
    private String promptEn;

    /**
     * 扩展属性
     */
    private Map<String, Object> properties;

    /**
     * 任务状态
     */
    private Status status;

    /**
     * 任务提交时间戳
     */
    private Long submitTime;

    /**
     * 任务开始执行时间戳
     */
    private Long startTime;

    /**
     * 任务动作枚举（内部枚举）
     */
    public enum Action {
        IMAGINE("IMAGINE", "创建图片"),
        UPSCALE("UPSCALE", "放大图片"),
        VARIATION("VARIATION", "变体生成"),
        ZOOM("ZOOM", "缩放图片"),
        PAN("PAN", "平移图片"),
        DESCRIBE("DESCRIBE", "图片描述"),
        BLEND("BLEND", "图片混合"),
        SHORTEN("SHORTEN", "缩短提示词"),
        SWAP_FACE("SWAP_FACE", "人脸替换");

        private final String code;
        private final String description;

        Action(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static Action getByCode(String code) {
            for (Action value : values()) {
                if (value.getCode().equals(code)) {
                    return value;
                }
            }
            return null;
        }
    }

    /**
     * 任务状态枚举（内部枚举）
     */
    public enum Status {
        NOT_START("NOT_START", "未开始"),
        SUBMITTED("SUBMITTED", "已提交"),
        MODAL("MODAL", "模态操作中"),
        IN_PROGRESS("IN_PROGRESS", "进行中"),
        FAILURE("FAILURE", "失败"),
        SUCCESS("SUCCESS", "成功"),
        CANCEL("CANCEL", "已取消");

        private final String code;
        private final String description;

        Status(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static Status getByCode(String code) {
            for (Status value : values()) {
                if (value.getCode().equals(code)) {
                    return value;
                }
            }
            return null;
        }
    }

    /**
     * 按钮样式枚举（内部枚举）
     */
    public enum ButtonStyle {
        PRIMARY(2, "主要按钮"),
        SUCCESS(3, "成功按钮"),
        SECONDARY(1, "次要按钮"),
        DANGER(4, "危险按钮");

        private final Integer code;
        private final String description;

        ButtonStyle(Integer code, String description) {
            this.code = code;
            this.description = description;
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static ButtonStyle getByCode(Integer code) {
            for (ButtonStyle value : values()) {
                if (value.getCode().equals(code)) {
                    return value;
                }
            }
            return null;
        }
    }

    /**
     * 按钮类型枚举（内部枚举）
     */
    public enum ButtonType {
        UPSAMPLE(1, "放大操作"),
        VARIATION(2, "变体生成"),
        REROLL(3, "重新生成"),
        ZOOM(4, "缩放操作"),
        PAN(5, "平移操作"),
        CUSTOM(99, "自定义操作");

        private final Integer code;
        private final String description;

        ButtonType(Integer code, String description) {
            this.code = code;
            this.description = description;
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static ButtonType getByCode(Integer code) {
            for (ButtonType value : values()) {
                if (value.getCode().equals(code)) {
                    return value;
                }
            }
            return null;
        }
    }

    /**
     * 操作按钮（内部类）
     */
    @Data
    public static class Button implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 动作标识
         */
        private String customId;

        /**
         * 按钮图标
         */
        private String emoji;

        /**
         * 按钮文本
         */
        private String label;

        /**
         * 样式(2=Primary, 3=Green)
         */
        private Integer style;

        /**
         * 系统内部使用的类型
         */
        private Integer type;

        /**
         * 获取按钮样式枚举
         */
        public ButtonStyle getButtonStyle() {
            return ButtonStyle.getByCode(style);
        }

        /**
         * 获取按钮类型枚举
         */
        public ButtonType getButtonType() {
            return ButtonType.getByCode(type);
        }

        /**
         * 判断是否为放大按钮
         */
        public boolean isUpsampleButton() {
            return customId != null && customId.contains("upsample");
        }

        /**
         * 判断是否为变体按钮
         */
        public boolean isVariationButton() {
            return customId != null && customId.contains("variation");
        }

        /**
         * 判断是否为缩放按钮
         */
        public boolean isZoomButton() {
            return customId != null && customId.contains("zoom");
        }

        /**
         * 判断是否为平移按钮
         */
        public boolean isPanButton() {
            return customId != null && customId.contains("pan");
        }

        /**
         * 获取放大索引（U1-U4）
         */
        public Integer getUpsampleIndex() {
            if (isUpsampleButton() && customId != null) {
                String[] parts = customId.split("::");
                if (parts.length >= 4) {
                    try {
                        return Integer.parseInt(parts[3]);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            }
            return null;
        }

        /**
         * 获取变体索引（V1-V4）
         */
        public Integer getVariationIndex() {
            if (isVariationButton() && customId != null) {
                String[] parts = customId.split("::");
                if (parts.length >= 4) {
                    try {
                        return Integer.parseInt(parts[3]);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            }
            return null;
        }
    }

    // ========== 业务方法 ==========

    /**
     * 判断任务是否完成
     */
    public boolean isCompleted() {
        return Status.SUCCESS.equals(status) ||
                Status.FAILURE.equals(status) ||
                Status.CANCEL.equals(status);
    }

    /**
     * 判断任务是否成功
     */
    public boolean isSuccess() {
        return Status.SUCCESS.equals(status);
    }

    /**
     * 判断任务是否失败
     */
    public boolean isFailed() {
        return Status.FAILURE.equals(status);
    }

    /**
     * 判断任务是否在进行中
     */
    public boolean isInProgress() {
        return Status.IN_PROGRESS.equals(status) ||
                Status.SUBMITTED.equals(status) ||
                Status.MODAL.equals(status);
    }

    /**
     * 判断任务是否等待中
     */
    public boolean isPending() {
        return Status.NOT_START.equals(status) ||
                Status.SUBMITTED.equals(status);
    }

    /**
     * 判断任务是否需要模态操作
     */
    public boolean isModalRequired() {
        return Status.MODAL.equals(status);
    }

    /**
     * 判断是否有可用的操作按钮
     */
    public boolean hasButtons() {
        return buttons != null && !buttons.isEmpty();
    }

    /**
     * 获取放大按钮
     */
    public List<Button> getUpsampleButtons() {
        if (!hasButtons()) {
            return java.util.Collections.emptyList();
        }
        return buttons.stream()
                .filter(Button::isUpsampleButton)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取变体按钮
     */
    public List<Button> getVariationButtons() {
        if (!hasButtons()) {
            return java.util.Collections.emptyList();
        }
        return buttons.stream()
                .filter(Button::isVariationButton)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取缩放按钮
     */
    public List<Button> getZoomButtons() {
        if (!hasButtons()) {
            return java.util.Collections.emptyList();
        }
        return buttons.stream()
                .filter(Button::isZoomButton)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取平移按钮
     */
    public List<Button> getPanButtons() {
        if (!hasButtons()) {
            return java.util.Collections.emptyList();
        }
        return buttons.stream()
                .filter(Button::isPanButton)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取指定索引的放大按钮
     */
    public Button getUpsampleButton(int index) {
        return getUpsampleButtons().stream()
                .filter(button -> index == button.getUpsampleIndex())
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取指定索引的变体按钮
     */
    public Button getVariationButton(int index) {
        return getVariationButtons().stream()
                .filter(button -> index == button.getVariationIndex())
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取任务耗时（毫秒）
     */
    public Long getDuration() {
        if (startTime == null) {
            return null;
        }
        Long endTime = finishTime != null ? finishTime : System.currentTimeMillis();
        return endTime - startTime;
    }

    /**
     * 获取格式化后的任务耗时
     */
    public String getFormattedDuration() {
        Long duration = getDuration();
        if (duration == null) {
            return "N/A";
        }

        long seconds = duration / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes % 60, seconds % 60);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds % 60);
        } else {
            return String.format("%ds", seconds);
        }
    }

    /**
     * 获取任务进度百分比
     */
    public Integer getProgressPercentage() {
        if (progress == null || progress.isEmpty()) {
            return null;
        }

        try {
            // 解析类似 "25%" 的进度字符串
            if (progress.contains("%")) {
                return Integer.parseInt(progress.replace("%", "").trim());
            }
        } catch (NumberFormatException e) {
            // 忽略解析错误
        }

        return null;
    }

    /**
     * 判断是否包含图片URL
     */
    public boolean hasImageUrl() {
        return imageUrl != null && !imageUrl.trim().isEmpty();
    }

    /**
     * 获取有效的图片URL
     */
    public String getEffectiveImageUrl() {
        if (hasImageUrl()) {
            return imageUrl;
        }

        // 从properties中获取图片URL
        if (properties != null) {
            Object imageUrlObj = properties.get("imageUrl");
            if (imageUrlObj instanceof String) {
                String url = (String) imageUrlObj;
                if (!url.trim().isEmpty()) {
                    return url;
                }
            }
        }

        return null;
    }

    /**
     * 获取任务创建时间（如果submitTime存在）
     */
    public java.util.Date getSubmitDate() {
        if (submitTime != null && submitTime > 0) {
            return new java.util.Date(submitTime);
        }
        return null;
    }

    /**
     * 获取任务完成时间（如果finishTime存在）
     */
    public java.util.Date getFinishDate() {
        if (finishTime != null && finishTime > 0) {
            return new java.util.Date(finishTime);
        }
        return null;
    }

    /**
     * 获取任务开始时间（如果startTime存在）
     */
    public java.util.Date getStartDate() {
        if (startTime != null && startTime > 0) {
            return new java.util.Date(startTime);
        }
        return null;
    }

    /**
     * 构建简洁的任务信息
     */
    public String toSimpleString() {
        return String.format("MidjourneyTaskResponse{id='%s', action=%s, status=%s, progress='%s'}",
                id, action != null ? action.getCode() : "null",
                status != null ? status.getCode() : "null",
                progress != null ? progress : "N/A");
    }

    /**
     * 构建详细的任务信息
     */
    public String toDetailedString() {
        return String.format(
                "MidjourneyTaskResponse{\n" +
                        "  id='%s',\n" +
                        "  action=%s,\n" +
                        "  status=%s,\n" +
                        "  prompt='%s',\n" +
                        "  promptEn='%s',\n" +
                        "  progress='%s',\n" +
                        "  imageUrl=%s,\n" +
                        "  buttons=%d,\n" +
                        "  submitTime=%s,\n" +
                        "  startTime=%s,\n" +
                        "  finishTime=%s,\n" +
                        "  duration=%s\n" +
                        "}",
                id,
                action != null ? action.getCode() : "null",
                status != null ? status.getCode() : "null",
                prompt != null ? (prompt.length() > 50 ? prompt.substring(0, 50) + "..." : prompt) : "null",
                promptEn != null ? (promptEn.length() > 50 ? promptEn.substring(0, 50) + "..." : promptEn) : "null",
                progress != null ? progress : "N/A",
                hasImageUrl() ? "Present" : "None",
                buttons != null ? buttons.size() : 0,
                getSubmitDate(),
                getStartDate(),
                getFinishDate(),
                getFormattedDuration()
        );
    }

    /**
     * 创建构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 构建器类
     */
    public static class Builder {
        private final MidjourneyTaskResponse task;

        private Builder() {
            this.task = new MidjourneyTaskResponse();
        }

        public Builder id(String id) {
            task.id = id;
            return this;
        }

        public Builder action(Action action) {
            task.action = action;
            return this;
        }

        public Builder status(Status status) {
            task.status = status;
            return this;
        }

        public Builder prompt(String prompt) {
            task.prompt = prompt;
            return this;
        }

        public Builder promptEn(String promptEn) {
            task.promptEn = promptEn;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            task.imageUrl = imageUrl;
            return this;
        }

        public Builder progress(String progress) {
            task.progress = progress;
            return this;
        }

        public Builder buttons(List<Button> buttons) {
            task.buttons = buttons;
            return this;
        }

        public Builder submitTime(Long submitTime) {
            task.submitTime = submitTime;
            return this;
        }

        public Builder startTime(Long startTime) {
            task.startTime = startTime;
            return this;
        }

        public Builder finishTime(Long finishTime) {
            task.finishTime = finishTime;
            return this;
        }

        public Builder description(String description) {
            task.description = description;
            return this;
        }

        public Builder failReason(String failReason) {
            task.failReason = failReason;
            return this;
        }

        public Builder properties(Map<String, Object> properties) {
            task.properties = properties;
            return this;
        }

        public MidjourneyTaskResponse build() {
            return task;
        }
    }
}