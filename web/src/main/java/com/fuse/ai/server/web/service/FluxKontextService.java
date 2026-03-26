package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.image.*;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface FluxKontextService {

    BaseResponse fluxKontextGenerate(FluxKontextGenerateDTO request, UserJwtDTO userJwtDTO);

    BaseResponse flux2ProImageToImage(Flux2ProImageToImageDTO request, UserJwtDTO userJwtDTO);

    BaseResponse flux2ProTextToImage(Flux2ProTextToImageDTO request, UserJwtDTO userJwtDTO);

    BaseResponse flux2TextToImage(Flux2TextToImageDTO request, UserJwtDTO userJwtDTO);

    BaseResponse flux2ImageToImage(Flux2ImageToImageDTO request, UserJwtDTO userJwtDTO);

}
