DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply`
(
  `id`           bigint       NOT NULL AUTO_INCREMENT,
  `gmt_create`   datetime     NOT NULL,
  `gmt_modified` datetime     NOT NULL,
  `status`       int          NOT NULL default '1' COMMENT '删除状态 1-正常 0-删除',
  `comment_id`   bigint       not null comment '主评论id',
  `content`      varchar(255) NOT NULL COMMENT '回复内容',
  `uid`          bigint       not null comment '用户id',
  `mobile`       varchar(30)       not null comment '手机号码',
  `name`         varchar(256) DEFAULT null comment '用户名称',
  `avatar_url`   varchar(256) DEFAULT null comment '头像',
  `device`       varchar(80)  DEFAULT null comment '设备',
  `net_type`     varchar(80)  DEFAULT null comment '网络类型',
  `extend`       text         DEFAULT null comment '扩展字段',
  PRIMARY KEY (`id`),
  key `idx_c_u` (`comment_id`, `uid`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='回复表';