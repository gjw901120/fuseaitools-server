package com.fuse.ai.server.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuse.ai.server.manager.entity.Bill;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BillMapper extends BaseMapper<Bill> {
}