/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50540
Source Host           : localhost:3306
Source Database       : managesystemdb

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2018-05-07 08:43:16
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `tb_books`
-- ----------------------------
DROP TABLE IF EXISTS `tb_books`;
CREATE TABLE `tb_books` (
  `book_id` varchar(50) NOT NULL DEFAULT '',
  `book_name` varchar(50) DEFAULT NULL,
  `book_press` varchar(50) DEFAULT NULL,
  `book_inventory` int(255) NOT NULL,
  `book_author` varchar(50) DEFAULT NULL,
  `current_inventory` int(255) DEFAULT NULL,
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_books
-- ----------------------------
INSERT INTO `tb_books` VALUES ('b800ced1cad54e678a36392aab5dbd49', 'dsfsdf', 'sdfsdfs', '2', 'sdfsfds', '2');
INSERT INTO `tb_books` VALUES ('d2ae0201058c48e89c88bcfeb1af11c3', '图书', '出版社', '50', '作者', '49');

-- ----------------------------
-- Table structure for `tb_borrow_books`
-- ----------------------------
DROP TABLE IF EXISTS `tb_borrow_books`;
CREATE TABLE `tb_borrow_books` (
  `user_id` int(11) NOT NULL,
  `book_id` varchar(50) NOT NULL DEFAULT '',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `book_name` varchar(255) NOT NULL,
  `book_author` varchar(255) DEFAULT NULL,
  `book_press` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_borrow_books
-- ----------------------------
INSERT INTO `tb_borrow_books` VALUES ('26', 'd2ae0201058c48e89c88bcfeb1af11c3', '2018-05-03 20:37:44', '图书', '作者', '出版社');

-- ----------------------------
-- Table structure for `tb_memorandum`
-- ----------------------------
DROP TABLE IF EXISTS `tb_memorandum`;
CREATE TABLE `tb_memorandum` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` datetime NOT NULL,
  `user_code` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `resource_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_memorandum
-- ----------------------------
INSERT INTO `tb_memorandum` VALUES ('16', '2018-05-02 19:01:36', 'admin', 'admin', '登陆 | admin');
INSERT INTO `tb_memorandum` VALUES ('17', '2018-05-02 19:01:52', 'admin', 'admin', '删除 | 1526705078 | LGG');
INSERT INTO `tb_memorandum` VALUES ('18', '2018-05-02 19:04:04', 'admin', 'admin', '修改/新增 | admin | admin');
INSERT INTO `tb_memorandum` VALUES ('19', '2018-05-02 19:15:39', 'admin', 'admin', '修改/新增角色 | ddddd | dddd');
INSERT INTO `tb_memorandum` VALUES ('20', '2018-05-02 19:16:19', 'admin', 'admin', '修改/新增角色 | ddddd | dddd');
INSERT INTO `tb_memorandum` VALUES ('21', '2018-05-02 19:16:47', 'admin', 'admin', '删除角色 | ddddd | dddd');
INSERT INTO `tb_memorandum` VALUES ('22', '2018-05-02 19:27:30', 'admin', 'admin', '登陆 | admin');
INSERT INTO `tb_memorandum` VALUES ('23', '2018-05-02 19:28:21', 'admin', 'admin', '登陆 | admin');
INSERT INTO `tb_memorandum` VALUES ('25', '2018-05-02 19:32:17', 'admin', 'admin', '登陆 | admin');
INSERT INTO `tb_memorandum` VALUES ('26', '2018-05-02 19:33:40', 'admin', 'admin', '登陆 | admin');
INSERT INTO `tb_memorandum` VALUES ('27', '2018-05-02 19:36:17', 'admin', 'admin', '登出 | admin');
INSERT INTO `tb_memorandum` VALUES ('28', '2018-05-02 19:36:33', 'admin', 'admin', '登陆 | admin');
INSERT INTO `tb_memorandum` VALUES ('29', '2018-05-02 19:45:33', 'admin', 'admin', '登陆 | admin');
INSERT INTO `tb_memorandum` VALUES ('30', '2018-05-02 19:55:11', 'admin', 'admin', '登出 | admin');
INSERT INTO `tb_memorandum` VALUES ('31', '2018-05-02 19:55:30', '1526705079', '1111', '登陆 | 1526705079');
INSERT INTO `tb_memorandum` VALUES ('32', '2018-05-02 19:56:03', '1526705079', '1111', '登出 | 1526705079');
INSERT INTO `tb_memorandum` VALUES ('33', '2018-05-02 19:56:08', 'admin', 'admin', '登陆 | admin');
INSERT INTO `tb_memorandum` VALUES ('34', '2018-05-02 19:57:38', 'admin', 'admin', '修改/新增用户 | 1526705033 | 222');
INSERT INTO `tb_memorandum` VALUES ('35', '2018-05-02 19:57:57', 'admin', 'admin', '修改/新增角色 | 测试用的 | test');
INSERT INTO `tb_memorandum` VALUES ('36', '2018-05-02 19:58:39', 'admin', 'admin', '登出 | admin');
INSERT INTO `tb_memorandum` VALUES ('37', '2018-05-02 19:58:43', '1526705033', '222', '登陆 | 1526705033');
INSERT INTO `tb_memorandum` VALUES ('38', '2018-05-02 19:59:47', 'admin', 'admin', '登陆 | admin');
INSERT INTO `tb_memorandum` VALUES ('39', '2018-05-02 20:05:48', 'admin', 'admin', '修改/新增用户 | 1526705033 | 222');
INSERT INTO `tb_memorandum` VALUES ('40', '2018-05-02 20:06:21', 'admin', 'admin', '登出 | admin');
INSERT INTO `tb_memorandum` VALUES ('41', '2018-05-02 20:06:46', '1526705033', '222', '登陆 | 1526705033');
INSERT INTO `tb_memorandum` VALUES ('42', '2018-05-02 21:22:59', 'admin', 'admin', '登陆 | admin');
INSERT INTO `tb_memorandum` VALUES ('43', '2018-05-03 20:21:04', '1526705033', '222', '登陆 | 1526705033');
INSERT INTO `tb_memorandum` VALUES ('44', '2018-05-03 20:31:56', 'admin', 'admin', '登陆 | admin');
INSERT INTO `tb_memorandum` VALUES ('45', '2018-05-03 20:37:24', '1526705033', '222', '登陆 | 1526705033');
INSERT INTO `tb_memorandum` VALUES ('46', '2018-05-03 20:38:02', 'admin', 'admin', '注销用户 | 1526705033 | 222');
INSERT INTO `tb_memorandum` VALUES ('47', '2018-05-03 20:38:11', 'admin', 'admin', '注销用户 | 1526705079 | 1111');
INSERT INTO `tb_memorandum` VALUES ('48', '2018-05-03 20:38:16', 'admin', 'admin', '注销用户 | 1418140304 | Jon');
INSERT INTO `tb_memorandum` VALUES ('49', '2018-05-03 20:38:20', 'admin', 'admin', '注销用户 | 1418140303 | James');
INSERT INTO `tb_memorandum` VALUES ('50', '2018-05-03 20:38:48', 'admin', 'admin', '修改/新增用户 | 1526705033 | SB');
INSERT INTO `tb_memorandum` VALUES ('51', '2018-05-03 20:39:00', '1526705033', 'SB', '登陆 | 1526705033');
INSERT INTO `tb_memorandum` VALUES ('52', '2018-05-03 20:39:37', '1526705033', 'SB', '修改/新增用户 | admin | admin');
INSERT INTO `tb_memorandum` VALUES ('53', '2018-05-03 20:39:57', '1526705033', 'SB', '修改/新增用户 | 1418140303 | James');
INSERT INTO `tb_memorandum` VALUES ('54', '2018-05-05 17:10:45', 'admin', 'admin', '登陆 | admin');
INSERT INTO `tb_memorandum` VALUES ('55', '2018-05-05 17:11:00', 'admin', 'admin', '修改/新增图书 | a6282a1558a34bf087e7b1dc5782e373 | 小王子');
INSERT INTO `tb_memorandum` VALUES ('56', '2018-05-05 17:11:58', 'admin', 'admin', '删除图书 | ce365ba449704b78871390384c40ed25 | 百年孤独');
INSERT INTO `tb_memorandum` VALUES ('57', '2018-05-05 17:14:44', 'admin', 'admin', '修改/新增图书 | a6282a1558a34bf087e7b1dc5782e373 | 小王子');
INSERT INTO `tb_memorandum` VALUES ('58', '2018-05-05 17:14:53', 'admin', 'admin', '修改/新增图书 | a6282a1558a34bf087e7b1dc5782e373 | 小王子');
INSERT INTO `tb_memorandum` VALUES ('59', '2018-05-05 17:15:19', 'admin', 'admin', '修改/新增图书 | d2ae0201058c48e89c88bcfeb1af11c3 | 图书');
INSERT INTO `tb_memorandum` VALUES ('60', '2018-05-05 17:16:01', 'admin', 'admin', '修改/新增图书 | a6282a1558a34bf087e7b1dc5782e373 | 小王子');
INSERT INTO `tb_memorandum` VALUES ('61', '2018-05-05 17:16:10', 'admin', 'admin', '修改/新增图书 | a6282a1558a34bf087e7b1dc5782e373 | 小王子');
INSERT INTO `tb_memorandum` VALUES ('62', '2018-05-05 17:16:23', 'admin', 'admin', '修改/新增图书 | a6282a1558a34bf087e7b1dc5782e373 | 小王子');
INSERT INTO `tb_memorandum` VALUES ('63', '2018-05-05 17:17:37', 'admin', 'admin', '修改/新增图书 | a6282a1558a34bf087e7b1dc5782e373 | 小王子');
INSERT INTO `tb_memorandum` VALUES ('64', '2018-05-05 17:21:53', 'admin', 'admin', '修改/新增图书 | b800ced1cad54e678a36392aab5dbd49 | dsfsdf');
INSERT INTO `tb_memorandum` VALUES ('65', '2018-05-05 17:22:06', 'admin', 'admin', '修改/新增图书 | b800ced1cad54e678a36392aab5dbd49 | dsfsdf');
INSERT INTO `tb_memorandum` VALUES ('66', '2018-05-05 17:27:12', 'admin', 'admin', '修改/新增图书 | b800ced1cad54e678a36392aab5dbd49 | dsfsdf');
INSERT INTO `tb_memorandum` VALUES ('67', '2018-05-05 17:35:51', 'admin', 'admin', '修改/新增图书 | a6282a1558a34bf087e7b1dc5782e373 | 小王子');
INSERT INTO `tb_memorandum` VALUES ('68', '2018-05-05 17:56:57', 'admin', 'admin', '登陆 | admin');
INSERT INTO `tb_memorandum` VALUES ('69', '2018-05-05 17:58:04', 'admin', 'admin', '修改/新增图书 | a6282a1558a34bf087e7b1dc5782e373 | 小王子');
INSERT INTO `tb_memorandum` VALUES ('70', '2018-05-05 17:58:26', 'admin', 'admin', '修改/新增图书 | a6282a1558a34bf087e7b1dc5782e373 | 小王子');
INSERT INTO `tb_memorandum` VALUES ('71', '2018-05-05 18:00:05', 'admin', 'admin', '修改/新增图书 | a6282a1558a34bf087e7b1dc5782e373 | 小王子');
INSERT INTO `tb_memorandum` VALUES ('72', '2018-05-05 18:01:32', 'admin', 'admin', '修改/新增图书 | a6282a1558a34bf087e7b1dc5782e373 | 小王子');
INSERT INTO `tb_memorandum` VALUES ('73', '2018-05-05 18:04:06', 'admin', 'admin', '修改/新增图书 | a6282a1558a34bf087e7b1dc5782e373 | 小王子');
INSERT INTO `tb_memorandum` VALUES ('74', '2018-05-05 18:04:30', 'admin', 'admin', '修改/新增图书 | d2ae0201058c48e89c88bcfeb1af11c3 | 图书');
INSERT INTO `tb_memorandum` VALUES ('75', '2018-05-05 18:06:09', 'admin', 'admin', '删除图书 | a6282a1558a34bf087e7b1dc5782e373 | 小王子');
INSERT INTO `tb_memorandum` VALUES ('76', '2018-05-05 18:07:11', 'admin', 'admin', '修改/新增图书 | b800ced1cad54e678a36392aab5dbd49 | dsfsdf');
INSERT INTO `tb_memorandum` VALUES ('77', '2018-05-05 18:07:23', 'admin', 'admin', '修改/新增图书 | b800ced1cad54e678a36392aab5dbd49 | dsfsdf');
INSERT INTO `tb_memorandum` VALUES ('78', '2018-05-05 18:07:34', 'admin', 'admin', '修改/新增图书 | d2ae0201058c48e89c88bcfeb1af11c3 | 图书');
INSERT INTO `tb_memorandum` VALUES ('79', '2018-05-05 18:08:17', 'admin', 'admin', '修改/新增图书 | b800ced1cad54e678a36392aab5dbd49 | dsfsdf');
INSERT INTO `tb_memorandum` VALUES ('80', '2018-05-05 18:08:37', 'admin', 'admin', '修改/新增图书 | b800ced1cad54e678a36392aab5dbd49 | dsfsdf');
INSERT INTO `tb_memorandum` VALUES ('81', '2018-05-05 18:11:30', 'admin', 'admin', '登陆 | admin');
INSERT INTO `tb_memorandum` VALUES ('82', '2018-05-05 18:11:43', 'admin', 'admin', '修改/新增图书 | 116bceda63a84735bc0f6213ba562a1b | hhhh');
INSERT INTO `tb_memorandum` VALUES ('83', '2018-05-05 18:12:15', 'admin', 'admin', '删除图书 | 116bceda63a84735bc0f6213ba562a1b | hhhh');
INSERT INTO `tb_memorandum` VALUES ('85', '2018-05-05 18:49:35', 'admin', 'admin', '修改/新增图书 | b800ced1cad54e678a36392aab5dbd49 | dsfsdf');
INSERT INTO `tb_memorandum` VALUES ('86', '2018-05-05 19:28:42', 'admin', 'admin', '登陆 | admin');
INSERT INTO `tb_memorandum` VALUES ('87', '2018-05-05 19:39:33', 'admin', 'admin', '修改/新增图书 | a13713cf15ec4acaaba9289e417f919c | dsa');
INSERT INTO `tb_memorandum` VALUES ('88', '2018-05-05 19:39:46', 'admin', 'admin', '删除图书 | a13713cf15ec4acaaba9289e417f919c | dsa');

-- ----------------------------
-- Table structure for `tb_resource`
-- ----------------------------
DROP TABLE IF EXISTS `tb_resource`;
CREATE TABLE `tb_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `is_hide` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `source_key` varchar(255) DEFAULT NULL,
  `source_url` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf5ra2gn0xedeida2op8097sr5` (`parent_id`),
  CONSTRAINT `FKf5ra2gn0xedeida2op8097sr5` FOREIGN KEY (`parent_id`) REFERENCES `tb_resource` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_resource
-- ----------------------------
INSERT INTO `tb_resource` VALUES ('1', '2018-03-01 13:56:51', '用户管理', null, '0', '2', '用户管理', '1', 'system:user:index', '/admin/user/index', '1', '2018-04-01 13:59:01', null);
INSERT INTO `tb_resource` VALUES ('2', '2018-03-01 13:56:51', '用户修改', null, '0', '3', '用户修改', '1', 'system:user:edit', '/admin/user/edit*', '2', '2018-04-01 16:26:42', '1');
INSERT INTO `tb_resource` VALUES ('3', '2018-03-01 16:48:48', '用户添加', null, '0', '3', '用户添加', '2', 'system:user:add', '/admin/user/add', '2', '2018-04-01 16:49:26', '1');
INSERT INTO `tb_resource` VALUES ('4', '2018-03-01 16:48:48', '用户删除', null, '0', '3', '用户删除', '3', 'system:user:deleteBatch', '/admin/user/deleteBatch', '2', '2018-04-01 14:11:41', '1');
INSERT INTO `tb_resource` VALUES ('5', '2018-03-01 16:48:48', '用户授权', null, '0', '3', '用户授权', '4', 'system:user:grant', '/admin/user/grant/**', '2', '2018-04-01 14:11:51', '1');
INSERT INTO `tb_resource` VALUES ('6', '2018-03-01 16:45:10', '角色管理', null, '0', '2', '角色管理', '2', 'system:role:index', '/admin/role/index', '1', '2018-04-01 16:46:52', null);
INSERT INTO `tb_resource` VALUES ('7', '2018-03-01 16:47:02', '角色修改', null, '0', '3', '角色修改', '1', 'system:role:edit', '/admin/role/edit*', '2', '2018-04-01 10:24:06', '6');
INSERT INTO `tb_resource` VALUES ('8', '2018-03-01 16:47:23', '角色添加', null, '0', '3', '角色添加', '2', 'system:role:add', '/admin/role/add', '2', '2018-04-01 16:49:16', '6');
INSERT INTO `tb_resource` VALUES ('9', '2018-03-01 16:47:23', '角色删除', null, '0', '3', '角色删除', '3', 'system:role:deleteBatch', '/admin/role/deleteBatch', '2', '2018-04-01 14:12:03', '6');
INSERT INTO `tb_resource` VALUES ('10', '2018-03-01 16:47:23', '角色授权', null, '0', '3', '角色授权', '4', 'system:role:grant', '/admin/role/grant/**', '2', '2018-04-01 14:12:11', '6');
INSERT INTO `tb_resource` VALUES ('11', '2018-03-01 11:21:12', '资源管理', null, '0', '2', '资源管理', '3', 'system:resource:index', '/admin/resource/index', '1', '2018-04-01 11:21:42', null);
INSERT INTO `tb_resource` VALUES ('12', '2018-03-01 11:21:52', '资源修改', null, '0', '3', '资源修改', '1', 'system:resource:edit', '/admin/resource/edit*', '2', '2018-04-01 11:22:36', '11');
INSERT INTO `tb_resource` VALUES ('13', '2018-03-01 11:21:54', '资源添加', null, '0', '3', '资源添加', '2', 'system:resource:add', '/admin/resource/add', '2', '2018-04-01 11:22:39', '11');
INSERT INTO `tb_resource` VALUES ('14', '2018-03-01 11:21:54', '资源删除', null, '0', '3', '资源删除', '3', 'system:resource:deleteBatch', '/admin/resource/deleteBatch', '2', '2018-04-01 14:12:31', '11');
INSERT INTO `tb_resource` VALUES ('15', '2018-03-01 11:21:54', '图书管理', null, '0', '2', '图书管理', '4', 'system:books:book_management', '/admin/books/book_management', '1', '2018-04-01 14:12:31', null);
INSERT INTO `tb_resource` VALUES ('16', '2018-03-01 09:45:58', 'SQL监控', null, '0', '2', 'SQL监控', '2', 'system:resource:druid', '/druid', '1', '2018-04-01 09:45:58', null);
INSERT INTO `tb_resource` VALUES ('17', '2018-04-02 11:37:23', '用户恢复', null, '0', '3', '用户恢复', '2', 'system:user:resume', '/admin/user/resume', '2', '2018-04-02 11:37:23', '1');
INSERT INTO `tb_resource` VALUES ('18', '2018-05-02 08:28:58', '备忘记录', null, '0', '2', '备忘记录', '2', 'system:memorandum:memorandum', '/admin/memorandum/*', '1', '2018-05-02 08:28:58', null);

-- ----------------------------
-- Table structure for `tb_role`
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `role_key` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES ('1', '2018-03-20 17:25:30', '管理员', 'administrator', 'administrator', '0', '2018-04-09 17:26:25');
INSERT INTO `tb_role` VALUES ('2', '2018-03-20 18:50:05', '学生', 'student', 'student', '0', '2018-03-25 17:26:27');
INSERT INTO `tb_role` VALUES ('5', '2018-05-02 19:57:57', 'ceshi', 'test', '测试用的', '0', '2018-05-02 19:57:57');

-- ----------------------------
-- Table structure for `tb_role_resource`
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_resource`;
CREATE TABLE `tb_role_resource` (
  `role_id` int(11) NOT NULL,
  `resource_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`,`resource_id`),
  KEY `FK868kc8iic48ilv5npa80ut6qo` (`resource_id`),
  CONSTRAINT `FK7ffc7h6obqxflhj1aq1mk20jk` FOREIGN KEY (`role_id`) REFERENCES `tb_role` (`id`),
  CONSTRAINT `FK868kc8iic48ilv5npa80ut6qo` FOREIGN KEY (`resource_id`) REFERENCES `tb_resource` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_role_resource
-- ----------------------------
INSERT INTO `tb_role_resource` VALUES ('1', '1');
INSERT INTO `tb_role_resource` VALUES ('5', '1');
INSERT INTO `tb_role_resource` VALUES ('1', '2');
INSERT INTO `tb_role_resource` VALUES ('5', '2');
INSERT INTO `tb_role_resource` VALUES ('1', '3');
INSERT INTO `tb_role_resource` VALUES ('5', '3');
INSERT INTO `tb_role_resource` VALUES ('1', '4');
INSERT INTO `tb_role_resource` VALUES ('5', '4');
INSERT INTO `tb_role_resource` VALUES ('1', '5');
INSERT INTO `tb_role_resource` VALUES ('5', '5');
INSERT INTO `tb_role_resource` VALUES ('1', '6');
INSERT INTO `tb_role_resource` VALUES ('5', '6');
INSERT INTO `tb_role_resource` VALUES ('1', '7');
INSERT INTO `tb_role_resource` VALUES ('5', '7');
INSERT INTO `tb_role_resource` VALUES ('1', '8');
INSERT INTO `tb_role_resource` VALUES ('5', '8');
INSERT INTO `tb_role_resource` VALUES ('1', '9');
INSERT INTO `tb_role_resource` VALUES ('5', '9');
INSERT INTO `tb_role_resource` VALUES ('1', '10');
INSERT INTO `tb_role_resource` VALUES ('5', '10');
INSERT INTO `tb_role_resource` VALUES ('1', '11');
INSERT INTO `tb_role_resource` VALUES ('1', '12');
INSERT INTO `tb_role_resource` VALUES ('1', '13');
INSERT INTO `tb_role_resource` VALUES ('1', '14');
INSERT INTO `tb_role_resource` VALUES ('1', '15');
INSERT INTO `tb_role_resource` VALUES ('5', '15');
INSERT INTO `tb_role_resource` VALUES ('1', '16');
INSERT INTO `tb_role_resource` VALUES ('5', '16');
INSERT INTO `tb_role_resource` VALUES ('1', '17');
INSERT INTO `tb_role_resource` VALUES ('5', '17');
INSERT INTO `tb_role_resource` VALUES ('1', '18');
INSERT INTO `tb_role_resource` VALUES ('5', '18');

-- ----------------------------
-- Table structure for `tb_user`
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_code` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `delete_status` int(11) DEFAULT NULL,
  `locked` int(11) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('1', 'admin', 'admin', '2017-01-09 17:26:41', '0', '0', '3931MUEQD1939MQMLM4AISPVNE', '13065120502', '2018-05-03 20:39:37');
INSERT INTO `tb_user` VALUES ('2', '1418140303', 'James', '2017-04-20 22:48:34', '1', '0', '3931MUEQD1939MQMLM4AISPVNE', '15820582502', '2018-05-03 20:39:57');
INSERT INTO `tb_user` VALUES ('12', '1418140304', 'Jon', '2017-05-04 19:31:10', '1', '0', '23QPTHDNUT5MB5UPK1RI1GA1IQ', '15825624789', '2018-04-04 10:37:09');
INSERT INTO `tb_user` VALUES ('25', '1526705079', '1111', '2018-05-02 19:55:23', '1', '0', '3931MUEQD1939MQMLM4AISPVNE', '13065120405', '2018-05-02 19:55:23');
INSERT INTO `tb_user` VALUES ('26', '1526705033', 'SB', '2018-05-02 19:57:38', '0', '0', '3931MUEQD1939MQMLM4AISPVNE', '13065120405', '2018-05-03 20:47:27');

-- ----------------------------
-- Table structure for `tb_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKea2ootw6b6bb0xt3ptl28bymv` (`role_id`),
  CONSTRAINT `FK7vn3h53d0tqdimm8cp45gc0kl` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`),
  CONSTRAINT `FKea2ootw6b6bb0xt3ptl28bymv` FOREIGN KEY (`role_id`) REFERENCES `tb_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user_role
-- ----------------------------
INSERT INTO `tb_user_role` VALUES ('1', '1');
INSERT INTO `tb_user_role` VALUES ('12', '1');
INSERT INTO `tb_user_role` VALUES ('2', '2');
INSERT INTO `tb_user_role` VALUES ('12', '2');
INSERT INTO `tb_user_role` VALUES ('14', '2');
INSERT INTO `tb_user_role` VALUES ('16', '2');
INSERT INTO `tb_user_role` VALUES ('19', '2');
INSERT INTO `tb_user_role` VALUES ('20', '2');
INSERT INTO `tb_user_role` VALUES ('21', '2');
INSERT INTO `tb_user_role` VALUES ('25', '2');
INSERT INTO `tb_user_role` VALUES ('26', '5');
