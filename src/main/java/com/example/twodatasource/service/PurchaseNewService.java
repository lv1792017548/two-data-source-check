package com.example.twodatasource.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.twodatasource.entity.Purchase;
import com.example.twodatasource.entity.PurchaseOld;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PurchaseNewService {


    Purchase selectOne(QueryWrapper<Purchase> record);

    int insert(Purchase record);

    List<Purchase> selectList(QueryWrapper<Purchase> record);




}
