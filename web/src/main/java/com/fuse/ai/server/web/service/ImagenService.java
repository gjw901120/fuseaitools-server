package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.image.Imagen4FastDTO;
import com.fuse.ai.server.web.model.dto.request.image.Imagen4GenerateDTO;
import com.fuse.ai.server.web.model.dto.request.image.Imagen4UltraDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface ImagenService {

    BaseResponse generate(Imagen4GenerateDTO request, UserJwtDTO userJwtDTO);

    BaseResponse fastGenerate(Imagen4FastDTO request, UserJwtDTO userJwtDTO);

    BaseResponse ultraGenerate(Imagen4UltraDTO request, UserJwtDTO userJwtDTO);

}
