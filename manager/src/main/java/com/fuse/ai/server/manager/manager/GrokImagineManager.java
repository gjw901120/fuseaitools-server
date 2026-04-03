package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.image.GrokImagineImageToImageRequest;
import com.fuse.ai.server.manager.model.request.image.GrokImagineTextToImageRequest;
import com.fuse.ai.server.manager.model.request.video.GrokImagineExtendRequest;
import com.fuse.ai.server.manager.model.request.video.GrokImagineImageToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.GrokImagineTextToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.GrokImagineUpscaleRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;

public interface GrokImagineManager {

    ImageGenerateResponse textToImage(GrokImagineTextToImageRequest request, String apiKey);

    ImageGenerateResponse imageToImage(GrokImagineImageToImageRequest request, String apiKey);

    VideoGenerateResponse textToVideo(GrokImagineTextToVideoRequest request, String apiKey);

    VideoGenerateResponse imageToVideo(GrokImagineImageToVideoRequest request, String apiKey);

    VideoGenerateResponse upscale(GrokImagineUpscaleRequest request, String apiKey);

    VideoGenerateResponse extend(GrokImagineExtendRequest request, String apiKey);
}
