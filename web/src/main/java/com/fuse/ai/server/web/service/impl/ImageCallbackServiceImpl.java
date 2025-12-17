package com.fuse.ai.server.web.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuse.ai.server.web.model.dto.request.callback.image.*;
import com.fuse.ai.server.web.service.ImageCallbackService;
import com.fuse.ai.server.web.service.RecordsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 图像回调服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImageCallbackServiceImpl implements ImageCallbackService {

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private RecordsService recordsService;

    @Override
    public void processGpt4oCallback(ImageGpt4oCallbackRequest request) {

        String taskId = request.getData().getTaskId();
        Integer code = request.getCode();

        log.info("Processing GPT-4O image callback: taskId={}, code={}", taskId, code);

        if (code == 200) {
            // 成功处理
            String[] resultUrls = request.getData().getInfo().getResultUrls();
            log.info("GPT-4O image generation completed: taskId={}, resultUrls={}", taskId, resultUrls);

            List<String> outputUrl = new ArrayList<>();
            recordsService.completed(request.getData().getTaskId(), outputUrl, request);

        } else {

            recordsService.failed(request.getData().getTaskId(), request);
            log.info("Failed to process GPT-4O callback: taskId={}, info={}", request.getData().getTaskId(), request);

        }

    }

    @Override
    public void processFluxKontextCallback(ImageFluxKontextCallbackRequest request) {

        String taskId = request.getData().getTaskId();
        Integer code = request.getCode();

        log.info("Processing Flux Kontext image callback: taskId={}, code={}", taskId, code);

        if (code == 200) {
            // 成功处理
            String originImageUrl = request.getData().getInfo().getOriginImageUrl();
            String resultImageUrl = request.getData().getInfo().getResultImageUrl();

            log.info("Flux Kontext image generation completed: taskId={}, originUrl={}, resultUrl={}", taskId, originImageUrl, resultImageUrl);

            List<String> outputUrl = new ArrayList<>();
            recordsService.completed(request.getData().getTaskId(), outputUrl, request);

        } else {

            recordsService.failed(request.getData().getTaskId(), request);
            log.info("Failed to process Flux Kontext callback: taskId={}, info={}", request.getData().getTaskId(), request);

        }

    }

    @Override
    public void processNanoBananaCallback(ImageNanoBananaCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            String state = request.getData().getState();
            String resultJson = request.getData().getResultJson();

            log.info("Processing Nano Banana image callback: taskId={}, state={}", taskId, state);

            if ("success".equals(state)) {
                // 解析结果
                ImageNanoBananaCallbackRequest.NanoBananaResult result =
                        objectMapper.readValue(resultJson, ImageNanoBananaCallbackRequest.NanoBananaResult.class);

                log.info("Nano Banana image generation completed: taskId={}, resultUrls={}", taskId, result.getResultUrls());

                List<String> outputUrl = new ArrayList<>();
                recordsService.completed(request.getData().getTaskId(), outputUrl, request);

            } else if ("fail".equals(state)) {
                log.info("Nano Banana image generation failed: taskId={}, info={}", taskId, request);

                recordsService.failed(request.getData().getTaskId(), request);
            }

        } catch (Exception e) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("Failed to process Nano Banana callback: taskId={}, error={}", request.getData().getTaskId(), e);
        }
    }

}