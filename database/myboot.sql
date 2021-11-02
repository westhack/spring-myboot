/*
 Navicat Premium Data Transfer

 Source Server         : 本地开发
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : myboot

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 02/11/2021 15:08:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of QRTZ_BLOB_TRIGGERS
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of QRTZ_CALENDARS
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(200) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of QRTZ_CRON_TRIGGERS
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint NOT NULL,
  `SCHED_TIME` bigint NOT NULL,
  `PRIORITY` int NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of QRTZ_FIRED_TRIGGERS
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of QRTZ_JOB_DETAILS
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of QRTZ_LOCKS
-- ----------------------------
BEGIN;
INSERT INTO `QRTZ_LOCKS` VALUES ('quartzScheduler', 'TRIGGER_ACCESS');
COMMIT;

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint NOT NULL,
  `CHECKIN_INTERVAL` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of QRTZ_SCHEDULER_STATE
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint NOT NULL,
  `REPEAT_INTERVAL` bigint NOT NULL,
  `TIMES_TRIGGERED` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int DEFAULT NULL,
  `INT_PROP_2` int DEFAULT NULL,
  `LONG_PROP_1` bigint DEFAULT NULL,
  `LONG_PROP_2` bigint DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint DEFAULT NULL,
  `PREV_FIRE_TIME` bigint DEFAULT NULL,
  `PRIORITY` int DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint NOT NULL,
  `END_TIME` bigint DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of QRTZ_TRIGGERS
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_apis
-- ----------------------------
DROP TABLE IF EXISTS `sys_apis`;
CREATE TABLE `sys_apis` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `path` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'api路径',
  `description` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'api中文描述',
  `api_group` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'api组',
  `method` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'POST' COMMENT '方法',
  `created_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `updated_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_sys_apis_deleted_at` (`deleted_at`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_apis
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_auto_code_histories
-- ----------------------------
DROP TABLE IF EXISTS `sys_auto_code_histories`;
CREATE TABLE `sys_auto_code_histories` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `auto_code_path` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `class_name` varchar(255) DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `flag` int DEFAULT NULL,
  `auto_code` longtext CHARACTER SET utf8 COLLATE utf8_general_ci,
  `request_meta` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `table_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of sys_auto_code_histories
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_clients
-- ----------------------------
DROP TABLE IF EXISTS `sys_clients`;
CREATE TABLE `sys_clients` (
  `id` bigint unsigned NOT NULL,
  `client_secret` varchar(255) DEFAULT NULL,
  `home_uri` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `redirect_uri` varchar(255) DEFAULT NULL,
  `auto_approve` bit(1) DEFAULT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of sys_clients
-- ----------------------------
BEGIN;
INSERT INTO `sys_clients` VALUES (1, 'system', NULL, 'app', 'home', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_configs
-- ----------------------------
DROP TABLE IF EXISTS `sys_configs`;
CREATE TABLE `sys_configs` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `parent_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '父类',
  `label` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标题',
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置的key键名',
  `value` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '配置的val值',
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT 'input' COMMENT '类型',
  `description` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
  `sort_order` int unsigned DEFAULT '999999' COMMENT '排序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：1-正常',
  `module` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '分組',
  `data_source` longtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '数据',
  `created_at` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `deleted_at` datetime DEFAULT NULL COMMENT '删除时间',
  `rule_source` tinytext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '规则',
  `multiple` tinyint(1) DEFAULT '0' COMMENT '是否多值',
  `value_type` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '值格式化',
  `fields` text COMMENT '动态input字段',
  `limit` int DEFAULT NULL COMMENT '动态input限制数量',
  `created_by` varchar(255) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`name`) USING BTREE,
  KEY `group` (`module`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='系统配置';

-- ----------------------------
-- Records of sys_configs
-- ----------------------------
BEGIN;
INSERT INTO `sys_configs` VALUES (1, '', '系统设置', 'core', '', 'label', NULL, 1, 1, 'core', NULL, '2019-07-27 14:31:10', '2019-08-02 14:37:05', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_configs` VALUES (15, '', '系统字典', 'dict', '', 'label', NULL, 2, 1, 'core', 'null', '2019-07-27 14:31:10', '2021-10-09 17:06:26', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_configs` VALUES (16, 'core', '应用设置', 'app', '{\"name\":\"MyBoot\",\"logo\":\"http://qmplusimg.henrongyi.top/gvalogo.png\",\"abort\":\"\"}', 'single-dynamic-input', NULL, 1, 1, 'core', NULL, '2019-07-27 14:31:10', '2021-10-09 21:20:25', NULL, '', 0, 'JSONString', '{\n    \"name\": {\n      \"label\": \"应用名称\",\n      \"name\": \"name\",\n      \"type\": \"input\",\n      \"value\": null\n    },\n    \"logo\": {\n      \"label\": \"应用LOGO\",\n      \"name\": \"logo\",\n      \"type\": \"single-select-image\",\n      \"value\": null\n    },\n    \"abort\": {\n      \"label\": \"应用简介\",\n      \"name\": \"abort\",\n      \"type\": \"editor\",\n      \"value\": null\n    }\n  }', NULL, NULL, NULL);
INSERT INTO `sys_configs` VALUES (41, 'dict', '性别', 'sex', '[{\"label\":\"男\",\"value\":\"1\",\"color\":\"\"},{\"label\":\"女\",\"value\":\"2\",\"color\":\"\"}]', 'dict', '', 1, 1, 'dict', '', '2021-09-14 10:11:54', '2021-10-09 21:20:25', NULL, '', 1, 'JSONString', NULL, NULL, NULL, NULL);
INSERT INTO `sys_configs` VALUES (42, 'dict', '订单状态', 'order-status', '[{\"name\":\"0\",\"value\":\"0\",\"label\":\"未支付\"},{\"name\":\"1\",\"value\":\"1\",\"label\":\"已支付\"}]', 'dict', '', 1, 1, 'dict', '', '2021-09-14 15:45:54', '2021-10-09 21:20:25', NULL, 'type=array,required=true,whitespace=true,message=不能为空', 1, 'JSONString', NULL, NULL, NULL, NULL);
INSERT INTO `sys_configs` VALUES (45, 'core', '本地上传配置', 'local', '{\"path\":\"uploads/file\"}', 'single-dynamic-input', NULL, 5, 1, 'core', NULL, NULL, '2021-10-09 21:20:25', NULL, NULL, 0, 'JSONString', '{\n  \"path\": {\n    \"label\": \"上传路径\",\n    \"name\": \"path\",\n    \"type\": \"input\",\n    \"value\":\"\"\n  }\n}', NULL, NULL, NULL);
INSERT INTO `sys_configs` VALUES (46, 'core', '阿里云存储设置', 'aliyun-oss', '{\"access-key-id\":null,\"access-key-secret\":null,\"base-path\":null,\"bucket-name\":null,\"bucket-url\":null,\"endpoint\":null}', 'single-dynamic-input', NULL, 6, 1, 'core', NULL, NULL, '2021-10-09 21:20:25', NULL, NULL, 0, 'JSONString', '{\n    \"accessKeyId\": {\n      \"label\": \"accessKeyId\",\n      \"name\": \"access-key-id\",\n      \"type\": \"input\",\n      \"value\": null\n    },\n    \"accessKeySecret\": {\n      \"label\": \"accessKeySecret\",\n      \"name\": \"access-key-secret\",\n      \"type\": \"input\",\n      \"value\": null\n    },\n    \"basePath\": {\n      \"label\": \"basePath\",\n      \"name\": \"base-path\",\n      \"type\": \"input\",\n      \"value\": null\n    },\n    \"bucketName\": {\n      \"label\": \"bucketName\",\n      \"name\": \"bucket-name\",\n      \"type\": \"input\",\n      \"value\": null\n    },\n    \"bucketUrl\": {\n      \"label\": \"bucketUrl\",\n      \"name\": \"bucket-url\",\n      \"type\": \"input\",\n      \"value\": null\n    },\n    \"endpoint\": {\n      \"label\": \"endpoint\",\n      \"name\": \"endpoint\",\n      \"type\": \"input\",\n      \"value\": null\n    }\n  }', NULL, NULL, NULL);
INSERT INTO `sys_configs` VALUES (48, 'core', '阿里大鱼短信设置', 'aldy', '{\"access-key-id\":null,\"access-key-secret\":null,\"sign-name\":null,\"template-code\":null,\"template-param\":null}', 'single-dynamic-input', NULL, 8, 1, 'core', NULL, NULL, '2021-10-09 21:20:25', NULL, NULL, 0, 'JSONString', '{\n    \"accessKeyId\": {\n      \"label\": \"accessKeyId\",\n      \"name\": \"access-key-id\",\n      \"type\": \"input\",\n      \"value\": null\n    },\n    \"accessKeySecret\": {\n      \"label\": \"accessKeySecret\",\n      \"name\": \"access-key-secret\",\n      \"type\": \"input\",\n      \"value\": null\n    },\n    \"signName\": {\n      \"label\": \"signName\",\n      \"name\": \"sign-name\",\n      \"type\": \"input\",\n      \"value\": null\n    },\n    \"templateCode\": {\n      \"label\": \"templateCode\",\n      \"name\": \"template-code\",\n      \"type\": \"input\",\n      \"value\": null\n    },\n    \"templateParam\": {\n      \"label\": \"templateParam\",\n      \"name\": \"template-param\",\n      \"type\": \"input\",\n      \"value\": null\n    }\n  }', NULL, NULL, NULL);
INSERT INTO `sys_configs` VALUES (50, 'core', '图片验证码设置', 'captcha', '{\"img-height\":240,\"img-width\":80,\"key-long\":4}', 'single-dynamic-input', NULL, 10, 1, 'core', NULL, NULL, '2021-10-09 21:20:25', NULL, NULL, 0, 'JSONString', '{\n    \"imgHeight\": {\n      \"label\": \"验证码宽度\",\n      \"name\": \"img-height\",\n      \"type\": \"input-number\",\n      \"value\": 80\n    },\n    \"imgWidth\": {\n      \"label\": \"验证码高度\",\n      \"name\": \"img-width\",\n      \"type\": \"input-number\",\n      \"value\": 240\n    },\n    \"keyLong\": {\n      \"label\": \"验证码长度\",\n      \"name\": \"key-long\",\n      \"type\": \"input-number\",\n      \"value\": 4\n    }\n  }', NULL, NULL, NULL);
INSERT INTO `sys_configs` VALUES (51, 'core', '邮箱设置', 'email', '{\"from\":\"xxx\",\"host\":\"xxx\",\"is-ssl\":false,\"nickname\":\"xxx\",\"port\":465,\"secret\":\"xxx\",\"to\":\"xxx@qq.com\"}', 'single-dynamic-input', NULL, 11, 1, 'core', NULL, NULL, '2021-10-09 21:20:25', NULL, NULL, 0, 'JSONString', '{\n    \"from\": {\n      \"label\": \"from\",\n      \"name\": \"from\",\n      \"type\": \"input\",\n      \"value\": \"xxx\"\n    },\n    \"host\": {\n      \"label\": \"host\",\n      \"name\": \"host\",\n      \"type\": \"input\",\n      \"value\": \"xxx\"\n    },\n    \"isSsl\": {\n      \"label\": \"isSsl\",\n      \"name\": \"is-ssl\",\n      \"type\": \"boolean\",\n      \"value\": true\n    },\n    \"nickname\": {\n      \"label\": \"nickname\",\n      \"name\": \"nickname\",\n      \"type\": \"input\",\n      \"value\": \"xxx\"\n    },\n    \"port\": {\n      \"label\": \"port\",\n      \"name\": \"port\",\n      \"type\": \"input\",\n      \"value\": 465\n    },\n    \"secret\": {\n      \"label\": \"secret\",\n      \"name\": \"secret\",\n      \"type\": \"input\",\n      \"value\": \"xxx\"\n    },\n    \"to\": {\n      \"label\": \"to\",\n      \"name\": \"to\",\n      \"type\": \"input\",\n      \"value\": \"xxx@qq.com\"\n    }\n  }', NULL, NULL, NULL);
INSERT INTO `sys_configs` VALUES (53, 'core', 'JWT 设置', 'jwt', '{\"buffer-time\":86400,\"expires-time\":604800,\"signing-key\":\"myBoot\"}', 'single-dynamic-input', NULL, 13, 1, 'core', NULL, NULL, '2021-10-09 21:20:25', NULL, NULL, 0, 'JSONString', '{\n    \"bufferTime\": {\n      \"label\": \"bufferTime\",\n      \"name\": \"buffer-time\",\n      \"type\": \"input\",\n      \"value\": 86400\n    },\n    \"expiresTime\": {\n      \"label\": \"JWT 过期时间（秒）\",\n      \"name\": \"expires-time\",\n      \"type\": \"input\",\n      \"value\": 604800\n    },\n    \"signingKey\": {\n      \"label\": \"JWT key\",\n      \"name\": \"signing-key\",\n      \"type\": \"input\",\n      \"value\": \"myBoot\"\n    }\n  }', NULL, NULL, NULL);
INSERT INTO `sys_configs` VALUES (55, 'core', '腾讯云存储设置', 'tencent-cos', '{\"base-url\":\"https://gin.vue.admin\",\"bucket\":\"xxxxx-10005608\",\"path-prefix\":\"gin-myboot\",\"region\":\"ap-shanghai\",\"secret-id\":\"xxxxxxxx\",\"secret-key\":\"xxxxxxxx\"}', 'single-dynamic-input', NULL, 15, 1, 'core', NULL, NULL, '2021-10-09 21:20:25', NULL, NULL, 0, 'JSONString', '{\n    \"baseUrl\": {\n      \"label\": \"baseUrl\",\n      \"name\": \"base-url\",\n      \"type\": \"input\",\n      \"value\": \"https://gin.vue.admin\"\n    },\n    \"bucket\": {\n      \"label\": \"bucket\",\n      \"name\": \"bucket\",\n      \"type\": \"input\",\n      \"value\": \"xxxxx-10005608\"\n    },\n    \"pathPrefix\": {\n      \"label\": \"pathPrefix\",\n      \"name\": \"path-prefix\",\n      \"type\": \"input\",\n      \"value\": \"gin-myboot\"\n    },\n    \"region\": {\n      \"label\": \"region\",\n      \"name\": \"region\",\n      \"type\": \"input\",\n      \"value\": \"ap-shanghai\"\n    },\n    \"secretId\": {\n      \"label\": \"secretId\",\n      \"name\": \"secret-id\",\n      \"type\": \"input\",\n      \"value\": \"xxxxxxxx\"\n    },\n    \"secretKey\": {\n      \"label\": \"secretKey\",\n      \"name\": \"secret-key\",\n      \"type\": \"input\",\n      \"value\": \"xxxxxxxx\"\n    }\n  }', NULL, NULL, NULL);
INSERT INTO `sys_configs` VALUES (58, 'dict', '动态测试', 'dynamic', '[{\"label\":\"未支付\",\"name\":\"order_nopay\",\"value\":\"1\"},{\"label\":\"已支付\",\"name\":\"order_pay\",\"value\":\"2\"}]', 'dynamic-input', NULL, 999999, 1, 'dict', NULL, NULL, '2021-10-09 21:20:25', NULL, NULL, 0, 'JSONString', '[{\n		\"label\": \"中文名\",\n		\"name\": \"label\",\n		\"type\": \"input\",\n		\"value\": \"\"\n	},\n	{\n		\"label\": \"英文名\",\n		\"name\": \"name\",\n		\"type\": \"input\",\n		\"value\": \"\"\n	},\n	{\n		\"label\": \"值\",\n		\"name\": \"value\",\n		\"type\": \"input\",\n		\"value\": \"\"\n	}\n]', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_department_headers
-- ----------------------------
DROP TABLE IF EXISTS `sys_department_headers`;
CREATE TABLE `sys_department_headers` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `department_id` bigint unsigned DEFAULT NULL,
  `type` tinyint(1) DEFAULT NULL,
  `user_id` bigint unsigned DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of sys_department_headers
-- ----------------------------
BEGIN;
INSERT INTO `sys_department_headers` VALUES (1, 3, 0, 7, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_department_headers` VALUES (2, 2, 0, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_department_headers` VALUES (3, 1, 1, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_department_headers` VALUES (10, 7, 1, 4, '2021-10-19 10:53:22.165000', 'admin', NULL, '2021-10-19 10:53:22.165000', 'admin');
INSERT INTO `sys_department_headers` VALUES (11, 12, 0, 1, '2021-10-19 13:50:18.718000', 'admin', NULL, '2021-10-19 13:50:18.718000', 'admin');
INSERT INTO `sys_department_headers` VALUES (12, 12, 1, 7, '2021-10-19 13:50:18.905000', 'admin', NULL, '2021-10-19 13:50:18.905000', 'admin');
COMMIT;

-- ----------------------------
-- Table structure for sys_departments
-- ----------------------------
DROP TABLE IF EXISTS `sys_departments`;
CREATE TABLE `sys_departments` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` bigint unsigned NOT NULL,
  `sort_order` decimal(10,2) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `is_parent` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of sys_departments
-- ----------------------------
BEGIN;
INSERT INTO `sys_departments` VALUES (1, 0, 1.00, 1, '总部', b'1', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_departments` VALUES (2, 1, 1.00, 1, '技术部', b'1', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_departments` VALUES (3, 1, 1.00, 1, '研发中心', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_departments` VALUES (4, 1, 2.00, 1, '大数据', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_departments` VALUES (5, 1, 3.00, 1, '人工智障', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_departments` VALUES (6, 0, 2.00, 1, '成都分部', b'1', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_departments` VALUES (7, 1, 2.00, 1, 'Vue', b'0', NULL, NULL, NULL, '2021-10-19 10:53:21.899000', NULL);
INSERT INTO `sys_departments` VALUES (8, 1, 1.00, 1, 'JAVA', b'0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_departments` VALUES (9, 0, 3.00, 1, '人事部', b'1', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_departments` VALUES (10, 1, 1.00, 1, '游客', b'0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_departments` VALUES (11, 1, 2.00, 1, 'VIP', b'0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_departments` VALUES (12, 6, 99999.00, 1, 'Java', b'0', '2021-10-19 10:58:20.955000', NULL, NULL, '2021-10-19 13:50:18.600000', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_details
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_details`;
CREATE TABLE `sys_dict_details` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted_at` datetime DEFAULT NULL,
  `label` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '展示值',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典值',
  `status` tinyint(1) DEFAULT NULL COMMENT '启用状态',
  `sort_order` bigint DEFAULT NULL COMMENT '排序标记',
  `dict_id` bigint unsigned DEFAULT NULL COMMENT '关联标记',
  `name` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `color` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '颜色',
  `created_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `updated_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_sys_dict_details_deleted_at` (`deleted_at`),
  KEY `FKg96etwemxy5hymogl0bttlrbu` (`dict_id`),
  CONSTRAINT `FKg96etwemxy5hymogl0bttlrbu` FOREIGN KEY (`dict_id`) REFERENCES `sys_dicts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_dict_details
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_details` VALUES (28, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, '男', '1', 1, 1, 7, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (29, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, '女', '2', 1, 1, 7, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (40, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, '本地', 'local', 1, 1, 8, NULL, '#c72323', NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (41, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, '七牛', 'qiniu', 1, 1, 8, NULL, '#5426c0', NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (42, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, '腾讯云', 'tencent-cos', 1, 1, 8, NULL, '#15cbc8', NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (43, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, '阿里云', 'aliyun-oss', 1, 1, 8, NULL, '#16a503', NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (46, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, '女', '2', 1, 1, 1, NULL, '', NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (50, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, '系统消息', '0', 1, 1, 9, NULL, '#d95959', NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (51, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, '普通消息', '1', 1, 1, 9, NULL, '#1048b2', NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (52, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, '订单消息', '2', 1, 1, 9, NULL, '#1edccf', NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (53, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'int', 'int', 1, 1, 10, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (54, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'bigint', 'bigint', 1, 1, 10, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (55, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'varchar', 'varchar', 1, 1, 10, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (56, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'datetime', 'datetime', 1, 1, 10, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (57, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'decimal', 'decimal', 1, 1, 10, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (58, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'char', 'char', 1, 1, 10, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (59, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'tinyint', 'tinyint', 1, 1, 10, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (60, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'text', 'text', 1, 1, 10, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (61, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'longtext', 'longtext', 1, 1, 10, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (62, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'String', 'String', 1, 1, 11, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (63, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'Integer', 'Integer', 1, 1, 11, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (64, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'Long', 'Long', 1, 1, 11, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (65, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'Float', 'Float', 1, 1, 11, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (66, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'Double', 'Double', 1, 1, 11, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (67, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'Date', 'Date', 1, 1, 11, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (68, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'BigDecimal', 'BigDecimal', 1, 1, 11, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_details` VALUES (69, '2021-09-16 14:57:52', '2021-09-16 14:57:52', NULL, 'Boolean', 'Boolean', 1, 1, 11, NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_dicts
-- ----------------------------
DROP TABLE IF EXISTS `sys_dicts`;
CREATE TABLE `sys_dicts` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted_at` datetime DEFAULT NULL,
  `name` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典名（中）',
  `type` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典名（英）',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态',
  `description` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `sort_order` decimal(10,2) DEFAULT NULL COMMENT '排序',
  `created_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `updated_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_sys_dicts_deleted_at` (`deleted_at`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_dicts
-- ----------------------------
BEGIN;
INSERT INTO `sys_dicts` VALUES (1, '2021-09-20 10:29:55', '2021-09-20 10:29:55', NULL, '性别', 'sex', 1, NULL, 99.00, NULL, NULL);
INSERT INTO `sys_dicts` VALUES (7, '2021-09-20 10:29:55', '2021-09-20 10:29:55', NULL, '性别', 'xing_bie', 1, '', 99.00, NULL, NULL);
INSERT INTO `sys_dicts` VALUES (8, '2021-09-20 10:29:55', '2021-09-20 10:29:55', NULL, '文件上传类型', 'uploadType', 1, '', 99.00, NULL, NULL);
INSERT INTO `sys_dicts` VALUES (9, '2021-09-20 10:29:55', '2021-09-20 10:29:55', NULL, '系统消息类型', 'messageType', 1, '', 99.00, NULL, NULL);
INSERT INTO `sys_dicts` VALUES (10, '2021-09-20 10:29:55', '2021-09-20 10:29:55', NULL, '数据库字段类型', 'dataType', 1, '', 99.00, NULL, NULL);
INSERT INTO `sys_dicts` VALUES (11, '2021-09-20 10:29:55', '2021-09-20 10:29:55', NULL, '字段类型', 'fieldType', 1, '', 99.00, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_jobs
-- ----------------------------
DROP TABLE IF EXISTS `sys_jobs`;
CREATE TABLE `sys_jobs` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `cron_expression` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `job_class_name` varchar(255) DEFAULT NULL,
  `parameter` varchar(255) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of sys_jobs
-- ----------------------------
BEGIN;
INSERT INTO `sys_jobs` VALUES (1, '1111', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2021-11-01 19:15:00.566000', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_logs
-- ----------------------------
DROP TABLE IF EXISTS `sys_logs`;
CREATE TABLE `sys_logs` (
  `id` bigint unsigned NOT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `ip_info` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `body` longtext CHARACTER SET utf8 COLLATE utf8_general_ci,
  `method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `request_url` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `log_type` tinyint(1) DEFAULT NULL,
  `device` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `latency` int DEFAULT NULL,
  `agent` varchar(255) DEFAULT NULL,
  `error_message` varchar(255) DEFAULT NULL,
  `resp` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of sys_logs
-- ----------------------------
BEGIN;
INSERT INTO `sys_logs` VALUES (1428171590318821376, '127.0.0.1', '未知', '登录系统', '{\"password\":\"你是看不见我的\",\"code\":\"cbcq\",\"saveLogin\":\"true\",\"captchaId\":\"2a7808a6746d4eec9a7c158dfb558701\",\"username\":\"admin\"}', 'POST', '/myboot/login', 'admin', 1, 'Chrome 92.0.4515.159 | Mac OSX | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1428174310647926784, '127.0.0.1', '未知', '登录系统', '{\"password\":\"你是看不见我的\",\"code\":\"pa7v\",\"saveLogin\":\"true\",\"captchaId\":\"e0163c56e4a042f7ad97235c14bd8c04\",\"username\":\"admin\"}', 'POST', '/myboot/login', 'admin', 1, 'Chrome 92.0.4515.159 | Mac OSX | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1428175420720812032, '127.0.0.1', '未知', '登录系统', '{\"password\":\"你是看不见我的\",\"code\":\"tj4w\",\"saveLogin\":\"true\",\"captchaId\":\"0fb54d1ce97b421e9b1cc71cf15e5418\",\"username\":\"admin\"}', 'POST', '/myboot/login', 'admin', 1, 'Chrome 92.0.4515.159 | Mac OSX | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1428178048469045248, '127.0.0.1', '未知', '登录系统', '{\"password\":\"你是看不见我的\",\"code\":\"send\",\"saveLogin\":\"true\",\"captchaId\":\"708055e7fcf940c5b44100aa23e60406\",\"username\":\"admin\"}', 'POST', '/myboot/login', 'admin', 1, 'Chrome 92.0.4515.159 | Mac OSX | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1432298989893586944, '127.0.0.1', '未知', '登录系统', '{\"password\":\"你是看不见我的\",\"code\":\"xh8n\",\"saveLogin\":\"true\",\"captchaId\":\"b8ceec91c9204829b959f9cd70e5ba6c\",\"username\":\"admin\"}', 'POST', '/myboot/login', 'admin', 1, 'Chrome 92.0.4515.159 | Mac OSX | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1447103406727630848, '127.0.0.1', '未知', '用户登录', '{}', 'POST', '/api/v1/auth/login', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1447111535720796160, '127.0.0.1', '未知', '用户登录', '{}', 'POST', '/api/v1/auth/login', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1447116417316032512, '127.0.0.1', '未知', '用户登录', '{}', 'POST', '/api/v1/auth/login', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1447117657622056960, '127.0.0.1', '未知', '用户登录', '{}', 'POST', '/api/v1/auth/login', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1447118464719392768, '127.0.0.1', '未知', '用户登录', '{}', 'POST', '/api/v1/auth/login', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1447122539745775616, '127.0.0.1', '未知', '用户登录', '{}', 'POST', '/api/v1/auth/login', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1448309048670818304, '127.0.0.1', '未知', '用户登录', '{}', 'POST', '/api/v1/auth/login', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1448311434730672128, '127.0.0.1', '未知', '用户登录', '{}', 'POST', '/api/v1/auth/login', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1448327792176533504, '127.0.0.1', '未知', '登录系统', '{}', 'POST', '/api/v1/common/auth/login', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1448329207905128448, '127.0.0.1', '未知', '登录系统', '{}', 'POST', '/api/v1/common/auth/login', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1448474020218540032, '127.0.0.1', '未知', '登录系统', '{}', 'POST', '/api/v1/common/auth/login', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1448551923673337856, '127.0.0.1', '未知', '用户登录', '{}', 'POST', '/api/v1/auth/login', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1448860278828699648, '127.0.0.1', '未知', '用户登录', '', 'POST', 'http://localhost:8889/api/v1/auth/login', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', '2021-10-15 11:56:24.466000', NULL, NULL, '2021-10-15 11:56:24.466000', NULL, 1065, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1448860506273222656, '127.0.0.1', '未知', '用户登录', '', 'POST', 'http://localhost:8889/api/v1/auth/login', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', '2021-10-15 11:57:18.268000', NULL, NULL, '2021-10-15 11:57:18.268000', NULL, 755, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1448860815007551488, '127.0.0.1', '未知', '用户登录', '', 'POST', '/api/v1/auth/login', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', '2021-10-15 11:58:31.971000', NULL, NULL, '2021-10-15 11:58:31.971000', NULL, 940, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1448869172216991744, '127.0.0.1', '未知', '用户登录', '{\"v\":\"1\"}', 'POST', '/api/v1/auth/login?v=1', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', '2021-10-15 12:31:44.380000', NULL, NULL, '2021-10-15 12:31:44.380000', NULL, 725, 'PostmanRuntime/7.28.4', NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1448876982719877120, '127.0.0.1', '未知', '用户登录', '{    \"password\": \"123456\",    \"captcha\": \"b\",    \"username\": \"admin\",    \"captchaId\": \"a38835a6ebba4abd84d3a3b79ac85457\"}', 'POST', '/api/v1/auth/login?v=1', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', '2021-10-15 13:02:48.066000', NULL, NULL, '2021-10-15 13:02:48.066000', NULL, 2988, 'PostmanRuntime/7.28.4', NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1448878068805537792, '127.0.0.1', '未知', '用户登录', '{    \"password\": \"123456\",    \"captcha\": \"b\",    \"username\": \"admin\",    \"captchaId\": \"a38835a6ebba4abd84d3a3b79ac85457\"}', 'POST', '/api/v1/auth/login?v=1', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', '2021-10-15 13:07:05.320000', NULL, NULL, '2021-10-15 13:07:05.320000', NULL, 1080, 'PostmanRuntime/7.28.4', NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1448885527506980864, '127.0.0.1', '未知', '用户登录', '{    \"password\": \"123456\",    \"captcha\": \"b\",    \"username\": \"admin\",    \"captchaId\": \"a38835a6ebba4abd84d3a3b79ac85457\"}', 'POST', '/api/v1/auth/login?v=1', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', '2021-10-15 13:36:45.682000', NULL, NULL, '2021-10-15 13:36:45.682000', NULL, 4424, 'PostmanRuntime/7.28.4', NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1448886700280516608, '127.0.0.1', '未知', '用户登录', '{    \"password\": \"123456\",    \"captcha\": \"b\",    \"username\": \"admin\",    \"captchaId\": \"a38835a6ebba4abd84d3a3b79ac85457\"}', 'POST', '/api/v1/auth/login?v=1', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', '2021-10-15 13:41:23.312000', NULL, NULL, '2021-10-15 13:41:23.312000', NULL, 1005, 'PostmanRuntime/7.28.4', NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1448932115688329216, '127.0.0.1', '未知', '用户登录', '{    \"password\": \"123456\",    \"captcha\": \"b\",    \"username\": \"admin\",    \"captchaId\": \"a38835a6ebba4abd84d3a3b79ac85457\"}', 'POST', '/api/v1/auth/login?v=1', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', '2021-10-15 16:41:53.086000', NULL, NULL, NULL, NULL, 3516, 'PostmanRuntime/7.28.4', NULL, NULL, NULL, NULL);
INSERT INTO `sys_logs` VALUES (1448936257844547584, '127.0.0.1', '未知', '用户登录', '{    \"password\": \"123456\",    \"captcha\": \"b\",    \"username\": \"admin\",    \"captchaId\": \"a38835a6ebba4abd84d3a3b79ac85457\"}', 'POST', '/api/v1/auth/login?v=1', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', '2021-10-15 16:58:20.304000', NULL, NULL, NULL, NULL, 3660, 'PostmanRuntime/7.28.4', NULL, 'org.springframework.security.web.util.OnCommittedResponseWrapper$SaveContextServletOutputStream[delegate=org.apache.catalina.connector.CoyoteOutputStream@50fbe9aa]', '1', '200');
INSERT INTO `sys_logs` VALUES (1448944839247925248, '127.0.0.1', '未知', '用户登录', '{    \"password\": \"123456\",    \"captcha\": \"b\",    \"username\": \"admin\",    \"captchaId\": \"a38835a6ebba4abd84d3a3b79ac85457\"}', 'POST', '/api/v1/auth/login?v=1', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', '2021-10-15 17:32:25.605000', NULL, NULL, NULL, NULL, 2189, 'PostmanRuntime/7.28.4', NULL, '', '1', '200');
INSERT INTO `sys_logs` VALUES (1448955889338945536, '127.0.0.1', '未知', '用户登录', '{    \"password\": \"123456\",    \"captcha\": \"b\",    \"username\": \"admin\",    \"captchaId\": \"a38835a6ebba4abd84d3a3b79ac85457\"}', 'POST', '/api/v1/auth/login?v=1', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', '2021-10-15 18:16:20.056000', NULL, NULL, NULL, NULL, 3633, 'PostmanRuntime/7.28.4', NULL, '{\"code\":200,\"message\":\"登录成功\",\"result\":{\"expiresAt\":0,\"token\":\"e05221b4414646259d3fa5aaeabe3f6f\"},\"success\":true,\"timestamp\":1634292977384}', '1', '200');
INSERT INTO `sys_logs` VALUES (1449403135620681728, '127.0.0.1', '未知', '登录系统', NULL, 'POST', '/api/v1/common/auth/login', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', '2021-10-16 23:53:32.779000', NULL, NULL, NULL, NULL, 1673, 'PostmanRuntime/7.28.4', NULL, NULL, '1', '200');
INSERT INTO `sys_logs` VALUES (1449404194162348032, '127.0.0.1', '未知', '登录系统', NULL, 'POST', '/api/v1/common/auth/login', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', '2021-10-16 23:57:43.284000', NULL, NULL, NULL, NULL, 135, 'PostmanRuntime/7.28.4', NULL, NULL, '1', '200');
INSERT INTO `sys_logs` VALUES (1449562194411917312, '127.0.0.1', '未知', '登录系统', NULL, 'POST', '/api/v1/common/auth/login', 'admin', 1, 'Unknown null | Unknown Unknown | PC端', '2021-10-17 10:25:34.778000', NULL, NULL, NULL, NULL, 1616, 'PostmanRuntime/7.28.4', NULL, NULL, '1', '200');
INSERT INTO `sys_logs` VALUES (1449563678591553536, '127.0.0.1', '未知', '登录系统', NULL, 'POST', '/api/v1/common/auth/login', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', '2021-10-17 10:31:28.574000', NULL, NULL, NULL, NULL, 1008, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36', NULL, NULL, '1', '200');
INSERT INTO `sys_logs` VALUES (1449568930074071040, '127.0.0.1', '未知', '登录系统', NULL, 'POST', '/api/v1/common/auth/login', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', '2021-10-17 10:52:19.574000', NULL, NULL, NULL, NULL, 393, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36', NULL, NULL, '1', '200');
INSERT INTO `sys_logs` VALUES (1449652275449565184, '127.0.0.1', '未知', '登录系统', NULL, 'POST', '/api/v1/common/auth/login', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', '2021-10-17 16:23:31.175000', NULL, NULL, NULL, NULL, 510, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36', NULL, NULL, '1', '200');
INSERT INTO `sys_logs` VALUES (1449714587967557632, '127.0.0.1', '未知', '登录系统', NULL, 'POST', '/api/v1/common/auth/login', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', '2021-10-17 20:31:07.609000', NULL, NULL, NULL, NULL, 829, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36', NULL, NULL, '1', '200');
INSERT INTO `sys_logs` VALUES (1449715625000833024, '127.0.0.1', '未知', '登录系统', NULL, 'POST', '/api/v1/common/auth/login', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', '2021-10-17 20:35:14.155000', NULL, NULL, NULL, NULL, 110, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36', NULL, NULL, '1', '200');
INSERT INTO `sys_logs` VALUES (1449716097342377984, '127.0.0.1', '未知', '登录系统', NULL, 'POST', '/api/v1/common/auth/login', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', '2021-10-17 20:37:06.780000', NULL, NULL, NULL, NULL, 188, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36', NULL, NULL, '1', '200');
INSERT INTO `sys_logs` VALUES (1449992445839413248, '127.0.0.1', '未知', '用户登录', '{\"mobile\":\"18888888888\",\"smsCode\":\"2510\",\"captchaId\":\"ca0ad56728ef41fcbe1f6a129a3550b4\"}', 'POST', '/api/v1/common/auth/login1', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', '2021-10-18 14:55:14.015000', NULL, NULL, NULL, NULL, 1066, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36', NULL, '{\"code\":200,\"message\":\"登录成功\",\"result\":{\"expiresAt\":0,\"token\":\"eba2649c51c54e1a828c567b851f3789\"},\"success\":true,\"timestamp\":1634540112949}', '1', '200');
INSERT INTO `sys_logs` VALUES (1449994499639087104, '127.0.0.1', '未知', '用户登录', '{\"password\":\"123456\",\"code\":\"4a5x\",\"username\":\"admin\",\"captchaId\":\"4a988f76756540d4b9a7b8b788004c28\"}', 'POST', '/api/v1/common/auth/login1', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', '2021-10-18 15:03:23.078000', NULL, NULL, NULL, NULL, 483, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36', NULL, '{\"code\":200,\"message\":\"登录成功\",\"result\":{\"expiresAt\":0,\"token\":\"5ade69f3c7c341b6aaa17d866188f32c\"},\"success\":true,\"timestamp\":1634540602950}', '1', '200');
INSERT INTO `sys_logs` VALUES (1450040588249468928, '127.0.0.1', '未知', '用户登录', '{\"password\":\"123456\",\"code\":\"n8y5\",\"username\":\"admin\",\"captchaId\":\"c115f56e1ce147c6b7de244901604ed3\"}', 'POST', '/api/v1/common/auth/login1', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', '2021-10-18 18:06:31.958000', NULL, NULL, NULL, NULL, 956, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36', NULL, '{\"code\":200,\"message\":\"登录成功\",\"result\":{\"expiresAt\":0,\"token\":\"b62a5c964f0c4f0a95971c1264cfe027\"},\"success\":true,\"timestamp\":1634551591115}', '1', '200');
INSERT INTO `sys_logs` VALUES (1450734078767796224, '127.0.0.1', '未知', '用户登录', '{\"password\":\"123456\",\"code\":\"4288\",\"username\":\"admin\",\"captchaId\":\"2e4824ef858341eeabd75bdc287b21e9\"}', 'POST', '/api/v1/common/auth/login1', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', '2021-10-20 16:02:13.011000', NULL, NULL, NULL, NULL, 953, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36', NULL, '{\"code\":200,\"message\":\"登录成功\",\"result\":{\"expiresAt\":0,\"token\":\"729e934c6ef746efa19ec4f7f386a77a\"},\"success\":true,\"timestamp\":1634716932139}', '1', '200');
INSERT INTO `sys_logs` VALUES (1453364954316017664, '127.0.0.1', '未知', '用户登录', '{\"password\":\"123456\",\"code\":\"7588\",\"username\":\"admin\",\"captchaId\":\"69aa41d55a8243f0a7547bc8135e0ecc\"}', 'POST', '/api/v1/common/auth/login', 'admin', 1, 'Chrome 94.0.4606.71 | Mac OSX | PC端', '2021-10-27 22:16:22.535000', NULL, NULL, NULL, NULL, 657, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36', NULL, '{\"code\":200,\"message\":\"登录成功\",\"result\":{\"expiresAt\":0,\"token\":\"78d1eca0457a4b65a5fe57a3c68ca702\"},\"success\":true,\"timestamp\":1635344181851}', '1', '200');
COMMIT;

-- ----------------------------
-- Table structure for sys_messages
-- ----------------------------
DROP TABLE IF EXISTS `sys_messages`;
CREATE TABLE `sys_messages` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `type` tinyint unsigned DEFAULT '1' COMMENT '消息类型',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标题',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消息内容',
  `created_at` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `user_id` bigint unsigned DEFAULT NULL COMMENT '接受用户ID',
  `from_user_id` bigint DEFAULT NULL COMMENT '发送用户ID',
  `status` tinyint(1) DEFAULT '0' COMMENT '是否已读',
  `view_time` datetime DEFAULT NULL COMMENT '阅读时间',
  `is_backend` tinyint(1) DEFAULT '0' COMMENT '后台消息',
  `data_id` int unsigned DEFAULT NULL COMMENT '数据ID',
  `data_type` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据类型',
  `data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '数据',
  `created_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `session_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `icon` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `image` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户消息';

-- ----------------------------
-- Records of sys_messages
-- ----------------------------
BEGIN;
INSERT INTO `sys_messages` VALUES (3, 1, '面试', '你推荐的 曲妮妮 已通过第三轮面试', '2020-04-09 13:27:42', '2021-10-27 22:34:14', 1, 2, 1, '2020-04-10 23:33:04', 1, NULL, 'msg', '11', NULL, NULL, NULL, NULL, '/assets/images/message.png', NULL);
INSERT INTO `sys_messages` VALUES (5, 1, '周报', '你收到了 14 份新周报', '2020-04-09 13:27:42', '2021-10-27 22:34:49', 1, 0, 1, NULL, 0, NULL, 'msg', '11', NULL, NULL, NULL, NULL, '/assets/images/message.png', NULL);
INSERT INTO `sys_messages` VALUES (6, 1, '系统提示', '你收到了 14 份新周报', '2020-04-09 13:27:42', '2021-10-27 22:35:01', 1, 0, 1, NULL, 0, NULL, 'msg', NULL, NULL, NULL, NULL, NULL, '/assets/images/message.png', NULL);
INSERT INTO `sys_messages` VALUES (7, 0, '消息内容', '消息内容', '2021-11-02 14:23:09', NULL, 3, 0, 0, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/assets/images/message.png', NULL);
INSERT INTO `sys_messages` VALUES (8, 0, '消息内容', '消息内容', '2021-11-02 14:23:09', NULL, 2, 0, 0, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/assets/images/message.png', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_permissions
-- ----------------------------
DROP TABLE IF EXISTS `sys_permissions`;
CREATE TABLE `sys_permissions` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `level` bigint unsigned DEFAULT NULL,
  `parent_id` int unsigned DEFAULT '0' COMMENT '父菜单ID',
  `path` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路由path',
  `name` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路由name',
  `hidden` tinyint(1) DEFAULT '0' COMMENT '是否在列表隐藏',
  `component` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对应前端文件路径',
  `keep_alive` tinyint(1) DEFAULT '0' COMMENT '附加属性',
  `default_menu` tinyint(1) DEFAULT NULL COMMENT '附加属性',
  `title` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '附加属性',
  `icon` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'menu' COMMENT '附加属性',
  `status` tinyint unsigned DEFAULT '1' COMMENT '状态',
  `redirect` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '跳转地址',
  `api` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'API权限',
  `sort_order` decimal(10,2) DEFAULT NULL COMMENT '排序',
  `is_button` tinyint(1) DEFAULT '0' COMMENT '是否权限按钮',
  `updated_by` varchar(125) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_by` varchar(125) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_parent` tinyint(1) DEFAULT '0' COMMENT '是否为父节点',
  `type` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_sys_permissions_deleted_at` (`deleted_at`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_permissions
-- ----------------------------
BEGIN;
INSERT INTO `sys_permissions` VALUES (1, '2021-08-22 09:34:27', '2021-10-11 13:40:21', NULL, 0, 0, '/dashboard', 'dashboard', 0, 'RouteView', 1, 0, '仪表盘', 'dashboard', 1, '/dashboard/workplace', '', 1.00, 0, 'admin', NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (2, '2021-08-22 09:34:27', '2021-08-22 09:34:27', NULL, 0, 1, '/dashboard/workplace', 'workplace', 0, 'Workplace', 1, 0, '工作台', 'dashboard', 1, NULL, NULL, 1.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (3, '2021-08-22 09:34:27', '2021-08-22 09:34:27', NULL, 0, 1, '/dashboard/monitor', 'monitor', 0, 'modules/dashboard/views/Monitor.vue', 1, 0, '监控台', 'menu', 1, NULL, NULL, 1.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (4, '2021-08-22 09:34:27', '2021-08-22 09:34:27', NULL, 0, 1, 'https://docs.spring.io/spring-data/jpa/docs/', 'redirect', 0, '', 1, 0, 'spingboot', 'menu', 1, NULL, NULL, 1.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (5, '2021-08-22 09:34:27', '2021-08-22 09:34:27', NULL, 0, 1, 'https://mp.baomidou.com/guide', 'gorm', 0, 'IFrameView', 1, 0, 'mybatisplus', 'menu', 1, NULL, NULL, 1.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (6, '2021-08-22 09:34:27', '2021-10-18 19:57:30', NULL, 0, 0, 'system', 'system', 0, 'RouteView', 1, 0, '系统设置', 'setting', 1, NULL, NULL, 2.00, 0, NULL, NULL, 1, 1);
INSERT INTO `sys_permissions` VALUES (7, '2021-08-22 09:34:27', '2021-08-22 09:34:27', NULL, 0, 6, 'permission', 'permission', 0, 'modules/system/views/permission/Index.vue', 1, NULL, '权限菜单', 'lock', 1, NULL, NULL, 2.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (8, '2021-08-22 09:34:27', '2021-08-22 09:34:27', NULL, 0, 6, 'admin', 'admin', 0, 'modules/system/views/admin/Index.vue', 1, NULL, '系统管理员', 'user', 1, NULL, NULL, 2.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (9, '2021-08-22 09:34:27', '2021-08-22 09:34:27', NULL, 0, 6, 'role', 'role', 0, 'modules/system/views/role/Index.vue', 1, NULL, '系统角色', 'solution', 1, NULL, NULL, 2.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (10, '2021-08-22 09:34:27', '2021-08-22 09:34:27', NULL, 0, 6, 'setting', 'config', 0, 'modules/system/views/config/Index.vue', 1, NULL, '系统配置', 'appstore', 1, NULL, '', 2.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (11, NULL, '2021-10-11 18:16:15', NULL, 0, 6, 'user', 'user', 0, 'modules/system/views/user/Index.vue', 1, NULL, '用户列表', 'team', 1, NULL, '', 2.00, 0, 'admin', NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (14, '2021-09-15 10:01:14', '2021-09-15 11:50:03', NULL, 0, 6, 'router', 'router', 0, 'modules/system/views/api/Index.vue', 1, NULL, 'API管理', 'branches', 1, '', '', 99999.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (15, '2021-09-15 21:45:30', '2021-09-15 21:45:30', NULL, 0, 6, 'dict', 'dict', 0, 'modules/system/views/dict/Index.vue', 1, NULL, '字典管理', 'menu', 1, '', '', 99999.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (16, '2021-09-19 16:50:54', '2021-10-11 19:05:34', NULL, 0, 0, 'storage', 'storage', 0, 'RouteView', 1, NULL, '文件系统', 'folder', 1, '', '', 99999.00, 0, NULL, NULL, 1, 1);
INSERT INTO `sys_permissions` VALUES (17, '2021-09-19 16:52:37', '2021-09-19 16:52:37', NULL, 0, 16, 'filesystem', 'filesystem', 0, 'modules/storage/views/filesystem/Index.vue', 1, NULL, '文件列表', 'file', 1, '', '', 99999.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (18, '2021-09-21 10:13:17', '2021-09-21 10:13:17', NULL, 0, 0, 'account', 'account', 0, 'RouteView', 1, NULL, '个人中心', 'user', 1, '', '', 99999.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (19, '2021-09-21 10:16:11', '2021-09-21 10:16:11', NULL, 0, 18, 'profile', 'profile', 0, 'modules/system/views/account/center/Index.vue', 1, NULL, '个人资料', 'snippets', 1, '', '', 99999.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (20, '2021-09-21 10:17:37', '2021-09-21 10:27:48', NULL, 0, 18, 'settings', 'settings', 0, 'modules/system/views/account/settings/Index.vue', 1, NULL, '个人设置', 'menu', 1, '', '', 99999.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (21, '2021-09-21 10:29:23', '2021-09-21 10:29:23', NULL, 0, 20, 'base', 'accountBaseSettings', 0, 'modules/system/views/account/settings/BaseSettings.vue', 1, NULL, '基本资料', 'form', 1, '', '', 99999.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (22, '2021-09-21 10:30:10', '2021-09-21 10:30:10', NULL, 0, 20, 'password', 'accountPassword', 0, 'modules/system/views/account/settings/Password.vue', 1, NULL, '密码修改', 'unlock', 1, '', '', 99999.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (23, '2021-10-08 11:31:44', '2021-10-08 11:44:59', NULL, 0, 6, 'log', 'log', 0, 'modules/system/views/log/Index.vue', 1, NULL, '操作日志', 'menu', 1, '', '', 99999.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (53, '2021-10-11 18:16:08', NULL, NULL, 1, 11, NULL, 'user_create', 0, NULL, 0, NULL, '新增', 'menu', 1, NULL, NULL, 1.00, 1, NULL, NULL, 0, 0);
INSERT INTO `sys_permissions` VALUES (54, '2021-10-11 18:16:08', NULL, NULL, 1, 11, NULL, 'user_delete', 0, NULL, 0, NULL, '删除', 'menu', 1, NULL, NULL, 2.00, 1, NULL, NULL, 0, 0);
INSERT INTO `sys_permissions` VALUES (55, '2021-10-11 18:16:08', NULL, NULL, 1, 11, '/myboot/log/getAllByPage*', 'user_update', 0, NULL, 0, NULL, '修改', 'menu', 1, '', '', 3.00, 1, NULL, NULL, 0, 0);
INSERT INTO `sys_permissions` VALUES (66, '2021-10-18 19:57:30', '2021-10-18 20:00:44', NULL, 1, 6, 'department', 'department', 0, 'modules/system/views/department/Index.vue', 1, NULL, '部门管理', 'menu', 1, '', '', 99999.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (67, '2021-10-20 15:06:39', '2021-10-27 22:23:09', NULL, 1, 6, 'message', 'message', 0, 'modules/system/views/message/Index.vue', 1, NULL, '消息管理', 'message', 1, '', '', 99999.00, 0, NULL, NULL, 0, 1);
INSERT INTO `sys_permissions` VALUES (68, '2021-10-28 10:24:08', NULL, NULL, 1, 6, 'cache', 'cache', 0, 'modules/system/views/redis/Index.vue', 1, NULL, '缓存管理', 'cloud-server', 1, '', '', 99999.00, 0, NULL, NULL, 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_departments
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_departments`;
CREATE TABLE `sys_role_departments` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `department_id` bigint unsigned DEFAULT NULL,
  `role_id` bigint unsigned DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `department_id` (`department_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of sys_role_departments
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_departments` VALUES (34, 1, 4, '2021-10-19 19:54:28.742000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_departments` VALUES (35, 2, 4, '2021-10-19 19:54:28.744000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_departments` VALUES (39, 1, 1, '2021-10-28 20:13:41.794000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_departments` VALUES (40, 2, 1, '2021-10-28 20:13:41.797000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_departments` VALUES (41, 3, 1, '2021-10-28 20:13:41.798000', NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_permissions
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permissions`;
CREATE TABLE `sys_role_permissions` (
  `permission_id` int unsigned DEFAULT NULL,
  `role_id` int unsigned DEFAULT NULL,
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_permission_id` (`permission_id`,`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=198 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_role_permissions
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_permissions` VALUES (1, 3, 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (2, 0, 4, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (2, 3, 6, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (3, 3, 9, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (4, 3, 14, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (5, 3, 17, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (1, 4, 148, '2021-10-19 19:54:28.525000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (2, 4, 149, '2021-10-19 19:54:28.526000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (3, 4, 150, '2021-10-19 19:54:28.527000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (1, 1, 174, '2021-10-28 20:13:41.250000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (2, 1, 175, '2021-10-28 20:13:41.357000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (3, 1, 176, '2021-10-28 20:13:41.359000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (4, 1, 177, '2021-10-28 20:13:41.360000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (5, 1, 178, '2021-10-28 20:13:41.362000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (6, 1, 179, '2021-10-28 20:13:41.362000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (7, 1, 180, '2021-10-28 20:13:41.363000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (8, 1, 181, '2021-10-28 20:13:41.363000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (9, 1, 182, '2021-10-28 20:13:41.364000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (10, 1, 183, '2021-10-28 20:13:41.365000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (11, 1, 184, '2021-10-28 20:13:41.366000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (14, 1, 185, '2021-10-28 20:13:41.367000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (15, 1, 186, '2021-10-28 20:13:41.368000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (16, 1, 187, '2021-10-28 20:13:41.369000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (17, 1, 188, '2021-10-28 20:13:41.372000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (18, 1, 189, '2021-10-28 20:13:41.393000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (19, 1, 190, '2021-10-28 20:13:41.396000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (20, 1, 191, '2021-10-28 20:13:41.398000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (21, 1, 192, '2021-10-28 20:13:41.414000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (22, 1, 193, '2021-10-28 20:13:41.416000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (23, 1, 194, '2021-10-28 20:13:41.416000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (66, 1, 195, '2021-10-28 20:13:41.417000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (67, 1, 196, '2021-10-28 20:13:41.417000', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_permissions` VALUES (68, 1, 197, '2021-10-28 20:13:41.418000', NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_roles
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles`;
CREATE TABLE `sys_roles` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` int DEFAULT NULL COMMENT '角色ID',
  `title` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `parent_id` int DEFAULT NULL COMMENT '父角色ID',
  `default_router` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'dashboard' COMMENT '默认菜单',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色名',
  `created_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `updated_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `data_type` int DEFAULT NULL,
  `default_role` bit(1) DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id` (`id`) USING BTREE,
  UNIQUE KEY `rrole_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_roles
-- ----------------------------
BEGIN;
INSERT INTO `sys_roles` VALUES (1, 888, '普通用户', '2021-08-22 09:34:27', '2021-10-28 20:13:41', NULL, 0, 'dashboard', 'admin', NULL, NULL, 0, b'0', NULL);
INSERT INTO `sys_roles` VALUES (2, 8881, '普通用户子角色', '2021-08-22 09:34:27', '2021-08-22 09:34:27', NULL, 1, 'dashboard', 'demo', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_roles` VALUES (3, 9528, '测试角色', '2021-08-22 09:34:27', '2021-09-14 17:40:06', NULL, 0, 'dashboard', 'test2', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_roles` VALUES (4, 999, '测试角色2', NULL, '2021-10-19 19:54:29', NULL, 0, 'dashboard', 'test87', NULL, NULL, 0, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_upload_files
-- ----------------------------
DROP TABLE IF EXISTS `sys_upload_files`;
CREATE TABLE `sys_upload_files` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `name` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件名',
  `url` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件地址',
  `tag` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件标签',
  `uuid` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '编号',
  `type` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类型:local=本地,qiniu=七牛,tencent-cos=腾讯云,aliyun-oss=阿里云',
  `user_id` bigint unsigned DEFAULT NULL COMMENT '上传用户',
  `created_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `updated_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `file_size` bigint DEFAULT NULL,
  `md5` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_exa_file_upload_and_downloads_deleted_at` (`deleted_at`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_upload_files
-- ----------------------------
BEGIN;
INSERT INTO `sys_upload_files` VALUES (1, '2021-08-22 09:34:27', '2021-08-22 09:34:27', NULL, '10.png', 'http://qmplusimg.henrongyi.top/gvalogo.png', 'png', '158787308910.png', 'local', 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_upload_files` VALUES (2, '2021-08-22 09:34:27', '2021-08-22 09:34:27', NULL, 'logo.png', 'http://qmplusimg.henrongyi.top/1576554439myAvatar.png', 'png', '1587973709logo.png', 'local', 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_upload_files` VALUES (3, '2021-09-18 20:34:06', '2021-09-18 20:34:06', NULL, '二乔.jpg', 'uploads/file/91392b98e6f99c3aeea243f433785e18_20210918203406.jpg', 'jpg', '91392b98e6f99c3aeea243f433785e18_20210918203406.jpg', 'local', 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_upload_files` VALUES (5, '2021-09-19 16:29:29', '2021-09-19 16:29:29', NULL, 'wallhaven-3z32j3.jpeg', 'uploads/file/087a464b2d91e6dc7ca000ae26c9db22_20210919162929.jpeg', 'jpeg', '087a464b2d91e6dc7ca000ae26c9db22_20210919162929.jpeg', 'local', 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_upload_files` VALUES (6, '2021-10-06 13:25:19', '2021-10-06 13:25:19', NULL, 'wallhaven-3z32j3.jpeg', 'uploads/file/087a464b2d91e6dc7ca000ae26c9db22_20211006132518.jpeg', 'jpeg', '087a464b2d91e6dc7ca000ae26c9db22_20211006132518.jpeg', 'local', 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_upload_files` VALUES (15, '2021-10-06 16:49:07', '2021-10-06 16:49:07', NULL, 'wallhaven-3z32j3.jpeg', 'uploads/file/087a464b2d91e6dc7ca000ae26c9db22_20211006164907.jpeg', 'jpeg', '087a464b2d91e6dc7ca000ae26c9db22_20211006164907.jpeg', 'local', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_upload_files` VALUES (16, '2021-10-06 17:00:01', '2021-10-06 17:00:01', NULL, 'wallhaven-3z32j3.jpeg', 'uploads/file/087a464b2d91e6dc7ca000ae26c9db22_20211006170000.jpeg', 'jpeg', '087a464b2d91e6dc7ca000ae26c9db22_20211006170000.jpeg', 'local', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_upload_files` VALUES (17, '2021-10-06 17:16:34', '2021-10-06 17:16:34', NULL, 'wallhaven-3z32j3.jpeg', 'uploads/file/087a464b2d91e6dc7ca000ae26c9db22_20211006171633.jpeg', 'jpeg', '087a464b2d91e6dc7ca000ae26c9db22_20211006171633.jpeg', 'local', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_upload_files` VALUES (18, '2021-10-06 17:23:43', '2021-10-06 17:23:43', NULL, 'wallhaven-3z32j3.jpeg', 'uploads/file/087a464b2d91e6dc7ca000ae26c9db22_20211006172342.jpeg', 'jpeg', '087a464b2d91e6dc7ca000ae26c9db22_20211006172342.jpeg', 'local', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_upload_files` VALUES (19, '2021-10-06 17:42:08', '2021-10-06 17:42:08', NULL, 'wallhaven-3z32j3.jpeg', 'uploads/file/087a464b2d91e6dc7ca000ae26c9db22_20211006174207.jpeg', 'jpeg', '087a464b2d91e6dc7ca000ae26c9db22_20211006174207.jpeg', 'local', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_upload_files` VALUES (20, '2021-10-06 17:42:37', '2021-10-06 17:42:37', NULL, 'wallhaven-3z32j3.jpeg', 'uploads/file/087a464b2d91e6dc7ca000ae26c9db22_20211006174236.jpeg', 'jpeg', '087a464b2d91e6dc7ca000ae26c9db22_20211006174236.jpeg', 'local', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_upload_files` VALUES (21, '2021-10-06 18:17:35', '2021-10-06 18:17:35', NULL, 'wallhaven-3z32j3.jpeg', 'uploads/file/087a464b2d91e6dc7ca000ae26c9db22_20211006181734.jpeg', 'jpeg', '087a464b2d91e6dc7ca000ae26c9db22_20211006181734.jpeg', 'local', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_upload_files` VALUES (22, '2021-10-15 22:34:35', NULL, NULL, 'wallhaven-3z32j3.jpeg', 'http://127.0.0.1:8889/uploads//uploads/2021/10/15/654000FA71FF48C68D40E41C0CD91E48.jpeg', 'jpeg', '654000FA71FF48C68D40E41C0CD91E48', NULL, NULL, NULL, NULL, 1637926, 'ff4cee364d67ee859c38a583907ad74a');
INSERT INTO `sys_upload_files` VALUES (23, '2021-10-15 22:35:54', NULL, NULL, 'wallhaven-3z32j3.jpeg', 'http://127.0.0.1:8889/uploads//uploads/2021/10/15/4A133328C3B44CF0BEF0E371D73B66A5.jpeg', 'jpeg', '4A133328C3B44CF0BEF0E371D73B66A5', NULL, NULL, NULL, NULL, 1637926, 'ff4cee364d67ee859c38a583907ad74a');
INSERT INTO `sys_upload_files` VALUES (24, '2021-10-15 22:37:49', NULL, NULL, 'wallhaven-3z32j3.jpeg', 'http://127.0.0.1:8889/uploads//uploads/2021/10/15/49EDACC3BC524A64A594F4C2DEB87964.jpeg', 'jpeg', '49EDACC3BC524A64A594F4C2DEB87964', NULL, NULL, NULL, NULL, 1637926, 'ff4cee364d67ee859c38a583907ad74a');
INSERT INTO `sys_upload_files` VALUES (25, '2021-10-15 22:39:21', NULL, NULL, 'wallhaven-3z32j3.jpeg', 'http://127.0.0.1:8889/uploads//uploads/2021/10/15/D2A45BF6B4B3443F9B5332A4FD7213B0.jpeg', 'jpeg', 'D2A45BF6B4B3443F9B5332A4FD7213B0', NULL, NULL, NULL, NULL, 1637926, 'ff4cee364d67ee859c38a583907ad74a');
INSERT INTO `sys_upload_files` VALUES (26, '2021-10-15 22:40:21', NULL, NULL, 'wallhaven-3z32j3.jpeg', 'http://127.0.0.1:8889/uploads//uploads/2021/10/15/B2529F82FE03489F83797F10B46AC340.jpeg', 'jpeg', 'B2529F82FE03489F83797F10B46AC340', NULL, NULL, NULL, NULL, 1637926, 'ff4cee364d67ee859c38a583907ad74a');
INSERT INTO `sys_upload_files` VALUES (27, '2021-10-16 11:50:07', NULL, NULL, 'wallhaven-3z32j3.jpeg', '/uploads/2021/10/16/1C823FB9E48047D2A3C57D80D8C9F047.jpeg', 'jpeg', '1C823FB9E48047D2A3C57D80D8C9F047', 'local', NULL, NULL, NULL, 1637926, 'ff4cee364d67ee859c38a583907ad74a');
INSERT INTO `sys_upload_files` VALUES (28, '2021-10-16 12:46:45', NULL, NULL, 'wallhaven-3z32j3.jpeg', '/uploads/2021/10/16/78CF9359A828456EAF4F666CA1C8B66C.jpeg', 'jpeg', '78CF9359A828456EAF4F666CA1C8B66C', 'local', NULL, NULL, NULL, 1637926, 'ff4cee364d67ee859c38a583907ad74a');
INSERT INTO `sys_upload_files` VALUES (29, '2021-10-16 13:08:21', NULL, NULL, 'wallhaven-3z32j3.jpeg', '/uploads/2021/10/16/B04F9F1B8B4A4C88A301E646D4BA0A17.jpeg', 'jpeg', 'B04F9F1B8B4A4C88A301E646D4BA0A17', 'local', 1, NULL, NULL, 1637926, 'ff4cee364d67ee859c38a583907ad74a');
INSERT INTO `sys_upload_files` VALUES (30, '2021-10-16 13:10:33', NULL, NULL, '666632j3.jpeg', '/uploads/2021/10/16/BB7DCE6845704924A8F617FEE7FB49DB.jpeg', 'jpeg', 'BB7DCE6845704924A8F617FEE7FB49DB', 'local', 1, NULL, NULL, 1637926, 'ff4cee364d67ee859c38a583907ad74a');
INSERT INTO `sys_upload_files` VALUES (31, '2021-10-16 23:55:12', NULL, NULL, 'wallhaven-3z32j3.jpeg', '/uploads/2021/10/16/2CFD0754476442EF9DECF6E1EDAFA043.jpeg', 'jpeg', '2CFD0754476442EF9DECF6E1EDAFA043', 'local', 1, NULL, NULL, 1637926, 'ff4cee364d67ee859c38a583907ad74a');
INSERT INTO `sys_upload_files` VALUES (32, '2021-10-17 10:56:30', NULL, NULL, 'wallhaven-3z32j3.jpeg', '/uploads/2021/10/17/73B033B723864222A12DD80580EADB58.jpeg', 'jpeg', '73B033B723864222A12DD80580EADB58', 'local', 1, NULL, NULL, 1637926, 'ff4cee364d67ee859c38a583907ad74a');
INSERT INTO `sys_upload_files` VALUES (33, '2021-10-17 11:12:59', NULL, NULL, 'wallhaven-3z32j3.jpeg', '/uploads/2021/10/17/9DB6C32DE8BA419AB4AC1BC9602F27FF.jpeg', 'jpeg', '9DB6C32DE8BA419AB4AC1BC9602F27FF', 'local', 1, NULL, NULL, 1637926, 'ff4cee364d67ee859c38a583907ad74a');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_roles
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_roles`;
CREATE TABLE `sys_user_roles` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `role_id` bigint unsigned NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_user_roles
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_roles` VALUES (1, 1, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_roles` VALUES (2, 1, 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_roles` VALUES (3, 1, 3, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_roles` VALUES (4, 2, 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_roles` VALUES (5, 3, 3, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_roles` VALUES (6, 4, 3, '2021-10-13 16:36:59.359000', 'admin', NULL, '2021-10-13 16:36:59.359000', 'admin');
INSERT INTO `sys_user_roles` VALUES (9, 7, 3, '2021-10-13 17:08:04.053000', 'admin', NULL, '2021-10-13 17:08:04.053000', 'admin');
COMMIT;

-- ----------------------------
-- Table structure for sys_users
-- ----------------------------
DROP TABLE IF EXISTS `sys_users`;
CREATE TABLE `sys_users` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `address` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `mobile` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `status` int DEFAULT NULL,
  `type` int DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `del_flag` tinyint(1) DEFAULT NULL,
  `department_id` bigint unsigned DEFAULT NULL,
  `street` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `pass_strength` varchar(2) CHARACTER SET utf8 DEFAULT NULL,
  `department_title` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `birth` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `is_admin` bit(1) DEFAULT NULL,
  `uuid` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `email` (`email`) USING BTREE,
  UNIQUE KEY `mobile` (`mobile`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_users
-- ----------------------------
BEGIN;
INSERT INTO `sys_users` VALUES (1, '北京市,市辖区,东城区', 'https://ooo.0o0.ooo/2019/04/28/5cc5a71a6e3b6.png', '啥也没有啊', 'admin@qq.com', '18888888888', '管理员', '$2a$10$PS04ecXfknNd3V8d.ymLTObQciapMU4xU8.GADBZZsuTZr7ymnagy', '男', 1, 1, 'admin', 0, 1, '浙江省杭州市', '弱', '总部', '2020-04-15 00:00:00.000000', '2018-05-01 03:13:51.000000', NULL, NULL, '2021-10-17 12:43:45.277000', NULL, b'1', NULL, NULL);
INSERT INTO `sys_users` VALUES (2, '北京市,市辖区,东城区', 'https://ooo.0o0.ooo/2019/04/28/5cc5a71a6e3b6.png', '啥也没有啊', 'westhack@limaopu.com', '18782059032', 'westhack', '$2a$10$PS04ecXfknNd3V8d.ymLTObQciapMU4xU8.GADBZZsuTZr7ymnagy', '女', 1, 0, 'westhack', 0, 1, '浙江省杭州市\n\n', '弱', '成都分部', '2020-04-13 00:00:00.000000', '2018-04-30 23:28:42.000000', NULL, NULL, NULL, NULL, b'1', NULL, NULL);
INSERT INTO `sys_users` VALUES (3, '北京市,市辖区,东城区', 'https://ooo.0o0.ooo/2019/04/28/5cc5a71a6e3b6.png', '啥也没有啊', 'test@ @limaopu.com', '18782059033', '游客1', '$2a$10$PS04ecXfknNd3V8d.ymLTObQciapMU4xU8.GADBZZsuTZr7ymnagy', '男', 1, 0, 'test', 0, 1, '浙江省杭州市\n\n', '弱', '成都分部', '2020-04-28 00:00:00.000000', '2018-05-03 15:09:42.000000', NULL, NULL, NULL, NULL, b'0', NULL, NULL);
INSERT INTO `sys_users` VALUES (4, '北京市,市辖区,东城区', 'https://ooo.0o0.ooo/2019/04/28/5cc5a71a6e3b6.png', '啥也没有啊', 'test2@ @limaopu.com', '18782059034', '游客2', '$2a$10$PS04ecXfknNd3V8d.ymLTObQciapMU4xU8.GADBZZsuTZr7ymnagy', '男', 1, 0, 'test2', 0, 1, '浙江省杭州市\n\n', '弱', '总部', '2020-04-22 11:00:00.000000', '2018-06-06 18:48:02.000000', NULL, NULL, '2021-10-13 16:36:59.274000', NULL, b'0', NULL, NULL);
INSERT INTO `sys_users` VALUES (7, NULL, 'https://ooo.0o0.ooo/2019/04/28/5cc5a71a6e3b6.png', '啥也没有啊', '123@qq.com', '12345', '测试1', '$2a$10$ROb9zkNyXrQQLdtL4kKEH.VfL84QMa/5CeT.6rUtv14FXLBm32CFy', NULL, 1, 0, 'ceshi2', NULL, 6, '浙江省杭州市\n\n', NULL, NULL, NULL, '2021-10-13 16:37:54.897000', NULL, NULL, '2021-10-13 17:08:03.915000', NULL, b'0', NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
