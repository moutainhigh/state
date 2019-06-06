package com.shinemo.score.client.reply.query;


import lombok.Getter;
import lombok.Setter;

import com.shinemo.client.common.QueryBase;

/**
 * 查询类
 * @ClassName: ReplyQuery
 * @author wenchao.li
 * @Date 2019-06-06 10:06:20
 */
@Getter
@Setter
public class ReplyQuery extends QueryBase {

	private Long id;
    /**
    * 删除状态 1-正常 0-删除
    */
	private Integer status;
    /**
    * 主评论id
    */
	private Long commentId;
    /**
    * 用户id
    */
	private Long uid;
    /**
    * 手机号码
    */
	private Long mobile;
    /**
    * 用户名称
    */
	private String name;
    /**
    * 设备
    */
	private String device;
    /**
    * 网络类型
    */
	private String netType;
}
