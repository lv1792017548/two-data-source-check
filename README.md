# two-data-source-check



## 测试痛点

### 解决新老系统采购单数据校验因人工肉眼检验效率低下问题。实现新老系统采购单数据自动化校验，



## 工具使用技术

- springboot框架方便集成其他框架，
- mybatis-plus java领域流行的orm框架，操作数据库
- mysql数据库，采购单服务采用的数据库，最重要的就是开源
- canal，canal 是阿里巴巴旗下的一款开源项目，纯Java开发。基于数据库增量日志解析，提供增量数据订阅&消费，目前主要支持了MySQL（也支持mariaDB）。



## 业务数据流转

新系统页面对采购单数据的增删改查操作 ,一方面落入新采购单数据库，另外会同步一份到老采购单数据库。

## 工具实现逻辑

v1:工具对外暴露的匹配接口，用户在页面提交一个采购单数据后，调用匹配接口，传入用采购单的编号，工具后端会用这笔采购单编号，去新库查询一条采购单信息，再去老库去查询一条采购单信息，然后进行字段校验，若字段完全一致接口返回校验通过，如果字段不一致，接口会返回不一致的字段。避免了，人工肉眼去校验新老数据库的数据一致性

v2:springboot集成 canal，监听新库的binlog（前提是，采购单落入新库和老库是原子操作，也就意味着，新库有binlog产生，那么老库理论上也会有对应的采购单产生），一旦监听新库有新的采购单binlog（包含采购单编号），会直接去新库查询一条采购单信息，再去老库查询一条采购单信息，然后进行字段校验，校验结果会在钉钉群里发布。

不同之处，v1版本需要用户自己手动调用接口，传入采购单编号，才能获取最终的匹配结果，v2代替了v1的手动操作，用户提交一条采购单数据以后，静等钉钉群消息返回的结果即可。大大提高的测试效率

## 代码主体结构
![i讯飞图片_1683354869432](https://user-images.githubusercontent.com/51152391/236607450-405ad876-cc80-4ef4-8222-0848da4096f0.png)


## 安装

- 下载所需文件， 里面有 canal 、 starter-canal-0.0.1-SNAPSHOT.jar 。链接如下，如失效，添加文末微信获取
  `https://pan.baidu.com/s/1R9PIB52OMNBh3Rx4jROWYA?pwd=hicd `

- 安装搭建canal服务端 https://blog.csdn.net/Ren_1760217692/article/details/126620250 

- 下载 starter-canal-0.0.1-SNAPSHOT.jar ，maven下载不了jar，需要 手动执行以下命令

  `mvn install:install-file -DgroupId=com.xpand -DartifactId=starter-canal -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar -Dfile=D:\starter-canal-0.0.1-SNAPSHOT.jar`

  **ps ： 注意 Dfile 的路径** 

- 安装mysql 5.7版本

## 数据库ddl

### 老数据库

```
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50736
Source Host           : localhost:3306
Source Database       : scmold

Target Server Type    : MYSQL
Target Server Version : 50736
File Encoding         : 65001

Date: 2023-05-06 15:51:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for purchase_old
-- ----------------------------
DROP TABLE IF EXISTS `purchase_old`;
CREATE TABLE `purchase_old` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '采购单id',
  `purchase_no` varchar(225) DEFAULT NULL COMMENT '采购单编号',
  `purchase_name` varchar(255) DEFAULT NULL COMMENT '采购单名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` int(10) unsigned DEFAULT '1' COMMENT '采购单状态 1：展示；0：已删除',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqe_key_purchase_no` (`purchase_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1649308976409571331 DEFAULT CHARSET=utf8mb4;

```



### 新数据库

```
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50736
Source Host           : localhost:3306
Source Database       : scm

Target Server Type    : MYSQL
Target Server Version : 50736
File Encoding         : 65001

Date: 2023-05-06 15:54:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for purchase
-- ----------------------------
DROP TABLE IF EXISTS `purchase`;
CREATE TABLE `purchase` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '采购单id',
  `purchase_no` varchar(225) DEFAULT NULL COMMENT '采购单编号',
  `purchase_name` varchar(255) DEFAULT NULL COMMENT '采购单名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` int(10) unsigned DEFAULT '1' COMMENT '采购单状态 1：展示；0：已删除',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqe_key_purchase_no` (`purchase_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1649288609091629064 DEFAULT CHARSET=utf8mb4;

```



## 适用场景

1、新造数据，新老数据库同一份数据一致性校验。

2、老库迁移新库后，数据一致性校验。

## 校验逻辑关键类解析

![image](https://user-images.githubusercontent.com/51152391/236608811-de7d9d68-50ff-4cdd-86f1-e6982ed01fc9.png)




## 交流群

- 项目参与者: 四哥
![微信图片_20230213112727](https://user-images.githubusercontent.com/51152391/236609043-e3146623-e808-456d-822b-1ca64b9a9e2c.jpg)


- 个人公众号：懂Java的测试

- 个人微信: `lvceshikaifa`

  

