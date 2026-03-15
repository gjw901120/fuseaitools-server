package com.fuse.ai.server.web.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuse.ai.server.web.common.utils.S3UploadUtil;
import com.fuse.ai.server.web.model.dto.request.callback.image.*;
import com.fuse.ai.server.web.service.ImageCallbackService;
import com.fuse.ai.server.web.service.RecordsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    private S3UploadUtil s3UploadUtil;

    @Override
    public void processGpt4oCallback(ImageGpt4oCallbackRequest request) {

        String taskId = request.getData().getTaskId();
        Integer code = request.getCode();

        //幂等性校验
        if (recordsService.isCompleted(taskId)) return;

        log.info("Processing GPT-4O image callback: taskId={}, code={}", taskId, code);

        if (code == 200) {
            // 成功处理
            String[] resultUrls = request.getData().getInfo().getResultUrls();
            log.info("GPT-4O image generation completed: taskId={}, resultUrls={}", taskId, resultUrls);

            List<String> outputUrl = new ArrayList<>();
            for (String url : resultUrls) {
                outputUrl.add(s3UploadUtil.uploadFileFromUrl(url));
            }
            recordsService.completed(taskId, outputUrl, new HashMap<>(), request);

        } else {

            recordsService.failed(taskId, request);
            log.info("Failed to process GPT-4O callback: taskId={}, info={}", taskId, request);

        }

    }

    @Override
    public void processFluxKontextCallback(ImageFluxKontextCallbackRequest request) {

        String taskId = request.getData().getTaskId();
        Integer code = request.getCode();

        //幂等性校验
        if (recordsService.isCompleted(taskId)) return;

        log.info("Processing Flux Kontext image callback: taskId={}, code={}", taskId, code);

        if (code == 200) {
            // 成功处理
            String originImageUrl = request.getData().getInfo().getOriginImageUrl();
            String resultImageUrl = request.getData().getInfo().getResultImageUrl();

            log.info("Flux Kontext image generation completed: taskId={}, originUrl={}, resultUrl={}", taskId, originImageUrl, resultImageUrl);

            List<String> outputUrl = new ArrayList<>();
            outputUrl.add(s3UploadUtil.uploadFileFromUrl(resultImageUrl));
            recordsService.completed(taskId, outputUrl, new HashMap<>(), request);

        } else {

            recordsService.failed(taskId, request);
            log.info("Failed to process Flux Kontext callback: taskId={}, info={}", taskId, request);

        }

    }

    @Override
    public void processNanoBananaCallback(ImageNanoBananaCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            String state = request.getData().getState();
            String resultJson = request.getData().getResultJson();

            //幂等性校验
            if (recordsService.isCompleted(request.getData().getTaskId())) return;

            log.info("Processing Nano Banana image callback: taskId={}, state={}", taskId, state);

            if ("success".equals(state)) {
                // 解析结果
                ImageNanoBananaCallbackRequest.NanoBananaResult result =
                        objectMapper.readValue(resultJson, ImageNanoBananaCallbackRequest.NanoBananaResult.class);

                log.info("Nano Banana image generation completed: taskId={}, resultUrls={}", taskId, result.getResultUrls());

                List<String> outputUrl = new ArrayList<>();
                for (String url : result.getResultUrls()) {
                    outputUrl.add(s3UploadUtil.uploadFileFromUrl(url));
                }
                recordsService.completed(request.getData().getTaskId(), outputUrl, new HashMap<>(), request);

            } else if ("fail".equals(state)) {
                log.info("Nano Banana image generation failed: taskId={}, info={}", taskId, request);

                recordsService.failed(request.getData().getTaskId(), request);
            }

        } catch (Exception e) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("Failed to process Nano Banana callback: taskId={}, error={}", request.getData().getTaskId(), e);
        }
    }

    @Override
    public void processMjGenerateCallback(ImageMjGenerateCallbackRequest request) {
        String taskId = request.getData().getTaskId();
        Integer code = request.getCode();

        //幂等性校验
        if (recordsService.isCompleted(request.getData().getTaskId())) return;

        log.info("Processing Mj Generate image callback: taskId={}, code={}", taskId, code);
        if (code == 200) {
            // 成功处理;
            log.info("Mj Generate image generation completed: taskId={}, resultUrl={}", taskId, request.getData().getResultUrls());

            List<String> outputUrl = new ArrayList<>();
            for (String url : request.getData().getResultUrls())
                outputUrl.add(s3UploadUtil.uploadFileFromUrl(url));
            recordsService.completed(request.getData().getTaskId(), outputUrl, new HashMap<>(), request);

        } else {
            recordsService.failed(request.getData().getTaskId(), request);
            log.info("Failed to process Mj Generate callback: taskId={}, info={}", request.getData().getTaskId(), request);
        }
    }

    @Override
    public void processQwenCallback(ImageQwenCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            String state = request.getData().getState();
            String resultJson = request.getData().getResultJson();

            //幂等性校验
            if (recordsService.isCompleted(request.getData().getTaskId())) return;

            log.info("Processing Qwen image callback: taskId={}, state={}", taskId, state);

            if ("success".equals(state)) {
                // 解析结果
                ImageQwenCallbackRequest.QwenResult result =
                        objectMapper.readValue(resultJson, ImageQwenCallbackRequest.QwenResult.class);

                log.info("Qwen image generation completed: taskId={}, resultUrls={}", taskId, result.getResultUrls());

                List<String> outputUrl = new ArrayList<>();
                for (String url : result.getResultUrls()) {
                    outputUrl.add(s3UploadUtil.uploadFileFromUrl(url));
                }
                recordsService.completed(request.getData().getTaskId(), outputUrl, new HashMap<>(), request);

            } else if ("fail".equals(state)) {
                log.info("Qwen image generation failed: taskId={}, info={}", taskId, request);

                recordsService.failed(request.getData().getTaskId(), request);
            }

        } catch (Exception e) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("Failed to process Qwen callback: taskId={}, error={}", request.getData().getTaskId(), e);
        }
    }

    @Override
    public void processSeedreamCallback(ImageSeedreamCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            String state = request.getData().getState();
            String resultJson = request.getData().getResultJson();

            //幂等性校验
            if (recordsService.isCompleted(request.getData().getTaskId())) return;

            log.info("Processing Seedream image callback: taskId={}, state={}", taskId, state);

            if ("success".equals(state)) {
                // 解析结果
                ImageSeedreamCallbackRequest.SeedreamResult result =
                        objectMapper.readValue(resultJson, ImageSeedreamCallbackRequest.SeedreamResult.class);

                log.info("Seedream image generation completed: taskId={}, resultUrls={}", taskId, result.getResultUrls());

                List<String> outputUrl = new ArrayList<>();
                for (String url : result.getResultUrls()) {
                    outputUrl.add(s3UploadUtil.uploadFileFromUrl(url));
                }
                recordsService.completed(request.getData().getTaskId(), outputUrl, new HashMap<>(), request);

            } else if ("fail".equals(state)) {
                log.info("Seedream image generation failed: taskId={}, info={}", taskId, request);

                recordsService.failed(request.getData().getTaskId(), request);
            }

        } catch (Exception e) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("Failed to process Qwen callback: taskId={}, error={}", request.getData().getTaskId(), e);
        }
    }

    @Override
    public void processGptImageCallback(ImageGptImageCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            String state = request.getData().getState();
            String resultJson = request.getData().getResultJson();

            //幂等性校验
            if (recordsService.isCompleted(request.getData().getTaskId())) return;

            log.info("Processing Gpt Image image callback: taskId={}, state={}", taskId, state);

            if ("success".equals(state)) {
                // 解析结果
                ImageGptImageCallbackRequest.GptImageResult result =
                        objectMapper.readValue(resultJson, ImageGptImageCallbackRequest.GptImageResult.class);

                log.info("Gpt Image generation completed: taskId={}, resultUrls={}", taskId, result.getResultUrls());

                List<String> outputUrl = new ArrayList<>();
                for (String url : result.getResultUrls()) {
                    outputUrl.add(s3UploadUtil.uploadFileFromUrl(url));
                }
                recordsService.completed(request.getData().getTaskId(), outputUrl, new HashMap<>(), request);

            } else if ("fail".equals(state)) {
                log.info("Gpt Image generation failed: taskId={}, info={}", taskId, request);

                recordsService.failed(request.getData().getTaskId(), request);
            }

        } catch (Exception e) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("Failed to process Gpt Image callback: taskId={}, error={}", request.getData().getTaskId(), e);
        }
    }

    @Override
    public void processIdeogramCallback(ImageIdeogramCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            String state = request.getData().getState();
            String resultJson = request.getData().getResultJson();

            //幂等性校验
            if (recordsService.isCompleted(request.getData().getTaskId())) return;

            log.info("Processing Ideogram image callback: taskId={}, state={}", taskId, state);

            if ("success".equals(state)) {
                // 解析结果
                ImageIdeogramCallbackRequest.IdeogramResult result =
                        objectMapper.readValue(resultJson, ImageIdeogramCallbackRequest.IdeogramResult.class);

                log.info("Ideogram image generation completed: taskId={}, resultUrls={}", taskId, result.getResultUrls());

                List<String> outputUrl = new ArrayList<>();
                for (String url : result.getResultUrls()) {
                    outputUrl.add(s3UploadUtil.uploadFileFromUrl(url));
                }
                recordsService.completed(request.getData().getTaskId(), outputUrl, new HashMap<>(), request);

            } else if ("fail".equals(state)) {
                log.info("Ideogram image generation failed: taskId={}, info={}", taskId, request);

                recordsService.failed(request.getData().getTaskId(), request);
            }

        } catch (Exception e) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("Failed to process Ideogram callback: taskId={}, error={}", request.getData().getTaskId(), e);
        }
    }

}