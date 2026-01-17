package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.SunoMusicResponse;

public interface SunoManger {

    SunoMusicResponse generateMusic(SunoGenerateRequest request, String apiKey);

    /**
     * 延长音乐
     */
    SunoMusicResponse extendMusic(SunoExtendRequest request, String apiKey);

    /**
     * 上传并翻唱音乐
     */
    SunoMusicResponse uploadCover(SunoUploadCoverRequest request, String apiKey);

    /**
     * 上传并扩展音乐
     */
    SunoMusicResponse uploadExtend(SunoUploadExtendRequest request, String apiKey);

    /**
     * 添加伴奏生成音乐
     */
    SunoMusicResponse addInstrumental(SunoAddInstrumentalRequest request, String apiKey);

    /**
     * 添加人声生成音乐
     */
    SunoMusicResponse addVocals(SunoAddVocalsRequest request, String apiKey);
}
