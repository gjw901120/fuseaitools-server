package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CommonService {

    UploadResponse uploadFile(MultipartFile[] files, UserJwtDTO userJwtDTO);

}
