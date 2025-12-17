package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;

public interface VideoManager {

    VideoGenerateResponse veoGenerate(VeoGenerateRequest request);
    VideoGenerateResponse runwayGenerate(RunwayGenerateRequest request);

    VideoGenerateResponse runwayExtend(RunwayExtendRequest request);

    VideoGenerateResponse runwayAlephGenerate(RunwayAlephGenerateRequest request);

    VideoGenerateResponse lumaModify(LumaGenerateRequest request);

    VideoGenerateResponse veoExtend(VeoExtendRequest request);
}
