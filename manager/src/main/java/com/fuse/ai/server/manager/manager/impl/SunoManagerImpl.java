package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.SunoFeignClient;
import com.fuse.ai.server.manager.manager.SunoManger;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.SunoMusicResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SunoManagerImpl implements SunoManger {

    @Autowired
    private SunoFeignClient sunoFeignClient;

    @Override
    public SunoMusicResponse generateMusic(SunoGenerateRequest request, String apiKey){
        return sunoFeignClient.generateMusic(request, apiKey);
    }

    /**
     * 延长音乐
     */
    @Override
    public SunoMusicResponse extendMusic(SunoExtendRequest request, String apiKey) {
        return sunoFeignClient.extendMusic(request,apiKey);
    }

    /**
     * 上传并翻唱音乐
     */
    @Override
    public SunoMusicResponse uploadCover(SunoUploadCoverRequest request, String apiKey){
        return sunoFeignClient.uploadCover(request,apiKey);
    }

    /**
     * 上传并扩展音乐
     */
    @Override
    public SunoMusicResponse uploadExtend(SunoUploadExtendRequest request, String apiKey){
        return sunoFeignClient.uploadExtend(request,apiKey);
    }

    /**
     * 添加伴奏生成音乐
     */
    @Override
    public SunoMusicResponse addInstrumental(SunoAddInstrumentalRequest request, String apiKey){
        return sunoFeignClient.addInstrumental(request,apiKey);
    }

    /**
     * 添加人声生成音乐
     */
    @Override
    public SunoMusicResponse addVocals(SunoAddVocalsRequest request, String apiKey){
        return sunoFeignClient.addVocals(request,apiKey);
    }
}
