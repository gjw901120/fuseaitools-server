package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.image.SeedreamImageToImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.SeedreamTextToImageDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface SeedreamService {

    BaseResponse textToImage(SeedreamTextToImageDTO request, UserJwtDTO userJwtDTO);

    BaseResponse imageToImage(SeedreamImageToImageDTO request, UserJwtDTO userJwtDTO);

}
