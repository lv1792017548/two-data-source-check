package com.example.twodatasource.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.twodatasource.entity.Purchase;
import com.example.twodatasource.entity.PurchaseOld;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PurchaseMapper extends BaseMapper<Purchase> {
    int deleteByPrimaryKey(Long id);

    int insert(Purchase record);

    int insertSelective(Purchase record);

    Purchase selectByPrimaryKey(Long id);

    Purchase selectByNo(String no);


}