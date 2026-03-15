package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.image.*;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface IdeogramService {

    /**
     * Ideogram V3 文生图
     */
    BaseResponse v3TextToImage(IdeogramV3TextToImageDTO request, UserJwtDTO userJwtDTO);

    /**
     * Ideogram V3 编辑
     */
    BaseResponse v3Edit(IdeogramV3EditDTO request, UserJwtDTO userJwtDTO);

    /**
     * Ideogram V3 重混
     */
    BaseResponse v3Remix(IdeogramV3RemixDTO request, UserJwtDTO userJwtDTO);

    /**
     * Ideogram V3 重构
     */
    BaseResponse v3Reframe(IdeogramV3ReframeDTO request, UserJwtDTO userJwtDTO);

    /**
     * Ideogram 角色生成
     */
    BaseResponse character(IdeogramCharacterDTO request, UserJwtDTO userJwtDTO);

    /**
     * Ideogram 角色编辑
     */
    BaseResponse characterEdit(IdeogramCharacterEditDTO request, UserJwtDTO userJwtDTO);

    /**
     * Ideogram 角色重混
     */
    BaseResponse characterRemix(IdeogramCharacterRemixDTO request, UserJwtDTO userJwtDTO);
}