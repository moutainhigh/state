DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`
(
  `id`            bigint       NOT NULL AUTO_INCREMENT,
  `gmt_create`    datetime     NOT NULL,
  `gmt_modified`  datetime     NOT NULL,
  `status`        int          NOT NULL COMMENT '删除状态 1-正常 0-删除',
  `video_id`      bigint       not null comment '电影id',
  `video_type`    int          NOT NULL COMMENT '视频类型',
  `content`       varchar(255) NOT NULL COMMENT '评论内容',
  `like_num`      bigint       NOT NULL DEFAULT '0' COMMENT '赞数量',
  `uid`           bigint       not null comment '用户id',
  `mobile`        varchar(30)  not null comment '手机号码',
  `name`          varchar(256)          DEFAULT null comment '用户名称',
  `avatar_url`    varchar(256)          DEFAULT null comment '头像',
  `device`        varchar(80)           DEFAULT null comment '设备',
  `net_type`      varchar(80)           DEFAULT null comment '网络类型',
  `sort`          int                   default '0' comment '排序权重',
  `extend`        text                  DEFAULT null comment '扩展字段',
  `history_reply` text                  DEFAULT null comment '最近的回复(最近3条)',
  `version`       bigint       not null default '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  KEY `idx_u` (`uid`) USING BTREE,
  KEY `idx_v_t` (`video_id`, `video_type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='评论表';