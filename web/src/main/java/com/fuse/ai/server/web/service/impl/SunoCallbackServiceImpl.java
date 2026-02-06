package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.web.common.utils.S3UploadUtil;
import com.fuse.ai.server.web.model.dto.request.callback.suno.*;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.SunoCallbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SunoCallbackServiceImpl implements SunoCallbackService {

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private S3UploadUtil s3UploadUtil;

    @Override
    public void generateCallback(SunoGenerateRequest request) {
        log.info("处理生成音乐回调: code={}, msg={}", request.getCode(), request.getMsg());

        //幂等性校验
        if (recordsService.isCompleted(request.getData().getTaskId())) return;

        if (!request.isSuccess()) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("生成音乐失败: code={}, msg={}", request.getCode(), request.getMsg());
            return;
        }

        SunoGenerateRequest.GenerateData data = request.getData();
        List<String> outputUrl = new ArrayList<>();
        if (data != null && "complete".equals(data.getCallbackType())  && data.getData() != null) {
            data.getData().forEach(audio -> {
                log.info("生成音乐数据: audio {}", audio);
                // 处理生成音乐的业务逻辑
                outputUrl.add(s3UploadUtil.uploadFileFromUrl(audio.getImageUrl()));
                outputUrl.add(s3UploadUtil.uploadFileFromUrl(audio.getAudioUrl()));
            });

            recordsService.completed(request.getData().getTaskId(), outputUrl, new HashMap<>(), request);
        }
    }

    @Override
    public void extendCallback(SunoExtendRequest request) {
        log.info("处理延长音乐回调: code={}, msg={}", request.getCode(), request.getMsg());

        //幂等性校验
        if (recordsService.isCompleted(request.getData().getTaskId())) return;
        if (!request.isSuccess()) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("延长音乐失败: code={}, msg={}", request.getCode(), request.getMsg());
            return;
        }

        SunoExtendRequest.ExtendData data = request.getData();
        List<String> outputUrl = new ArrayList<>();
        if (data != null && data.getData() != null) {
            data.getData().forEach(audio -> {
                log.info("延长音乐数据: audio {}", audio);
                // 处理延长音乐的业务逻辑
                outputUrl.add(s3UploadUtil.uploadFileFromUrl(audio.getImageUrl()));
                outputUrl.add(s3UploadUtil.uploadFileFromUrl(audio.getAudioUrl()));
            });
            recordsService.completed(request.getData().getTaskId(), outputUrl, new HashMap<>(), request);
        }
    }

    @Override
    public void uploadCoverCallback(SunoUploadCoverRequest request) {
        log.info("处理上传翻唱回调: code={}, msg={}", request.getCode(), request.getMsg());

        //幂等性校验
        if (recordsService.isCompleted(request.getData().getTaskId())) return;

        if (!request.isSuccess()) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("上传翻唱失败: code={}, msg={}", request.getCode(), request.getMsg());
            return;
        }

        SunoUploadCoverRequest.UploadCoverData data = request.getData();
        List<String> outputUrl = new ArrayList<>();
        if (data != null && data.getData() != null) {
            data.getData().forEach(audio -> {
                log.info("上传翻唱数据: audio {}", audio);
                // 处理上传翻唱的业务逻辑
                outputUrl.add(s3UploadUtil.uploadFileFromUrl(audio.getImageUrl()));
                outputUrl.add(s3UploadUtil.uploadFileFromUrl(audio.getAudioUrl()));
                outputUrl.add(s3UploadUtil.uploadFileFromUrl(audio.getSourceAudioUrl()));
            });

            recordsService.completed(request.getData().getTaskId(), outputUrl, new HashMap<>(), request);
        }
    }

    @Override
    public void uploadExtendCallback(SunoUploadExtendRequest request) {
        log.info("处理上传扩展回调: code={}, msg={}", request.getCode(), request.getMsg());

        //幂等性校验
        if (recordsService.isCompleted(request.getData().getTaskId())) return;
        if (!request.isSuccess()) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("上传扩展失败: code={}, msg={}", request.getCode(), request.getMsg());
            return;
        }

        SunoUploadExtendRequest.UploadExtendData data = request.getData();
        List<String> outputUrl = new ArrayList<>();
        if (data != null && data.getData() != null) {
            data.getData().forEach(audio -> {
                log.info("上传扩展数据: audio {}", audio);
                // 处理上传扩展的业务逻辑
                outputUrl.add(s3UploadUtil.uploadFileFromUrl(audio.getImageUrl()));
                outputUrl.add(s3UploadUtil.uploadFileFromUrl(audio.getAudioUrl()));
                outputUrl.add(s3UploadUtil.uploadFileFromUrl(audio.getSourceAudioUrl()));
            });
            recordsService.completed(request.getData().getTaskId(), outputUrl, new HashMap<>(), request);
        }
    }

    @Override
    public void addInstrumentalCallback(SunoAddInstrumentalRequest request) {
        log.info("处理添加伴奏回调: code={}, msg={}", request.getCode(), request.getMsg());

        //幂等性校验
        if (recordsService.isCompleted(request.getData().getTaskId())) return;
        if (!request.isSuccess()) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("添加伴奏失败: code={}, msg={}", request.getCode(), request.getMsg());
            return;
        }

        SunoAddInstrumentalRequest.AddInstrumentalData data = request.getData();
        List<String> outputUrl = new ArrayList<>();
        if (data != null && data.getData() != null) {
            data.getData().forEach(audio -> {
                log.info("添加伴奏数据: audio {}", audio);
                // 处理添加伴奏的业务逻辑
                outputUrl.add(s3UploadUtil.uploadFileFromUrl(audio.getImageUrl()));
                outputUrl.add(s3UploadUtil.uploadFileFromUrl(audio.getAudioUrl()));
            });

            recordsService.completed(request.getData().getTaskId(), outputUrl, new HashMap<>(), request);
        }
    }

    @Override
    public void addVocalsCallback(SunoAddVocalsRequest request) {
        log.info("处理添加人声回调: code={}, msg={}", request.getCode(), request.getMsg());

        //幂等性校验
        if (recordsService.isCompleted(request.getData().getTaskId())) return;
        if (!request.isSuccess()) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("添加人声失败: code={}, msg={}", request.getCode(), request.getMsg());
            return;
        }

        SunoAddVocalsRequest.AddVocalsData data = request.getData();
        List<String> outputUrl = new ArrayList<>();
        if (data != null && data.getData() != null) {
            data.getData().forEach(audio -> {
                log.info("添加人声数据: audio {}", audio);
                // 处理添加人声的业务逻辑
                outputUrl.add(s3UploadUtil.uploadFileFromUrl(audio.getImageUrl()));
                outputUrl.add(s3UploadUtil.uploadFileFromUrl(audio.getAudioUrl()));
            });

            recordsService.completed(request.getData().getTaskId(), outputUrl, new HashMap<>(), request);
        }
    }
}