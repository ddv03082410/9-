/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80013
 Source Host           : localhost:3306
 Source Schema         : xsxk

 Target Server Type    : MySQL
 Target Server Version : 80013
 File Encoding         : 65001

 Date: 05/09/2024 11:56:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', 'admin', '管理员', 'http://localhost:9090/files/download/avatar.png', 'ADMIN');

-- ----------------------------
-- Table structure for choice
-- ----------------------------
DROP TABLE IF EXISTS `choice`;
CREATE TABLE `choice`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '课程名称',
  `teacher_id` int(10) NULL DEFAULT NULL COMMENT '授课教师',
  `student_id` int(10) NULL DEFAULT NULL COMMENT '选课学生',
  `course_id` int(10) NULL DEFAULT NULL COMMENT '课程ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '选课信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of choice
-- ----------------------------
INSERT INTO `choice` VALUES (5, 'Java基础', 3, 2, 2);
INSERT INTO `choice` VALUES (6, 'python基础', 6, 2, 1);
INSERT INTO `choice` VALUES (8, 'Java基础', 3, 4, 2);
INSERT INTO `choice` VALUES (9, 'Go语言', 3, 4, 4);
INSERT INTO `choice` VALUES (11, 'python基础', 6, 4, 1);

-- ----------------------------
-- Table structure for college
-- ----------------------------
DROP TABLE IF EXISTS `college`;
CREATE TABLE `college`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学院名称',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '学院描述',
  `score` int(10) NULL DEFAULT NULL COMMENT '最低学分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学院信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of college
-- ----------------------------
INSERT INTO `college` VALUES (1, '物流工程学院', '这是物流工程学院，牛逼', 30);
INSERT INTO `college` VALUES (2, '经济管理学院', '这是经济管理学院，厉害厉害', 40);
INSERT INTO `college` VALUES (6, '马克思主义学院', '	\n这是马克思主义学院，牛逼plus', 30);
INSERT INTO `college` VALUES (7, '软件学院', '牛逼plus', 40);

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '课程名称',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '课程介绍',
  `credit` decimal(3,1) NULL DEFAULT 2.0 COMMENT '课程学分',
  `teacher_id` int(10) NULL DEFAULT NULL COMMENT '授课教师',
  `num` int(10) NULL DEFAULT NULL COMMENT '开班人数',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '上课时间',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '上课地点',
  `college_id` int(10) NULL DEFAULT NULL COMMENT '所属学院',
  `speciality_id` int(10) NULL DEFAULT NULL COMMENT '专业ID',
  `is_general` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否通识课',
  `already_num` int(10) NULL DEFAULT 0 COMMENT '已选人数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '课程信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` (`id`, `name`, `content`, `credit`, `teacher_id`, `num`, `time`, `location`, `college_id`, `speciality_id`, `is_general`, `already_num`) VALUES
(1, 'python基础', '学习python基础，夯实基础。', 3.0, 6, 30, '周二第四大节', '2B202', 7, NULL, 1, 2),
(2, 'Java基础', '学习Java基础，为项目开发打好基础。', 3.0, 3, 30, '周三第四大节', '3B303', 7, 2, 0, 2),
(3, 'C语言', '学习C语言', 3.0, 4, 40, '周五第一大节', '4B404', 7, 1, 0, 0),
(4, 'Go语言', '这是Go语言', 4.0, 3, 40, '周二第三大节', '2B203', 7, 2, 0, 1),
(5, '人工智能导论', '多学科交叉，帮助打牢AI基础认知。', 3.0, 3, 45, '周一第三大节', 'AI201', 7, NULL, 1, 0),
(6, '深度学习实践', 'TensorFlow与PyTorch项目驱动式训练。', 4.0, 6, 40, '周六第二大节', 'AI302', 7, 1, 0, 0),
(7, '云原生架构', 'Kubernetes、Docker、DevOps全流程演练。', 3.5, 4, 50, '周三第一大节', 'Cloud101', 7, 2, 0, 0),
(8, '网络安全攻防', '渗透测试与攻防演练实验。', 3.0, 6, 35, '周四第三大节', 'SecLab', 7, 1, 0, 0),
(9, '并行计算', 'MPI、OpenMP与GPU算力综合实战。', 3.5, 4, 35, '周五第二大节', 'ComputeLab', 7, 1, 0, 0);

-- ----------------------------
-- Table structure for course_prerequisite
-- ----------------------------
DROP TABLE IF EXISTS `course_prerequisite`;
CREATE TABLE `course_prerequisite`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(10) NOT NULL,
  `prerequisite_id` int(10) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_course`(`course_id`),
  KEY `idx_prerequisite`(`prerequisite_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '课程前置要求' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_prerequisite
-- ----------------------------
INSERT INTO `course_prerequisite` VALUES (1, 5, 1);
INSERT INTO `course_prerequisite` VALUES (2, 5, 2);
INSERT INTO `course_prerequisite` VALUES (3, 6, 1);
INSERT INTO `course_prerequisite` VALUES (4, 6, 5);
INSERT INTO `course_prerequisite` VALUES (5, 7, 2);
INSERT INTO `course_prerequisite` VALUES (6, 8, 2);
INSERT INTO `course_prerequisite` VALUES (7, 8, 3);
INSERT INTO `course_prerequisite` VALUES (8, 9, 3);
INSERT INTO `course_prerequisite` VALUES (9, 9, 4);

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '公告内容',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (1, '关于最新选课通知', '选课快要开始了，大家请及时选课，注意自己的学分，过时不候', '2024-07-30 17:20:59');
INSERT INTO `notice` VALUES (2, '学校关于选课作弊的通告', '选课奔着公平公正原则，大家切勿利用任何方式作弊，一旦发现，记过处理。', '2024-07-30 17:21:17');
INSERT INTO `notice` VALUES (3, '最新选课管理系统上线啦！', '最新版选课管理系统经过内测，完美上线！好开森！！', '2024-07-30 17:21:34');

-- ----------------------------
-- Table structure for speciality
-- ----------------------------
DROP TABLE IF EXISTS `speciality`;
CREATE TABLE `speciality`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '专业名称',
  `college_id` int(10) NULL DEFAULT NULL COMMENT '学院ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '专业信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of speciality
-- ----------------------------
INSERT INTO `speciality` VALUES (1, '计算机科学与技术', 7);
INSERT INTO `speciality` VALUES (2, '软件工程', 7);
INSERT INTO `speciality` VALUES (4, '马克思主义哲学', 6);

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学号',
  `college_id` int(10) NULL DEFAULT NULL COMMENT '学院ID',
  `speciality_id` int(10) NULL DEFAULT NULL COMMENT '专业ID',
  `score` int(10) NULL DEFAULT 0 COMMENT '学分',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学生信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` (`id`, `username`, `password`, `name`, `role`, `sex`, `code`, `college_id`, `speciality_id`, `score`, `avatar`) VALUES
(1, 'zhangsan', '123456', '张三', 'STUDENT', '男', '10000', 7, 1, 0, 'http://localhost:9091/files/download/1722324698822-柴犬.jpeg'),
(2, 'lisi', '123456', '李四', 'STUDENT', '男', '10001', 7, 2, 3, 'http://localhost:9091/files/download/1722324706756-拉布拉多.jpeg'),
(3, 'wangwu', '123456', '王五', 'STUDENT', '男', '10002', 7, 1, 0, 'http://localhost:9091/files/download/1722324732342-柯基.jpeg'),
(4, 'zhaoliu', '123', '赵六', 'STUDENT', '男', '10003', 7, 2, 7, 'http://localhost:9091/files/download/1722329105934-金毛.jpeg'),
(5, 'liqi', '123456', '李七', 'STUDENT', '女', '10004', 6, 4, 0, 'http://localhost:9091/files/download/1722329141458-柯基.jpeg');

-- ----------------------------
-- Table structure for student_completed_course
-- ----------------------------
DROP TABLE IF EXISTS `student_completed_course`;
CREATE TABLE `student_completed_course`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` int(10) NOT NULL COMMENT '学生ID',
  `course_id` int(10) NOT NULL COMMENT '课程ID',
  `completed_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '完成时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_scc_student`(`student_id`),
  KEY `idx_scc_course`(`course_id`),
  CONSTRAINT `fk_scc_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_scc_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学生已修课程' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student_completed_course
-- ----------------------------
INSERT INTO `student_completed_course` VALUES (1, 4, 2, '2024-08-01 10:00:00');
INSERT INTO `student_completed_course` VALUES (2, 4, 4, '2024-08-15 10:00:00');
INSERT INTO `student_completed_course` VALUES (3, 2, 1, '2024-07-10 09:00:00');

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职称',
  `speciality_id` int(10) NULL DEFAULT NULL COMMENT '专业ID',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '教师信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (3, 'zhang', '123456', '张老师', '女', '教授', 1, 'TEACHER', 'http://localhost:9091/files/download/1722322696451-柴犬.jpeg');
INSERT INTO `teacher` VALUES (4, 'li', '123', '李老师', '男', '副教授', 2, 'TEACHER', 'http://localhost:9091/files/download/1722323069575-柯基.jpeg');
INSERT INTO `teacher` VALUES (6, 'wang', '123456', '王老师', '男', '副教授', 1, 'TEACHER', 'http://localhost:9091/files/download/1722323075913-拉布拉多.jpeg');

SET FOREIGN_KEY_CHECKS = 1;
