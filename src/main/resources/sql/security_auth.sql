/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : security_auth

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 18/09/2021 20:49:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_permission`;
CREATE TABLE `tb_permission`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限标识符',
  `description` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述信息',
  `url` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_permission
-- ----------------------------
INSERT INTO `tb_permission` VALUES (1, 'sys:add', '增加', '');
INSERT INTO `tb_permission` VALUES (2, 'sys:delete:', '删除', NULL);
INSERT INTO `tb_permission` VALUES (3, 'sys:update', '修改', NULL);
INSERT INTO `tb_permission` VALUES (4, 'sys:select', '查询', NULL);

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色表id',
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述信息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_role_name`(`role_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES ('1', 'ROLE_ADMIN', '拥有管理员权限', '2021-09-07 14:48:43', '2021-09-07 14:48:45', '1');
INSERT INTO `tb_role` VALUES ('2', 'ROLE_USER', '普通用户', '2021-09-07 14:58:35', '2021-09-07 14:58:44', '1');
INSERT INTO `tb_role` VALUES ('3', 'ROLE_VISITOR', '游客', '2021-09-07 14:59:24', '2021-09-07 14:59:27', '1');

-- ----------------------------
-- Table structure for tb_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_permission`;
CREATE TABLE `tb_role_permission`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role_permission
-- ----------------------------
INSERT INTO `tb_role_permission` VALUES (1, '1', '1');
INSERT INTO `tb_role_permission` VALUES (2, '1', '2');
INSERT INTO `tb_role_permission` VALUES (3, '1', '3');
INSERT INTO `tb_role_permission` VALUES (4, '1', '4');
INSERT INTO `tb_role_permission` VALUES (5, '2', '1');
INSERT INTO `tb_role_permission` VALUES (6, '2', '2');
INSERT INTO `tb_role_permission` VALUES (7, '2', '4');
INSERT INTO `tb_role_permission` VALUES (8, '3', '4');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` bigint(20) NOT NULL COMMENT 'user表主键',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `fullname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'admin', '$2a$10$3g0ihkE.PoZQOs4xK3mQKeYEQtOFSe7dzqarhyiSth9lJCC2ZKvBa', '宁在春', '17670930119', '951930136@qq.com');
INSERT INTO `tb_user` VALUES (2, 'user', '$2a$10$9kH.xMSu8RV4QLyMDUEi0u8VzRO0P6g8naes8I5BHujZInaCwv3Hy', '青冬栗', '17670930114', '1185232242@qq.com');
INSERT INTO `tb_user` VALUES (3, 'visitor', '$2a$10$t62Jgr8XeabCXSzYto/2gOuxl4DdXB7K.qGuBurRdpGn8ViL7tlAy', '北桑夏', '17670930113', 'a_176709301555@163.com');

-- ----------------------------
-- Table structure for tb_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role`  (
  `id` int(11) NOT NULL,
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户表id',
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色表id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_role
-- ----------------------------
INSERT INTO `tb_user_role` VALUES (1, '1', '1', '2021-09-07 14:50:17', '管理员');
INSERT INTO `tb_user_role` VALUES (2, '2', '2', '2021-09-07 15:05:01', '用户自己注册');
INSERT INTO `tb_user_role` VALUES (3, '3', '3', '2021-09-07 15:05:29', '游客');

SET FOREIGN_KEY_CHECKS = 1;
