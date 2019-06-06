package com.shinemo.score.client.comment.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.shinemo.client.common.BaseDO;


/**
 * 实体类
 * @ClassName: CommentDO
 * @author wenchao.li
 * @Date 2019-06-06 10:06:20
 */
@Getter
@Setter
public class CommentDO extends BaseDO {
	private Long id;
	private Date gmtCreate;
	private Date gmtModified;
	/**
	* 删除状态 1-正常 0-删除
	*/
	private Integer status;
	/**
	* 外部视频id
	*/
	private String videoId;
	/**
	* 视频类型
	*/
	private Integer videoType;
	/**
	* 评论内容
	*/
	private String content;
	/**
	* 赞数量
	*/
	private Long likeNum;
	/**
	* 用户id
	*/
	private Long uid;
	/**
	* 手机号码
	*/
	private String mobile;
	/**
	* 用户名称
	*/
	private String name;
	/**
	* 头像
	*/
	private String avatarUrl;
	/**
	* 设备
	*/
	private String device;
	/**
	* 网络类型
	*/
	private String netType;
	/**
	* 排序权重
	*/
	private Integer sort;
	/**
	* 扩展字段
	*/
	private String extend;
	/**
	* 最近的回复(最近3条)
	*/
	private String historyReply;
	/**
	* 版本号
	*/
	private Long version;
}
