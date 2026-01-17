package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsAudioIsolationDTO;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsSTTDTO;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsSoundEffectDTO;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsTTSDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.service.ElevenlabsService;
import com.fuse.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/audio/elevenLabs")
public class ElevenLabsController {

    @Autowired
    private ElevenlabsService elevenlabsService;

    @PostMapping("/text-to-speech")
    public ResponseResult<?> textToSpeech(@Valid @RequestBody ElevenlabsTTSDTO request,
                                          @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(elevenlabsService.elevenlabsTTS(request, userJwtDTO));
    }

    @PostMapping("/speech-to-text")
    public ResponseResult<?> speechToText(@Valid @RequestBody ElevenlabsSTTDTO request,
                                          @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(elevenlabsService.elevenlabsSTT(request, userJwtDTO));
    }

    @PostMapping("/sound-effect-v2")
    public ResponseResult<?> soundEffectV2(@Valid @RequestBody ElevenlabsSoundEffectDTO request,
                                           @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(elevenlabsService.elevenlabsSoundEffectDTO(request, userJwtDTO));
    }

    @PostMapping("/audio-isolation")
    public ResponseResult<?> audioIsolation(@Valid @RequestBody ElevenlabsAudioIsolationDTO request,
                                            @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(elevenlabsService.elevenlabsAudioIsolationDTO(request, userJwtDTO));
    }


}
