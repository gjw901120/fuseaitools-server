package com.fuse.ai.server.manager.model.response;

import com.fuse.ai.server.manager.enums.MidjourneyResponseCodeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Midjourney 基础响应
 */
@Data
public class MidjourneyBaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private MidjourneyResponseCodeEnum code;

    /**
     * 响应描述信息
     */
    private String description;

    /**
     * 扩展属性
     */
    private Map<String, Object> properties;

    /**
     * 返回结果
     */
    private T result;

    // 构造方法
    public MidjourneyBaseResponse() {}

    public MidjourneyBaseResponse(MidjourneyResponseCodeEnum code, String description) {
        this.code = code;
        this.description = description;
    }

    public static <T> MidjourneyBaseResponse<T> success(T result) {
        MidjourneyBaseResponse<T> response = new MidjourneyBaseResponse<>();
        response.setCode(MidjourneyResponseCodeEnum.SUCCESS);
        response.setDescription("提交成功");
        response.setResult(result);
        return response;
    }

    public static <T> MidjourneyBaseResponse<T> error(MidjourneyResponseCodeEnum code, String description) {
        return new MidjourneyBaseResponse<>(code, description);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return MidjourneyResponseCodeEnum.SUCCESS.equals(code);
    }
}