## 举报、帮助、反馈

### 表结构设计

* 用户举报表
```sql
CREATE TABLE `user_report` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `gmt_create` datetime NOT NULL COMMENT '举报时间',
   `from_id` bigint(20) NOT NULL COMMENT '举报人id',
   `to_id` bigint(20) NOT NULL COMMENT '被举报人id',
   `comment_id` bigint(20) NOT NULL COMMENT '举报的评论id',
   `extend` varchar(255) default NULL COMMENT '详情,冗余',
	PRIMARY KEY (`id`),
	UNIQUE KEY `idx_f_t_cmt` (`from_id`, `to_id`, `comment_id`),
	KEY `idx_to` (`to_id`),
  KEY `idx_cmt` (`comment_id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户举报表';
```

* 问题反馈选项表
```sql
 CREATE TABLE `problem_item` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `gmt_create` datetime NOT NULL COMMENT '创建时间',
   `pid` bigint(20) default NULL COMMENT '父节点id',
   `name` varchar(50) NOT NULL COMMENT '节点名称',
   `level` tinyint(4) NOT NULL COMMENT '节点级别',
   `flag` bigint(20) NOT NULL COMMENT '状态'
	PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='问题反馈选项表';
```

* 问题反馈表
```sql
  CREATE TABLE `problem_report` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `gmt_create` datetime NOT NULL COMMENT '创建时间',
   `user_id` bigint(20) NOT NULL COMMENT '反馈人',
   `item_id` bigint(20) NOT NULL COMMENT '选项id',
   `context` varchar(255) NOT NULL COMMENT '文字说明',
   `email` varchar(50) default NULL COMMENT '邮箱',
   `extend` varchar(255) default NULL COMMENT '图片url等信息',
    PRIMARY KEY (`id`),
    KEY `idx_user` (`user_id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='问题反馈表';
```
