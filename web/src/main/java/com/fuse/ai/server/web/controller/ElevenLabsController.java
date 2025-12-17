package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsAudioIsolationDTO;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsSTTDTO;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsSoundEffectDTO;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsTTSDTO;
import com.fuse.ai.server.web.service.ElevenlabsService;
import com.simply.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseResult<?> textToSpeech(@Valid @RequestBody ElevenlabsTTSDTO request) {

        return ResponseResult.success(elevenlabsService.elevenlabsTTS(request));
    }

    @PostMapping("/speech-to-text")
    public ResponseResult<?> speechToText(@Valid @RequestBody ElevenlabsSTTDTO request) {

        return ResponseResult.success(elevenlabsService.elevenlabsSTT(request));
    }

    @PostMapping("/sound-effect-v2")
    public ResponseResult<?> soundEffectV2(@Valid @RequestBody ElevenlabsSoundEffectDTO request) {

        return ResponseResult.success(elevenlabsService.elevenlabsSoundEffectDTO(request));
    }

    @PostMapping("/audio-isolation")
    public ResponseResult<?> audioIsolation(@Valid @RequestBody ElevenlabsAudioIsolationDTO request) {

        return ResponseResult.success(elevenlabsService.elevenlabsAudioIsolationDTO(request));
    }


}
