DROP TABLE IF EXISTS `like_log`;
CREATE TABLE `like_log`
(
  `id`           bigint   NOT NULL AUTO_INCREMENT,
  `gmt_create`   datetime NOT NULL,
  `gmt_modified` datetime NOT NULL,
  `comment_id`   bigint   not null comment '主评论id',
  `uid`          bigint   not null comment '用户id',
  `type`         int      not null comment '1.赞 2.取消赞',
  PRIMARY KEY (`id`),
  UNIQUE key `uk_c_u` (`comment_id`, `uid`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='赞记录表';