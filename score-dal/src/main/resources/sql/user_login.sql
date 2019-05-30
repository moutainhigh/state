DROP TABLE IF EXISTS `user_login`;
CREATE TABLE `user_login` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create`   datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `status` int  NOT NULL COMMENT '删除状态 1-正常 0-删除',
  `uid`   bigint not null comment 'user_info表的主键id',
  `device_id` varchar(256) not null comment '设备id',
  `biz_token` varchar(256) not null comment '业务token',
  `last_login_time` datetime DEFAULT NULL comment '最后登录时间',
  PRIMARY KEY (`id`),
  UNIQUE key `uk_u_d`(`uid`,`device_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户登录表';