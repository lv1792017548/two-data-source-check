package com.example.twodatasource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.twodatasource.entity.Purchase;
import com.example.twodatasource.entity.PurchaseOld;
import com.example.twodatasource.service.PurchaseOldService;
import com.example.twodatasource.test1.mapper.PurchaseOldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class PurchaseOldServiceImpl implements PurchaseOldService {

    @Autowired
    private PurchaseOldMapper purchaseOldMapper;

    @Override
    public PurchaseOld selectOne(QueryWrapper<PurchaseOld> queryWrapperOld) {

        return purchaseOldMapper.selectOne(queryWrapperOld);
    }

    @Override
    public int insert(PurchaseOld pu) {
        pu.setCreateTime(new Date());
        pu.setUpdateTime(new Date());
        return purchaseOldMapper.insert(pu);
    }

    @Override
    public List<PurchaseOld> selectList(QueryWrapper<PurchaseOld> queryWrapperOld) {

        return purchaseOldMapper.selectList(queryWrapperOld);
    }

    @Override
    public int update(PurchaseOld record) {
        return purchaseOldMapper.updatePurchaseById(record);

    }
}
