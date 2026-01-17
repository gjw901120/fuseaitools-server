package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsAudioIsolationDTO;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsSTTDTO;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsSoundEffectDTO;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsTTSDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface ElevenlabsService {

    BaseResponse elevenlabsTTS(ElevenlabsTTSDTO request, UserJwtDTO userJwtDTO);

    BaseResponse elevenlabsSTT(ElevenlabsSTTDTO request, UserJwtDTO userJwtDTO);

    BaseResponse elevenlabsAudioIsolationDTO(ElevenlabsAudioIsolationDTO request, UserJwtDTO userJwtDTO);
    BaseResponse elevenlabsSoundEffectDTO(ElevenlabsSoundEffectDTO request, UserJwtDTO userJwtDTO);

}
