DROP TABLE IF EXISTS `video`;
CREATE TABLE `video` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create`   datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `status` int  NOT NULL COMMENT '删除状态 1-正常 0-删除',
  `video_id` varchar(128) not null comment '音频id',
  `video_type` int(4) not null comment '音频类型',
  `video_name` varchar(128) not null comment '音频名称',
  `init_score` bigint DEFAULT null comment '初始分数',
  `init_weight` bigint DEFAULT null comment '初始权重(人数)',
  `score`  bigint DEFAULT null comment '当前电影分数',
  `weight` bigint DEFAULT null comment '当前权重',
  `yesterday_score` bigint DEFAULT null comment '昨日分数',
  `yesterday_weight` bigint DEFAULT null comment '昨日分数',
  `extend` text DEFAULT null comment '电影扩展信息',
  `version` bigint not null comment '版本',
  PRIMARY KEY (`id`),
  UNIQUE key `uk_m_id`(`video_id`,`video_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='音频基础信息表';