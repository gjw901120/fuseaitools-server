package com.fuse.ai.server.web.service;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;

import java.util.Map;

public interface UserCreditsService {

    verifyCreditsBO verifyCredits(Integer userId, Models model, ExtraDataBO extraData);

}
