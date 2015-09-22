/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50622
Source Host           : localhost:3306
Source Database       : web

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2015-08-17 11:12:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for filebox
-- ----------------------------
DROP TABLE IF EXISTS `filebox`;
CREATE TABLE `filebox` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `filename` varchar(50) NOT NULL COMMENT '文件名称',
  `filepath` varchar(50) NOT NULL COMMENT '文件保存名称',
  `uid` bigint(20) NOT NULL COMMENT '用户名称',
  `dirid` bigint(20) DEFAULT NULL COMMENT '目录id',
  `create_time` datetime NOT NULL COMMENT '上传时间',
  `ftype` varchar(10) DEFAULT NULL,
  `size` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='文件站';

-- ----------------------------
-- Records of filebox
-- ----------------------------

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `id` int(11) DEFAULT NULL,
  `operator_id` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '0 普通访问 1增 2 删改',
  `remark` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of log
-- ----------------------------

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log` (
  `id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `login_time` datetime DEFAULT NULL,
  `logout_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of login_log
-- ----------------------------
INSERT INTO `login_log` VALUES ('00000000001', '1', '127.0.0.1', '2015-08-17 10:15:10', '2015-08-17 10:16:51');
INSERT INTO `login_log` VALUES ('00000000002', '1', '127.0.0.1', '2015-08-17 10:16:53', '2015-08-17 10:18:56');
INSERT INTO `login_log` VALUES ('00000000003', '1', '127.0.0.1', '2015-08-17 10:18:58', '2015-08-17 10:19:23');
INSERT INTO `login_log` VALUES ('00000000004', '1', '127.0.0.1', '2015-08-17 10:19:24', null);
INSERT INTO `login_log` VALUES ('00000000005', '1', '127.0.0.1', '2015-08-17 10:59:44', null);

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '0 菜单 1 功能',
  `url` varchar(255) DEFAULT NULL,
  `seq` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('9', '0', '系统管理', '0', null, '4', '2014-07-01 11:36:22', '2014-07-01 11:38:01');
INSERT INTO `menu` VALUES ('17', '9', '操作员列表', '0', '/user', '0', '2014-07-01 11:39:13', '2014-07-01 11:40:26');
INSERT INTO `menu` VALUES ('18', '9', '菜单管理', '0', '/menu', '2', '2014-07-01 11:39:29', '2014-07-01 11:40:32');
INSERT INTO `menu` VALUES ('19', '9', '角色管理', '0', '/role', '3', '2014-07-01 11:39:41', '2014-07-01 11:40:37');
INSERT INTO `menu` VALUES ('20', '9', '登录日志', '0', '/loginLog', '4', '2014-07-01 11:39:55', '2014-07-01 11:40:42');
INSERT INTO `menu` VALUES ('21', '9', '文件站管理', '0', '/filebox', '1', '2014-07-01 11:40:15', null);
INSERT INTO `menu` VALUES ('32', '17', '查询列表', '1', '/user', '0', '2014-07-01 11:58:55', null);
INSERT INTO `menu` VALUES ('33', '21', '查询列表', '1', '/filebox', '0', '2014-07-01 11:59:09', null);
INSERT INTO `menu` VALUES ('34', '18', '查询列表', '1', '/menu', '0', '2014-07-01 11:59:22', null);
INSERT INTO `menu` VALUES ('35', '19', '查询列表', '1', '/role', '0', '2014-07-01 11:59:40', null);
INSERT INTO `menu` VALUES ('36', '20', '查询列表', '1', '/loginLog', '0', '2014-07-01 11:59:55', null);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `menuIds` varchar(2000) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `seq` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '系统管理员', '9,17,32,21,33,18,34,19,35,20,36', null, '2014-07-01 11:44:47', '0', '2015-08-17 10:19:14');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `menu_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES ('87', '1', '9', '2015-08-17 10:19:13', null);
INSERT INTO `role_menu` VALUES ('88', '1', '17', '2015-08-17 10:19:13', null);
INSERT INTO `role_menu` VALUES ('89', '1', '32', '2015-08-17 10:19:13', null);
INSERT INTO `role_menu` VALUES ('90', '1', '21', '2015-08-17 10:19:13', null);
INSERT INTO `role_menu` VALUES ('91', '1', '33', '2015-08-17 10:19:13', null);
INSERT INTO `role_menu` VALUES ('92', '1', '18', '2015-08-17 10:19:13', null);
INSERT INTO `role_menu` VALUES ('93', '1', '34', '2015-08-17 10:19:13', null);
INSERT INTO `role_menu` VALUES ('94', '1', '19', '2015-08-17 10:19:13', null);
INSERT INTO `role_menu` VALUES ('95', '1', '35', '2015-08-17 10:19:13', null);
INSERT INTO `role_menu` VALUES ('96', '1', '20', '2015-08-17 10:19:13', null);
INSERT INTO `role_menu` VALUES ('97', '1', '36', '2015-08-17 10:19:13', null);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shop_id` int(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `role_ids` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '1', 'admin', '123456', '123', null, null, '1', '2014-07-01 11:52:22', null);
INSERT INTO `user` VALUES ('12', null, 'tianjun', '123', '123', '3212@qq.com', '123123', '', '2015-08-17 10:26:48', '2014-05-19 17:18:29');
INSERT INTO `user` VALUES ('14', null, 'admin111', '123', '1233', '123@1123.com', '123', '', '2015-08-17 10:31:19', '2015-08-17 10:27:08');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL DEFAULT '0',
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('0', '1', '1', '2014-07-01 11:52:22', null);
