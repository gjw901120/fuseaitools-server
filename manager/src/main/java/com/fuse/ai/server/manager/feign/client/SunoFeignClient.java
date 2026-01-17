// SunoFeignClient.java
package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.SunoMusicResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * Suno音乐生成Feign客户端
 */
@FeignClient(
        name = "suno-music-service",
        url = "${feign.api.suno.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface SunoFeignClient {

    /**
     * 生成音乐
     */
    @PostMapping("/api/v1/suno/generate")
    @Headers("Content-Type: application/json")
    SunoMusicResponse generateMusic(@Valid @RequestBody SunoGenerateRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * 延长音乐
     */
    @PostMapping("/api/v1/suno/generate/extend")
    @Headers("Content-Type: application/json")
    SunoMusicResponse extendMusic(@Valid @RequestBody SunoExtendRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * 上传并翻唱音乐
     */
    @PostMapping("/api/v1/suno/generate/upload-cover")
    @Headers("Content-Type: application/json")
    SunoMusicResponse uploadCover(@Valid @RequestBody SunoUploadCoverRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * 上传并扩展音乐
     */
    @PostMapping("/api/v1/suno/generate/upload-extend")
    @Headers("Content-Type: application/json")
    SunoMusicResponse uploadExtend(@Valid @RequestBody SunoUploadExtendRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * 添加伴奏生成音乐
     */
    @PostMapping("/api/v1/suno/generate/add-instrumental")
    @Headers("Content-Type: application/json")
    SunoMusicResponse addInstrumental(@Valid @RequestBody SunoAddInstrumentalRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * 添加人声生成音乐
     */
    @PostMapping("/api/v1/suno/generate/add-vocals")
    @Headers("Content-Type: application/json")
    SunoMusicResponse addVocals(@Valid @RequestBody SunoAddVocalsRequest request, @RequestParam("apiKey") String apiKey);
}