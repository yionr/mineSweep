/*
 Navicat Premium Data Transfer

 Source Server         : ecs
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : yionr.cn:3306
 Source Schema         : mineSweep

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 02/12/2020 10:36:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ranking
-- ----------------------------
DROP TABLE IF EXISTS `ranking`;
CREATE TABLE `ranking`  (
  `time` int(11) NOT NULL,
  `mine` int(11) NOT NULL,
  `rank` int(11) NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
