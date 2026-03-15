package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.IdeogramFeignClient;
import com.fuse.ai.server.manager.manager.IdeogramManager;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IdeogramManagerImpl implements IdeogramManager {

    @Autowired
    private IdeogramFeignClient ideogramFeignClient;

    @Override
    public ImageGenerateResponse ideogramV3TextToImage(IdeogramV3TextToImageRequest request, String apiKey) {
        return ideogramFeignClient.ideogramV3TextToImage(request, apiKey);
    }

    @Override
    public ImageGenerateResponse ideogramV3Edit(IdeogramV3EditRequest request, String apiKey) {
        return ideogramFeignClient.ideogramV3Edit(request, apiKey);
    }

    @Override
    public ImageGenerateResponse ideogramV3Remix(IdeogramV3RemixRequest request, String apiKey) {
        return ideogramFeignClient.ideogramV3Remix(request, apiKey);
    }

    @Override
    public ImageGenerateResponse ideogramV3Reframe(IdeogramV3ReframeRequest request, String apiKey) {
        return ideogramFeignClient.ideogramV3Reframe(request, apiKey);
    }

    @Override
    public ImageGenerateResponse ideogramCharacter(IdeogramCharacterRequest request, String apiKey) {
        return ideogramFeignClient.ideogramCharacter(request, apiKey);
    }

    @Override
    public ImageGenerateResponse ideogramCharacterEdit(IdeogramCharacterEditRequest request, String apiKey) {
        return ideogramFeignClient.ideogramCharacterEdit(request, apiKey);
    }

    @Override
    public ImageGenerateResponse ideogramCharacterRemix(IdeogramCharacterRemixRequest request, String apiKey) {
        return ideogramFeignClient.ideogramCharacterRemix(request, apiKey);
    }
}