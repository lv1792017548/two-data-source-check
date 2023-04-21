package com.example.twodatasource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.twodatasource.entity.Purchase;
import com.example.twodatasource.entity.PurchaseOld;
import com.example.twodatasource.service.PurchaseNewService;
import com.example.twodatasource.service.PurchaseOldService;
import com.example.twodatasource.test.mapper.PurchaseMapper;
import com.example.twodatasource.test1.mapper.PurchaseOldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PurchaseNewServiceImpl implements PurchaseNewService {
    @Autowired
    private PurchaseMapper purchaseMapper;

    @Override
    public Purchase selectOne(QueryWrapper<Purchase> queryWrapperOld) {

        return purchaseMapper.selectOne(queryWrapperOld);
    }

    @Override
    public int insert(Purchase pu) {
        pu.setCreateTime(new Date());
        pu.setUpdateTime(new Date());
        return purchaseMapper.insert(pu);
    }

    @Override
    public List<Purchase> selectList(QueryWrapper<Purchase> queryWrapperOld) {

        return purchaseMapper.selectList(queryWrapperOld);
    }

}
