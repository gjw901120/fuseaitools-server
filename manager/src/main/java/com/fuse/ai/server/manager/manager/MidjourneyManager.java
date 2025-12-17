package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.MidjourneyBaseResponse;

public interface MidjourneyManager {

    /**
     * 提交 Imagine 任务 - 文本生成图片
     */
    MidjourneyBaseResponse<String> submitImagine(MidjourneyImagineRequest request);

    /**
     * 提交 Blend 任务 - 多图混合
     */
    MidjourneyBaseResponse<String> submitBlend(MidjourneyBlendRequest request);

    /**
     * 提交 Describe 任务 - 图片描述
     */
    MidjourneyBaseResponse<String> submitDescribe(MidjourneyDescribeRequest request);

    /**
     * 提交 Modal 任务 - 模态操作
     */
    MidjourneyBaseResponse<String> submitModal(MidjourneyModalRequest request);

    /**
     * 提交 Swap Face 任务 - 人脸替换
     */
    MidjourneyBaseResponse<String> submitSwapFace(MidjourneySwapFaceRequest request);

    /**
     * 执行 Action 动作 - 图片操作
     */
    MidjourneyBaseResponse<String> submitAction(MidjourneyActionRequest request);

}
