package com.example.twodatasource.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.twodatasource.entity.Purchase;
import com.example.twodatasource.entity.PurchaseOld;
import com.example.twodatasource.entity.User;
import com.example.twodatasource.service.PurchaseNewService;
import com.example.twodatasource.service.PurchaseOldService;
import com.example.twodatasource.test.mapper.PurchaseMapper;
import com.example.twodatasource.test.mapper.UserMapper;
import com.example.twodatasource.test1.mapper.PurchaseOldMapper;
import com.example.twodatasource.test1.mapper.UserMapper1;
import com.example.twodatasource.util.CheckPurchaseUtil;
import com.example.twodatasource.util.CommonBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@RestController
public class Mycontroller {

    @Autowired
    private PurchaseOldService purchaseOldService;

    @Autowired
    private PurchaseNewService purchaseNewService;

    @GetMapping("/check/purchase/{purchaseNo}")
    public List<String> checkPurchase(@PathVariable("purchaseNo")  String purchaseNo) {
        List<String> list = new ArrayList<>();
        if (null == purchaseNo || "" == purchaseNo) {
            list.add("purchaseNo不能为空！");
            return list;
        }
        QueryWrapper<PurchaseOld> queryWrapperOld = new QueryWrapper();
        queryWrapperOld.eq("purchase_no", purchaseNo);//必须是数据库中的字段
        PurchaseOld purchaseOld =  purchaseOldService.selectOne(queryWrapperOld);
        log.info("purchaseOld:【{}】", JSON.toJSONString(purchaseOld));




        QueryWrapper<Purchase> queryWrapperNew = new QueryWrapper();
        queryWrapperNew.eq("purchase_no", purchaseNo);//必须是数据库中的字段
        Purchase purchase =  purchaseNewService.selectOne(queryWrapperNew);
        log.info("purchase:【{}】", JSON.toJSONString(purchase));

        return CheckPurchaseUtil.checkPurchase(purchaseOld,purchase);
    }

    @PostMapping("/insert/purchase")
    public void synPurchase(@RequestBody Purchase pu) {
        log.info("Purchase更新:【{}】", JSON.toJSONString(pu));
        pu.setCreateTime(new Date());
        pu.setUpdateTime(new Date());
       if(purchaseNewService.insert(pu)>=1) {
           PurchaseOld purchaseOld = CommonBeanUtils.beanCopy(pu,PurchaseOld.class);
           log.info("PurchaseOld更新:【{}】", JSON.toJSONString(purchaseOld));
           purchaseOldService.insert(purchaseOld);
       }
    }

    @GetMapping("/checkAfterMigrate/purchase/{type}")
    public void checkAfterMigratePurchase(@PathVariable("type")  String type,@RequestParam("param") int param) {
        log.info("迁移后校验类型:{}", JSON.toJSONString(type));
        //1：全量校验 param=0； 2：步长校验 param=步长大小；3：随机校验 param=随机次数

        if (type.equals("1")){
            QueryWrapper<PurchaseOld> queryWrapperOld = new QueryWrapper();
            queryWrapperOld.ge("id", 1);//必须是数据库中的字段
            List<PurchaseOld> selectListOld =  purchaseOldService.selectList(queryWrapperOld);
            log.info("selectListOld:【{}】", JSON.toJSONString(selectListOld));

            QueryWrapper<Purchase> queryWrapperNew = new QueryWrapper();
            queryWrapperNew.ge("id", 1);//必须是数据库中的字段
            List<Purchase> selectListNew =  purchaseNewService.selectList(queryWrapperNew);

            log.info("selectListNew:【{}】", JSON.toJSONString(selectListNew));


        }else  if(type.equals("2")){
            QueryWrapper<PurchaseOld> queryWrapperOld = new QueryWrapper();
            queryWrapperOld.orderByDesc("purchase_no");//必须是数据库中的字段
            List<PurchaseOld> selectListOld =  purchaseOldService.selectList(queryWrapperOld);
            log.info("selectListOld:【{}】", JSON.toJSONString(selectListOld));

            QueryWrapper<Purchase> queryWrapperNew = new QueryWrapper();
            queryWrapperNew.orderByDesc("purchase_no");//必须是数据库中的字段
            List<Purchase> selectListNew =  purchaseNewService.selectList(queryWrapperNew);
            log.info("selectListNew:【{}】", JSON.toJSONString(selectListNew));

        }

      /*  else {


            QueryWrapper<PurchaseOld> queryWrapperOld = new QueryWrapper();
            queryWrapperOld.
            queryWrapperOld.ge("id", 1);//必须是数据库中的字段
            List<PurchaseOld> selectListOld =  purchaseOldMapper.selectList(queryWrapperOld);



            QueryWrapper<Purchase> queryWrapperNew = new QueryWrapper();
            queryWrapperNew.ge("id", 1);//必须是数据库中的字段
            List<Purchase> selectListNew =  purchaseNewMapper.selectList(queryWrapperNew);
        }
*/



    }

    @PostMapping("/test")
    public void sendMsg() throws Exception {
        String webhook = "https://oapi.dingtalk.com/robot/send?access_token=d68c99bbccdc95b97d23fee8aec757e3e3db252e888b5ba8cb4a2f3f7138347c";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject json = new JSONObject();
        json.put("msgtype", "text");
        JSONObject text = new JSONObject();
        text.put("content", "079654dfghdfg");
        json.put("text", text);
        log.info("开始发送");

        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(webhook, request, String.class);
        log.info("结束发送："+response.getStatusCode());

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Error occurred while sending message to DingTalk. Status code="
                    + response.getStatusCode() + ", message=" + response.getBody());
        }
    }
}
