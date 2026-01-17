package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.MidjourneyFeignClient;
import com.fuse.ai.server.manager.manager.MidjourneyManager;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.MidjourneyBaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MidjourneyManagerImpl implements MidjourneyManager {

    @Autowired
    private MidjourneyFeignClient midjourneyFeignClient;


    /**
     * 提交 Imagine 任务 - 文本生成图片
     */
    @Override
    public MidjourneyBaseResponse<String> submitImagine(MidjourneyImagineRequest request, String apiKey) {
        return midjourneyFeignClient.submitImagine(request, apiKey);
    }

    /**
     * 提交 Blend 任务 - 多图混合
     */
    @Override
    public MidjourneyBaseResponse<String> submitBlend(MidjourneyBlendRequest request, String apiKey) {
        return midjourneyFeignClient.submitBlend(request, apiKey);
    }

    /**
     * 提交 Describe 任务 - 图片描述
     */
    @Override
    public MidjourneyBaseResponse<String> submitDescribe(MidjourneyDescribeRequest request, String apiKey) {
        return midjourneyFeignClient.submitDescribe(request, apiKey);
    }

    /**
     * 提交 Modal 任务 - 模态操作
     */
    @Override
    public MidjourneyBaseResponse<String> submitModal(MidjourneyModalRequest request, String apiKey) {
        return midjourneyFeignClient.submitModal(request, apiKey);
    }

    /**
     * 提交 Swap Face 任务 - 人脸替换
     */
    @Override
    public MidjourneyBaseResponse<String> submitSwapFace(MidjourneySwapFaceRequest request, String apiKey) {
        return midjourneyFeignClient.submitSwapFace(request, apiKey);
    }

    /**
     * 执行 Action 动作 - 图片操作
     */
    @Override
    public MidjourneyBaseResponse<String> submitAction(MidjourneyActionRequest request, String apiKey) {
        return midjourneyFeignClient.submitAction(request, apiKey);
    }

}
