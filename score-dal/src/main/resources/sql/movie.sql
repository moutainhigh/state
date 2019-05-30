DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create`   datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `status` int  NOT NULL COMMENT '删除状态 1-正常 0-删除',
  `movie_id` varchar(128) not null comment '外部电影id',
  `movie_name` varchar(128) not null comment '电影名称',
  `init_score` int(4) not null comment '初始分数',
  `init_weight` bigint not null comment '初始权重(人数)',
  `score` int(4) not null comment '当前电影分数',
  `weight` int(4) not null comment '当前权重',
  `yesterday_score` int(4) not null comment '昨日分数',
  `extend` text DEFAULT null comment '电影扩展信息',
  `version` bigint not null comment '版本',
  PRIMARY KEY (`id`),
  UNIQUE key `uk_m_id`(`movie_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='电影基础信息表';