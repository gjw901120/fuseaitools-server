package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuse.ai.server.manager.entity.Bill;
import com.fuse.ai.server.manager.manager.BillManager;
import com.fuse.ai.server.manager.mapper.BillMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class BillManagerImpl implements BillManager {

    @Resource
    private BillMapper billMapper;

    @Override
    public Integer insert(Bill bill) {
        billMapper.insert(bill);
        return bill.getId();
    }

    @Override
    public List<Bill> getListByUserId(Integer userId) {
        QueryWrapper<Bill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return billMapper.selectList(queryWrapper);
    }

}
