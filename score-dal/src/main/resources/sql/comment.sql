CREATE TABLE `comment`
(
  `id`            bigint(20)          NOT NULL AUTO_INCREMENT,
  `gmt_create`    datetime            NOT NULL,
  `gmt_modified`  datetime            NOT NULL,
  `status`        int(11)             NOT NULL DEFAULT '1' COMMENT '删除状态 1-正常 0-删除',
  `video_id`      varchar(128)        NOT NULL DEFAULT '' COMMENT '外部视频id',
  `video_type`    int(11)             NOT NULL COMMENT '视频类型',
  `content`       blob                NOT NULL COMMENT '评论内容',
  `like_num`      bigint(20)          NOT NULL DEFAULT '0' COMMENT '赞数量',
  `uid`           bigint(20) unsigned NOT NULL COMMENT '用户id',
  `mobile`        varchar(30)         NOT NULL DEFAULT '' COMMENT '手机号码',
  `name`          varchar(256)                 DEFAULT NULL COMMENT '用户名称',
  `avatar_url`    varchar(256)                 DEFAULT NULL COMMENT '头像',
  `device`        varchar(80)                  DEFAULT NULL COMMENT '设备',
  `net_type`      varchar(80)                  DEFAULT NULL COMMENT '网络类型',
  `sort`          int(11)                      DEFAULT '0' COMMENT '排序权重',
  `extend`        text COMMENT '扩展字段',
  `history_reply` blob COMMENT '最近的回复(最近3条)',
  `reply_num`     int(11)             NOT NULL DEFAULT '0' COMMENT '回复的数量',
  `version`       bigint(20)          NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  KEY `idx_u` (`uid`) USING BTREE,
  KEY `idx_v_t` (`video_id`, `video_type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='评论表';


## 2期评论优化新增字段
alter table `comment` add `full_device` varchar(256) DEFAULT  null comment '完整机型';
alter table `comment` add `flag` bigint(20) DEFAULT  '0' comment '评论标位';
alter table `comment` add `ip` varchar(60) DEFAULT  null comment 'ip地址';

## 4.2
alter table comment add verify_status int(4) default '3' comment '评论审核状态 0-未审核 3-已审核 6-审核拒绝';
alter table comment add operate_uid bigint default null comment '操作者id';
alter table comment add operate_user_name varchar(32) default null comment '操作者名称';
alter table comment add key `idx_o_u`(`operate_uid`);