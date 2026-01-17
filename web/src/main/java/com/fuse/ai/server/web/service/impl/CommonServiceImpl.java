package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.web.common.utils.S3UploadUtil;
import com.fuse.ai.server.web.config.exception.ResponseErrorType;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.UploadResponse;
import com.fuse.ai.server.web.service.CommonService;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.SystemErrorType;
import com.fuse.common.core.exception.error.UserErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private S3UploadUtil s3UploadUtil;

    public UploadResponse uploadFile(MultipartFile[] files, UserJwtDTO userJwtDTO) {

        if(files.length == 0) {
            throw new BaseException(ResponseErrorType.FILE_EMPTY_ERROR, "No file to upload");
        }

        UploadResponse uploadResponse = new UploadResponse();
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = s3UploadUtil.uploadFile(file);
            urls.add(url);
        }
        uploadResponse.setUrls(urls);

        return uploadResponse;
    }

}
