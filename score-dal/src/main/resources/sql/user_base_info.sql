DROP TABLE IF EXISTS `user_base_info`;
CREATE TABLE `user_base_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create`   datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `status` int  NOT NULL COMMENT '删除状态 1-正常 0-删除',
  `mobile`   bigint not null comment '手机号码',
  `name` varchar(256) DEFAULT null comment '用户名称',
  `third_uid` varchar(256) DEFAULT null comment '外部uid(暂时也是手机号)',
  `user_portrait` varchar(256) DEFAULT null comment '用户头像',
  `gender` int(1) DEFAULT null comment '性别 1-男 2-女',
  `extend` text DEFAULT null comment '扩展字段',
  PRIMARY KEY (`id`),
  UNIQUE key `uk_m_u`(`mobile`,`third_uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户基础信息表';