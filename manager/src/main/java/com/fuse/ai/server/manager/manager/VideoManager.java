package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;

public interface VideoManager {

    VideoGenerateResponse veoGenerate(VeoGenerateRequest request, String apiKey);
    VideoGenerateResponse runwayGenerate(RunwayGenerateRequest request, String apiKey);

    VideoGenerateResponse runwayExtend(RunwayExtendRequest request, String apiKey);

    VideoGenerateResponse runwayAlephGenerate(RunwayAlephGenerateRequest request, String apiKey);

    VideoGenerateResponse lumaModify(LumaGenerateRequest request, String apiKey);

    VideoGenerateResponse veoExtend(VeoExtendRequest request, String apiKey);
}
