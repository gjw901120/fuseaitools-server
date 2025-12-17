package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.Models;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModelsManager {

    Integer getModelIdByName(@Param("modelName") String modelName);

    List<Models> getAll();

}
