package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    public List<Bill> getListByUserId(Integer userId, Integer page, Integer size) {
        Page<Bill> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bill::getUserId, userId);
        queryWrapper.orderByDesc(Bill::getGmtModified);
        return billMapper.selectPage(pageInfo, queryWrapper).getRecords();
    }

    @Override
    public Bill getDetailByRecordId(String recordId) {
        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bill::getRecordId, recordId);
        return billMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer updateById(Bill bill) {
        return billMapper.updateById(bill);
    }

}
