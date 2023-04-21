package com.example.twodatasource.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.List;

import com.alibaba.otter.canal.protocol.Message;

/**
 * @program: fas-haiot-interface
 * @description: 市级平台数据上传接口相关实现
 * @author: liuAnmin
 * @create: 2021-03-22 15:52
 **/

@Component
@Slf4j
public class CanalUtil {

//    @Resource
//    CorpsUploadService corpsUploadService;

    @Value("${canal-monitor-mysql.hostname}")
    String canalMonitorHost;

    @Value("${canal-monitor-mysql.port}")
    Integer canalMonitorPort;

    @Value("${canal-monitor-mysql.tableName}")
    String canalMonitorTableName;

    private final static int BATCH_SIZE = 10000;

    /**
     * 启动服务
     */
    @Async("TaskPool")
    public void startMonitorSQL() {
        while (true) {
            CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(canalMonitorHost, canalMonitorPort), "example", "canal", "canal");
            try {
                //打开连接
                connector.connect();
                log.info("数据库检测连接成功!" + canalMonitorTableName);
                //订阅数据库表,全部表q
                connector.subscribe(canalMonitorTableName + "\\..*");
                //回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
                connector.rollback();
                while (true) {
                    // 获取指定数量的数据
                    Message message = connector.getWithoutAck(BATCH_SIZE);
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0) {
                    } else {
                        handleDATAChange(message.getEntries());
                    }
                    // 提交确认
                    connector.ack(batchId);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("成功断开监测连接!尝试重连");
            } finally {
                connector.disconnect();
                //防止频繁访问数据库链接: 线程睡眠 10秒
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 打印canal server解析binlog获得的实体类信息
     */
    private void handleDATAChange(List<CanalEntry.Entry> entrys) {
        for (CanalEntry.Entry entry : entrys) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }
            //RowChange对象，包含了一行数据变化的所有特征
            CanalEntry.RowChange rowChage;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),e);
            }
            CanalEntry.EventType eventType = rowChage.getEventType();
            log.info("Canal监测到更新:【{}】", entry.getHeader().getTableName());

            log.info("Canal监测到rowChage更新:【{}】", JSON.toJSONString(rowChage));
            switch (eventType) {
                /**
                 * 删除操作
                 */
                case DELETE:
                   // corpsUploadService.DeleteOperateToCityInterface(rowChage, entry);
                    break;
                /**
                 * 添加操作
                 */
                case INSERT:
                   // corpsUploadService.InsertOperateToCityInterface(rowChage, entry);
                    break;
                /**
                 * 更新操作
                 */
                case UPDATE:
                   // corpsUploadService.UpdateOperateToCityInterface(rowChage, entry);
                    break;
                default:
                    break;
            }

        }
    }
}



