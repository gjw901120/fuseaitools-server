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
    public SunoMusicResponse generateMusic(SunoGenerateRequest request){
        return sunoFeignClient.generateMusic(request);
    }

    /**
     * 延长音乐
     */
    @Override
    public SunoMusicResponse extendMusic(SunoExtendRequest request) {
        return sunoFeignClient.extendMusic(request);
    }

    /**
     * 上传并翻唱音乐
     */
    @Override
    public SunoMusicResponse uploadCover(SunoUploadCoverRequest request){
        return sunoFeignClient.uploadCover(request);
    }

    /**
     * 上传并扩展音乐
     */
    @Override
    public SunoMusicResponse uploadExtend(SunoUploadExtendRequest request){
        return sunoFeignClient.uploadExtend(request);
    }

    /**
     * 添加伴奏生成音乐
     */
    @Override
    public SunoMusicResponse addInstrumental(SunoAddInstrumentalRequest request){
        return sunoFeignClient.addInstrumental(request);
    }

    /**
     * 添加人声生成音乐
     */
    @Override
    public SunoMusicResponse addVocals(SunoAddVocalsRequest request){
        return sunoFeignClient.addVocals(request);
    }
}
