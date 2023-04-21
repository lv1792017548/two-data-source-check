package com.example.twodatasource.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.twodatasource.entity.Purchase;
import com.example.twodatasource.entity.PurchaseOld;

import java.util.List;

public interface PurchaseOldService {

    PurchaseOld selectOne(QueryWrapper<PurchaseOld> record);

    int insert(PurchaseOld record);

    List<PurchaseOld>  selectList(QueryWrapper<PurchaseOld> record);

    int update(PurchaseOld record);

}
