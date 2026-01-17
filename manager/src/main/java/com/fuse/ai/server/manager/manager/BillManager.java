package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.Bill;

import java.util.List;

public interface BillManager {

    Integer insert(Bill bill);

    List<Bill> getListByUserId(Integer userId);

    Bill getDetailByRecordId(String recordId);

    Integer updateById(Bill bill);

}
