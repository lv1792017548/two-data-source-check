package com.example.twodatasource.util;

import com.example.twodatasource.entity.Purchase;
import com.example.twodatasource.entity.PurchaseOld;

import java.util.ArrayList;
import java.util.List;

public class CheckPurchaseUtil {

    public static List<String> checkPurchase(PurchaseOld purchaseOld, Purchase purchase) {

        List<String> list = new ArrayList<>();

        if (purchase == null || purchaseOld == null) {
            list.add("新库或老库无该数据");
            return list;
        }
        String prefix = "采购单编号:"+purchaseOld.getPurchaseNo();

        if (!purchaseOld.getPurchaseName().equals(purchase.getPurchaseName())) {
            list.add(prefix + ":purchaseName不一致！");
        }
        if (!purchaseOld.getCreateTime().equals(purchase.getCreateTime())) {
            list.add(prefix + ":createTime不一致！");

        }
        if (!purchaseOld.getCreator().equals(purchase.getCreator())) {
            list.add(prefix + ":creator不一致！");

        }
        if (!purchaseOld.getRemark().equals(purchase.getRemark())) {
            list.add(prefix + ":remark不一致！");

        }
        if (!purchaseOld.getStatus().equals(purchase.getStatus())) {
            list.add(prefix + ":status不一致！");

        }
        if (!purchaseOld.getUpdater().equals(purchase.getUpdater())) {
            list.add(prefix + ":updater不一致！");

        }

        if (!purchaseOld.getUpdateTime().equals(purchase.getUpdateTime())) {
            list.add(prefix + ":updateTime不一致！");
        }
        if (list.size()==0){
            list.add(prefix + ":两张表保持一致！");
        }

        return list;
    }


    public static List<String> checkPurchase(List<PurchaseOld> purchaseOld, List<Purchase> purchase, String type, Integer param) {


        return null;
    }
}
