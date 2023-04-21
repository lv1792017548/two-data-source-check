package com.example.twodatasource.util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.List;

import com.alibaba.otter.canal.protocol.Message;
public class CanalClient {
    public static void main(String[] args) throws Exception{
        //1.获取 canal 连接对象
        CanalConnector canalConnector =
                CanalConnectors.newSingleConnector(new
                        InetSocketAddress("10.7.106.243", 11111), "example", "", "");
        System.out.println("canal启动并开始监听数据 ...... ");
        while (true){
            canalConnector.connect();
            //订阅表
            canalConnector.subscribe("scm.purchase");
            //获取数据
            Message message = canalConnector.get(100);
            //解析message
            List<CanalEntry.Entry> entries = message.getEntries();
            if(entries.size() <=0){
                System.out.println("未检测到数据");
                Thread.sleep(1000);
            }
            for(CanalEntry.Entry entry : entries){
                //1、获取表名
                String tableName = entry.getHeader().getTableName();
                //2、获取类型
                CanalEntry.EntryType entryType = entry.getEntryType();
                //3、获取序列化后的数据
                ByteString storeValue = entry.getStoreValue();
                //判断是否rowdata类型数据
                if(CanalEntry.EntryType.ROWDATA.equals(entryType)){
                    //对第三步中的数据进行解析
                    CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(storeValue);
                    //获取当前事件的操作类型
                    CanalEntry.EventType eventType = rowChange.getEventType();
                    //获取数据集
                    List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
                    //便利数据
                    for(CanalEntry.RowData rowData : rowDatasList){
                        //数据变更之前的内容
                        JSONObject beforeData = new JSONObject();
                        List<CanalEntry.Column> beforeColumnsList = rowData.getAfterColumnsList();
                        for(CanalEntry.Column column : beforeColumnsList){
                            beforeData.put(column.getName(),column.getValue());
                        }
                        //数据变更之后的内容
                        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
                        JSONObject afterData = new JSONObject();
                        for(CanalEntry.Column column : afterColumnsList){
                            afterData.put(column.getName(),column.getValue());
                        }
                        System.out.println("Table ：" + tableName +
                                ",eventType :" + eventType +
                                ",beforeData :" + beforeData +
                                ",afterData : " + afterData);


                    }
                }else {
                    System.out.println("当前操作类型为：" + entryType);
                }
            }
        }
    }

}
