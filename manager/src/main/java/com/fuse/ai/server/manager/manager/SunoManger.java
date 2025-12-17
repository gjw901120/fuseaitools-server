package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.SunoMusicResponse;

public interface SunoManger {

    SunoMusicResponse generateMusic(SunoGenerateRequest request);

    /**
     * 延长音乐
     */
    SunoMusicResponse extendMusic(SunoExtendRequest request);

    /**
     * 上传并翻唱音乐
     */
    SunoMusicResponse uploadCover(SunoUploadCoverRequest request);

    /**
     * 上传并扩展音乐
     */
    SunoMusicResponse uploadExtend(SunoUploadExtendRequest request);

    /**
     * 添加伴奏生成音乐
     */
    SunoMusicResponse addInstrumental(SunoAddInstrumentalRequest request);

    /**
     * 添加人声生成音乐
     */
    SunoMusicResponse addVocals(SunoAddVocalsRequest request);
}
