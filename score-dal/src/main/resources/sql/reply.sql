CREATE TABLE `reply`
(
  `id`           bigint(20)  NOT NULL AUTO_INCREMENT,
  `gmt_create`   datetime    NOT NULL,
  `gmt_modified` datetime    NOT NULL,
  `status`       int(11)     NOT NULL DEFAULT '1' COMMENT '删除状态 1-正常 0-删除',
  `comment_id`   bigint(20)  NOT NULL COMMENT '主评论id',
  `content`      blob        NOT NULL COMMENT '回复内容',
  `uid`          bigint(20)  NOT NULL COMMENT '用户id',
  `mobile`       varchar(30) NOT NULL DEFAULT '' COMMENT '手机号码',
  `name`         varchar(256)         DEFAULT NULL COMMENT '用户名称',
  `avatar_url`   varchar(256)         DEFAULT NULL COMMENT '头像',
  `device`       varchar(80)          DEFAULT NULL COMMENT '设备',
  `net_type`     varchar(80)          DEFAULT NULL COMMENT '网络类型',
  `extend`       text COMMENT '扩展字段',
  PRIMARY KEY (`id`),
  KEY `idx_c_u` (`comment_id`, `uid`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='回复表';


alter table `reply` add `full_device` varchar(256) DEFAULT  null comment '完整机型';
alter table `reply` add `flag` bigint(20) DEFAULT  '0' comment '评论标位';
alter table `reply` add `ip` varchar(60) DEFAULT  null comment 'ip地址';