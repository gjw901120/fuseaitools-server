package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;

public interface IdeogramManager {

    /**
     * Ideogram V3 文生图
     */
    ImageGenerateResponse ideogramV3TextToImage(IdeogramV3TextToImageRequest request, String apiKey);

    /**
     * Ideogram V3 编辑
     */
    ImageGenerateResponse ideogramV3Edit(IdeogramV3EditRequest request, String apiKey);

    /**
     * Ideogram V3 重混
     */
    ImageGenerateResponse ideogramV3Remix(IdeogramV3RemixRequest request, String apiKey);

    /**
     * Ideogram V3 重构
     */
    ImageGenerateResponse ideogramV3Reframe(IdeogramV3ReframeRequest request, String apiKey);

    /**
     * Ideogram 角色生成
     */
    ImageGenerateResponse ideogramCharacter(IdeogramCharacterRequest request, String apiKey);

    /**
     * Ideogram 角色编辑
     */
    ImageGenerateResponse ideogramCharacterEdit(IdeogramCharacterEditRequest request, String apiKey);

    /**
     * Ideogram 角色重混
     */
    ImageGenerateResponse ideogramCharacterRemix(IdeogramCharacterRemixRequest request, String apiKey);
}