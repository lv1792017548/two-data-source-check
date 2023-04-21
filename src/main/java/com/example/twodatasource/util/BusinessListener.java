package com.example.twodatasource.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.twodatasource.entity.Purchase;
import com.example.twodatasource.entity.PurchaseOld;
import com.example.twodatasource.service.PurchaseNewService;
import com.example.twodatasource.service.PurchaseOldService;

import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Slf4j
@CanalEventListener
public class BusinessListener {

    @Autowired
    private PurchaseOldService purchaseOldService;

    @Autowired
    private PurchaseNewService purchaseNewService;

    /**
     * @param eventType 当前操作数据库的类型
     * @param rowData   当前操作数据库的数据
     */
    @ListenPoint(schema = "scm", table = "purchase")
    public void adUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        System.out.println("purchase发生改变");
        //获取改变之前的数据
       // rowData.getBeforeColumnsList().forEach((c) -> System.out.println("改变前的数据:" + c.getName() + "::" + c.getValue()));
        //获取改变之后的数据
       // rowData.getAfterColumnsList().forEach((c) -> System.out.println("改变之后的数据:" + c.getName() + "::" + c.getValue()));

//        JSONObject beforeData = new JSONObject();
//        List<CanalEntry.Column> beforeColumnsList = rowData.getAfterColumnsList();
//        for(CanalEntry.Column column : beforeColumnsList){
//            beforeData.put(column.getName(),column.getValue());
//        }
//        log.info("Canal监测到更新之前:【{}】", beforeData);
        //数据变更之后的内容
        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        JSONObject afterData = new JSONObject();
        for(CanalEntry.Column column : afterColumnsList){
            afterData.put(column.getName(),column.getValue());
        }
        log.info("Canal监测到更新之后:【{}】", afterData);

        try {
            String purchaseNo = afterData.getString("purchase_no");

            QueryWrapper<PurchaseOld> queryWrapperOld = new QueryWrapper();
            queryWrapperOld.eq("purchase_no", purchaseNo);//必须是数据库中的字段
            PurchaseOld purchaseOld =  purchaseOldService.selectOne(queryWrapperOld);
            log.info("purchaseOld:【{}】", JSON.toJSONString(purchaseOld));




            QueryWrapper<Purchase> queryWrapperNew = new QueryWrapper();
            queryWrapperNew.eq("purchase_no", purchaseNo);//必须是数据库中的字段
            Purchase purchase =  purchaseNewService.selectOne(queryWrapperNew);
            log.info("purchase:【{}】", JSON.toJSONString(purchase));

            List<String>  list=  CheckPurchaseUtil.checkPurchase(purchaseOld,purchase);

            log.info("新老数据库校验后:{}", JSON.toJSONString(list));
        }catch (Exception e){
            log.info("Canal监测到Exception {}", e.getMessage());

        }




    }
}