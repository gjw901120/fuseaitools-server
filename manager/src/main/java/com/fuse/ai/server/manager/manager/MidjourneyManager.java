package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.image.MidjourneyImagineRequest;
import com.fuse.ai.server.manager.model.request.image.MidjourneyUpscaleRequest;
import com.fuse.ai.server.manager.model.request.image.MidjourneyVaryRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;

public interface MidjourneyManager {

    /**
     * 提交 Imagine 任务 - 文本生成图片
     */
    ImageGenerateResponse submitImagine(MidjourneyImagineRequest request, String apiKey);

    ImageGenerateResponse submitUpscale(MidjourneyUpscaleRequest request, String apiKey);

    ImageGenerateResponse submitVary(MidjourneyVaryRequest request, String apiKey);

//    /**
//     * 提交 Blend 任务 - 多图混合
//     */
//    MidjourneyBaseResponse<String> submitBlend(MidjourneyBlendRequest request, String apiKey);
//
//    /**
//     * 提交 Describe 任务 - 图片描述
//     */
//    MidjourneyBaseResponse<String> submitDescribe(MidjourneyDescribeRequest request, String apiKey);
//
//    /**
//     * 提交 Modal 任务 - 模态操作
//     */
//    MidjourneyBaseResponse<String> submitModal(MidjourneyModalRequest request, String apiKey);
//
//    /**
//     * 提交 Swap Face 任务 - 人脸替换
//     */
//    MidjourneyBaseResponse<String> submitSwapFace(MidjourneySwapFaceRequest request, String apiKey);
//
//    /**
//     * 执行 Action 动作 - 图片操作
//     */
//    MidjourneyBaseResponse<String> submitAction(MidjourneyActionRequest request, String apiKey);

}
