// SunoFeignClient.java
package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.SunoMusicResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * Suno音乐生成Feign客户端
 */
@FeignClient(
        name = "suno-music-service",
        url = "${feign.client.suno.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface SunoFeignClient {

    /**
     * 生成音乐
     */
    @PostMapping("/api/v1/suno/generate")
    SunoMusicResponse generateMusic(@Valid @RequestBody SunoGenerateRequest request);

    /**
     * 延长音乐
     */
    @PostMapping("/api/v1/suno/generate/extend")
    SunoMusicResponse extendMusic(@Valid @RequestBody SunoExtendRequest request);

    /**
     * 上传并翻唱音乐
     */
    @PostMapping("/api/v1/suno/generate/upload-cover")
    SunoMusicResponse uploadCover(@Valid @RequestBody SunoUploadCoverRequest request);

    /**
     * 上传并扩展音乐
     */
    @PostMapping("/api/v1/suno/generate/upload-extend")
    SunoMusicResponse uploadExtend(@Valid @RequestBody SunoUploadExtendRequest request);

    /**
     * 添加伴奏生成音乐
     */
    @PostMapping("/api/v1/suno/generate/add-instrumental")
    SunoMusicResponse addInstrumental(@Valid @RequestBody SunoAddInstrumentalRequest request);

    /**
     * 添加人声生成音乐
     */
    @PostMapping("/api/v1/suno/generate/add-vocals")
    SunoMusicResponse addVocals(@Valid @RequestBody SunoAddVocalsRequest request);
}