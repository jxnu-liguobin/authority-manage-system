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
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES ('1', '2018-03-20 17:25:30', '管理员', 'administrator', 'administrator', '0', '2018-04-09 17:26:25');
INSERT INTO `tb_role` VALUES ('2', '2018-03-20 18:50:05', '学生', 'student', 'student', '0', '2018-03-25 17:26:27');

-- ----------------------------
-- Records of tb_role_resource
-- ----------------------------
INSERT INTO `tb_role_resource` VALUES ('1', '1');
INSERT INTO `tb_role_resource` VALUES ('1', '2');
INSERT INTO `tb_role_resource` VALUES ('1', '3');
INSERT INTO `tb_role_resource` VALUES ('1', '4');
INSERT INTO `tb_role_resource` VALUES ('1', '5');
INSERT INTO `tb_role_resource` VALUES ('1', '6');
INSERT INTO `tb_role_resource` VALUES ('1', '7');
INSERT INTO `tb_role_resource` VALUES ('1', '8');
INSERT INTO `tb_role_resource` VALUES ('1', '9');
INSERT INTO `tb_role_resource` VALUES ('1', '10');
INSERT INTO `tb_role_resource` VALUES ('1', '11');
INSERT INTO `tb_role_resource` VALUES ('1', '12');
INSERT INTO `tb_role_resource` VALUES ('1', '13');
INSERT INTO `tb_role_resource` VALUES ('1', '14');
INSERT INTO `tb_role_resource` VALUES ('1', '15');
INSERT INTO `tb_role_resource` VALUES ('1', '16');
INSERT INTO `tb_role_resource` VALUES ('1', '17');
INSERT INTO `tb_role_resource` VALUES ('1', '18');

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('1', 'admin', 'admin', '2017-01-09 17:26:41', '0', '0', 'S6557FPFLS0IL5VMMIA5AMUBJ', '13065120502', '2018-04-10 11:16:45');
INSERT INTO `tb_user` VALUES ('2', '1418140303', 'James', '2017-04-20 22:48:34', '0', '0', '37UKUUGEAAS613NRTV84V1409O', '15820582502', '2018-04-10 11:13:01');
INSERT INTO `tb_user` VALUES ('12', '1418140304', 'Jon', '2017-05-04 19:31:10', '0', '0', '23QPTHDNUT5MB5UPK1RI1GA1IQ', '15825624789', '2018-04-04 10:37:09');
INSERT INTO `tb_user` VALUES ('15', '1526705078', 'LGG', '2018-04-03 11:59:38', '0', '0', '3IH0UBLIE3JLE6UUF9OJPS16UM', '13065120502', '2018-04-10 10:25:00');

-- ----------------------------
-- Records of tb_user_role
-- ----------------------------
INSERT INTO `tb_user_role` VALUES ('1', '1');
INSERT INTO `tb_user_role` VALUES ('12', '1');
INSERT INTO `tb_user_role` VALUES ('2', '2');
INSERT INTO `tb_user_role` VALUES ('12', '2');
INSERT INTO `tb_user_role` VALUES ('14', '2');
INSERT INTO `tb_user_role` VALUES ('15', '2');
INSERT INTO `tb_user_role` VALUES ('16', '2');
INSERT INTO `tb_user_role` VALUES ('19', '2');
INSERT INTO `tb_user_role` VALUES ('20', '2');
INSERT INTO `tb_user_role` VALUES ('21', '2');

-- ----------------------------
-- Records of tb_memorandum
-- ----------------------------
INSERT INTO `tb_memorandum` VALUES ('1', '2018-04-03 11:59:38','admin','admin','图书修改');

