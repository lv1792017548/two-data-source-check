package com.example.twodatasource.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.twodatasource.entity.Purchase;
import com.example.twodatasource.entity.PurchaseOld;
import com.example.twodatasource.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PurchaseOldMapper extends BaseMapper<PurchaseOld> {

    int deleteByPrimaryKey(Long id);

    int insert(PurchaseOld record);

    int insertSelective(PurchaseOld record);

    PurchaseOld selectByPrimaryKey(Long id);

    PurchaseOld selectByNo(String no);

    int updatePurchaseById(PurchaseOld record);

}