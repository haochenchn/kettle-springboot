/*
该项目需要用到的数据表，使用mysql数据库
Date: 2018-11-22 11:09:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for k_data_dept
-- ----------------------------
DROP TABLE IF EXISTS `k_data_dept`;
CREATE TABLE `k_data_dept` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `kettle_job_name` varchar(255) DEFAULT NULL COMMENT '该部门对应的kettle导入job名',
  `kettle_job_path` varchar(100) DEFAULT NULL COMMENT '资源库内job相对路径，默认为/',
  `remark` varchar(255) DEFAULT NULL,
  `file_template` varchar(255) DEFAULT NULL COMMENT '模板文件.zip',
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `modifytime` timestamp NULL DEFAULT NULL,
  `createuser` varchar(50) DEFAULT NULL,
  `isdelete` int(1) DEFAULT '0' COMMENT '删除标志：0未删除1已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='数据部门';

-- ----------------------------
-- Records of k_data_dept
-- ----------------------------
INSERT INTO `k_data_dept` VALUES ('1', '人社', null, '数据导入job', '/test', null, 'test.zip', '2018-11-20 22:28:24', null, null, '0');
INSERT INTO `k_data_dept` VALUES ('2', '公安', null, '数据导入job', '/test', null, 'test.zip', '2018-11-20 17:25:27', null, null, '0');

-- ----------------------------
-- Table structure for k_data_file
-- ----------------------------
DROP TABLE IF EXISTS `k_data_file`;
CREATE TABLE `k_data_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '文件名',
  `path` varchar(100) DEFAULT NULL COMMENT '文件目录',
  `file_size` double DEFAULT NULL COMMENT '文件大小（k）',
  `pid` int(11) DEFAULT NULL COMMENT '父文件（如果是zip文件，其pid为0，否则为zip文件的id）',
  `dept_id` int(11) DEFAULT NULL COMMENT '数据部门',
  `dept_name` varchar(50) DEFAULT NULL,
  `extension` varchar(20) DEFAULT NULL COMMENT '文件后缀',
  `file_desc` varchar(255) DEFAULT NULL,
  `import_status` int(1) DEFAULT NULL COMMENT '文件导入状态(1正在导入2数据格式不对3已导入4导入失败)',
  `import_desc` varchar(255) DEFAULT NULL,
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `createuser` varchar(20) DEFAULT NULL,
  `isdelete` int(1) DEFAULT '0' COMMENT '删除标识（0未删除1已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of k_data_file
-- ----------------------------
INSERT INTO `k_data_file` VALUES ('18', 'test20181119.zip', 'dataclean/人社/test20181119.zip-20181119-223528/', '1094', '0', '1', '人社', 'zip', null, '3', null, '2018-11-19 22:35:32', null, '0');
INSERT INTO `k_data_file` VALUES ('19', 'file2.csv', 'dataclean/人社/test20181119.zip-20181119-223528/', '592', '18', '1', '人社', 'csv', null, null, null, '2018-11-19 22:35:33', null, '0');
INSERT INTO `k_data_file` VALUES ('20', 'other.csv', 'dataclean/人社/test20181119.zip-20181119-223528/', '592', '18', '1', '人社', 'csv', null, null, null, '2018-11-19 22:35:33', null, '0');
INSERT INTO `k_data_file` VALUES ('21', 'file.csv', 'dataclean/人社/test20181119.zip-20181119-223528/', '592', '18', '1', '人社', 'csv', null, null, null, '2018-11-19 22:35:33', null, '0');
INSERT INTO `k_data_file` VALUES ('22', 'test20181119.zip', 'dataclean/人社/test20181119.zip-20181120-150033/', '1', '0', '1', '人社', 'zip', null, '3', null, '2018-11-20 15:00:33', null, '0');
INSERT INTO `k_data_file` VALUES ('23', 'file2.csv', 'dataclean/人社/test20181119.zip-20181120-150033/', '0', '22', '1', '人社', 'csv', null, '0', null, '2018-11-20 15:00:33', null, '0');
INSERT INTO `k_data_file` VALUES ('24', 'other.csv', 'dataclean/人社/test20181119.zip-20181120-150033/', '0', '22', '1', '人社', 'csv', null, '0', null, '2018-11-20 15:00:33', null, '0');
INSERT INTO `k_data_file` VALUES ('25', 'file.csv', 'dataclean/人社/test20181119.zip-20181120-150033/', '0', '22', '1', '人社', 'csv', null, '0', null, '2018-11-20 15:00:33', null, '0');
INSERT INTO `k_data_file` VALUES ('26', 'test20181119.zip', 'dataclean/公安/test20181119.zip-20181120-200248/', '1', '0', '2', '公安', 'zip', null, '3', null, '2018-11-20 20:02:48', null, '0');
INSERT INTO `k_data_file` VALUES ('27', 'file2.csv', 'dataclean/公安/test20181119.zip-20181120-200248/', '0', '26', '2', '公安', 'csv', null, '0', null, '2018-11-20 20:02:48', null, '0');
INSERT INTO `k_data_file` VALUES ('28', 'other.csv', 'dataclean/公安/test20181119.zip-20181120-200248/', '0', '26', '2', '公安', 'csv', null, '0', null, '2018-11-20 20:02:48', null, '0');
INSERT INTO `k_data_file` VALUES ('29', 'file.csv', 'dataclean/公安/test20181119.zip-20181120-200248/', '0', '26', '2', '公安', 'csv', null, '0', null, '2018-11-20 20:02:48', null, '0');
INSERT INTO `k_data_file` VALUES ('30', 'test.zip', 'dataclean/公安/test.zip-20181120-200723/', '2', '0', '2', '公安', 'zip', '这是只一个测试', '3', null, '2018-11-20 20:07:23', null, '0');
INSERT INTO `k_data_file` VALUES ('31', 'v_job.xls', 'dataclean/公安/test.zip-20181120-200723/', '14', '30', '2', '公安', 'xls', null, '0', null, '2018-11-20 20:07:23', null, '0');
INSERT INTO `k_data_file` VALUES ('32', 'file.csv', 'dataclean/公安/test.zip-20181120-200723/', '0', '30', '2', '公安', 'csv', null, '0', null, '2018-11-20 20:07:23', null, '0');
INSERT INTO `k_data_file` VALUES ('33', 'test.zip', 'dataclean/人社/test.zip-20181121-085901/', '2', '0', '1', '人社', 'zip', 'rrr', '2', '文件[v_job.xls]命名不规范', '2018-11-21 08:59:01', null, '0');
INSERT INTO `k_data_file` VALUES ('34', '测试-命名不规范.zip', 'dataclean/人社/测试-命名不规范.zip-20181121-091312/', '7', '0', '1', '人社', 'zip', 'test1', '2', '文件[测试文件名2018.xls]格式有误，应为xls', '2018-11-21 09:13:12', null, '0');
INSERT INTO `k_data_file` VALUES ('35', '测试-命名不规范.zip', 'dataclean/人社/测试-命名不规范.zip-20181121-091659/', '7', '0', '1', '人社', 'zip', 'test1', '2', '文件[测试文件名2018.xls]格式有误，应为xls', '2018-11-21 09:16:59', null, '0');
INSERT INTO `k_data_file` VALUES ('36', '测试-命名不规范.zip', 'dataclean/人社/测试-命名不规范.zip-20181121-092508/', '7', '0', '1', '人社', 'zip', 'test1', '2', '文件[测试文件名2018.xls]格式有误，应为xls', '2018-11-21 09:25:08', null, '0');
INSERT INTO `k_data_file` VALUES ('37', '测试-命名不规范.zip', 'dataclean/人社/测试-命名不规范.zip-20181121-092935/', '7', '0', '1', '人社', 'zip', 'test1', '2', '文件[测试文件名2018.xls]的第i+1列数据有误', '2018-11-21 09:29:35', null, '0');
INSERT INTO `k_data_file` VALUES ('38', '测试-命名不规范.zip', 'dataclean/人社/测试-命名不规范.zip-20181121-093256/', '7', '0', '1', '人社', 'zip', 'test1', '2', '文件[other.csv]命名不规范', '2018-11-21 09:32:56', null, '0');
INSERT INTO `k_data_file` VALUES ('39', 'xlsx.zip', 'dataclean/人社/xlsx.zip-20181121-093949/', '7', '0', '1', '人社', 'zip', 'test-xlsx', '2', '文件[xlsx/测试文件名2018.xlsx]格式有误，应为xlsx', '2018-11-21 09:39:49', null, '0');
INSERT INTO `k_data_file` VALUES ('40', 'xlsx.zip', 'dataclean/人社/xlsx.zip-20181121-094132/', '7', '0', '1', '人社', 'zip', 'test-xlsx', '2', '文件[xlsx/测试文件名2018.xlsx]格式有误，应为xls', '2018-11-21 09:41:32', null, '0');
INSERT INTO `k_data_file` VALUES ('41', 'xlsx.zip', 'dataclean/人社/xlsx.zip-20181121-094219/', '7', '0', '1', '人社', 'zip', 'test-xlsx', '3', null, '2018-11-21 09:42:19', null, '0');
INSERT INTO `k_data_file` VALUES ('42', 'xlsx/测试文件名2018.xlsx', 'dataclean/人社/xlsx.zip-20181121-094219/', '9', '41', '1', '人社', 'xlsx', null, '0', null, '2018-11-21 09:43:29', null, '0');
INSERT INTO `k_data_file` VALUES ('43', '测试-命名不规范.zip', 'dataclean/人社/测试-命名不规范.zip-20181121-155013/', '7', '0', '1', '人社', 'zip', '', '2', '文件[other.csv]命名不规范', '2018-11-21 15:50:15', null, '0');
INSERT INTO `k_data_file` VALUES ('44', 'xlsx.zip', 'dataclean/人社/xlsx.zip-20181121-232115/', '7', '0', '1', '人社', 'zip', 'test1', '2', '文件[xlsx/测试文件名2018.xlsx]格式有误，应为xls', '2018-11-21 23:21:15', null, '0');

-- ----------------------------
-- Table structure for k_data_file_format
-- ----------------------------
DROP TABLE IF EXISTS `k_data_file_format`;
CREATE TABLE `k_data_file_format` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(150) DEFAULT NULL COMMENT '文件类型',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门id',
  `file_name` varchar(150) DEFAULT NULL COMMENT '标准文件名',
  `head_arr` varchar(510) DEFAULT NULL COMMENT '表头数组（以 , 分隔）',
  `begin_row` int(5) DEFAULT NULL COMMENT '表头所在行',
  `extension` varchar(25) DEFAULT NULL COMMENT '后缀',
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `createuser` varchar(25) DEFAULT NULL,
  `isdelete` int(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of k_data_file_format
-- ----------------------------
INSERT INTO `k_data_file_format` VALUES ('1', '测试文件', '1', '测试文件名', '作业ID,作业目录,作业名称,作业描述,作业状态,最后更新时间,增量时间戳,资源库', '1', 'xls', '2018-11-21 11:21:50', null, '0');
