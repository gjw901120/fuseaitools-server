package com.fuse.ai.server.web.model.dto.request.callback.elevenlabs;

import lombok.Data;

/**
 * ElevenLabs回调数据
 */
@Data
public class ElevenLabsCallbackData {

    /**
     * 任务完成时间
     */
    private Long completeTime;

    /**
     * 消耗积分
     */
    private Integer consumeCredits;

    /**
     * 耗时（秒）
     */
    private Integer costTime;

    /**
     * 任务创建时间
     */
    private Long createTime;

    /**
     * 失败代码
     */
    private String failCode;

    /**
     * 失败信息
     */
    private String failMsg;

    /**
     * 模型名称
     */
    private String model;

    /**
     * 任务参数（JSON字符串）
     */
    private String param;

    /**
     * 剩余积分
     */
    private Integer remainedCredits;

    /**
     * 结果JSON
     */
    private String resultJson;

    /**
     * 任务状态
     */
    private String state;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 更新时间
     */
    private Long updateTime;
}