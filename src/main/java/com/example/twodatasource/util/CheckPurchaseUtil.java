package com.example.twodatasource.util;

import com.example.twodatasource.entity.Purchase;
import com.example.twodatasource.entity.PurchaseOld;

import java.util.ArrayList;
import java.util.List;

public class CheckPurchaseUtil {

    public static List<String> checkPurchase(PurchaseOld purchaseOld, Purchase purchase){

        List<String> list = new ArrayList<>();

        if (!purchaseOld.getPurchaseName().equals(purchase.getPurchaseName())){
            list.add(purchaseOld.getPurchaseNo() +"purchaseName不一致！");
        }else if (!purchaseOld.getCreateTime().equals(purchase.getCreateTime())){
            list.add(purchaseOld.getPurchaseNo() +"createTime不一致！");

        }else if (!purchaseOld.getCreator().equals(purchase.getCreator())){
            list.add(purchaseOld.getPurchaseNo() +"creator不一致！");

        }else if (!purchaseOld.getRemark().equals(purchase.getRemark())){
            list.add(purchaseOld.getPurchaseNo() +"remark不一致！");

        }else if (!purchaseOld.getStatus().equals(purchase.getStatus())){
            list.add(purchaseOld.getPurchaseNo() +"status不一致！");

        }else if (!purchaseOld.getUpdater().equals(purchase.getUpdater())){
            list.add(purchaseOld.getPurchaseNo() +"updater不一致！");

        }else if (!purchaseOld.getUpdateTime().equals(purchase.getUpdateTime())){
            list.add(purchaseOld.getPurchaseNo() +"updateTime不一致！");

        }else {
            list.add(purchaseOld.getPurchaseNo() +":两张表保持一致！");

        }
        return  list;
    }


    public static List<String> checkPurchase(List<PurchaseOld> purchaseOld, List<Purchase> purchase,String type,Integer param){



        return  null;
    }
}
