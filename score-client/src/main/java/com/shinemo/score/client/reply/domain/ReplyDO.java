package com.shinemo.score.client.reply.domain;

import java.util.Date;

import com.shinemo.client.common.FlagHelper;
import com.shinemo.score.client.comment.domain.CommentFlag;
import lombok.Getter;
import lombok.Setter;

import com.shinemo.client.common.BaseDO;


/**
 * 实体类
 * @ClassName: ReplyDO
 * @author wenchao.li
 * @Date 2019-06-06 10:06:20
 */
@Getter
@Setter
public class ReplyDO extends BaseDO {
	private Long id;
	private Date gmtCreate;
	private Date gmtModified;
	/**
	* 删除状态 1-正常 0-删除
	*/
	private Integer status;
	/**
	* 主评论id
	*/
	private Long commentId;
	/**
	* 回复内容
	*/
	private String content;
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
	* 扩展字段
	*/
	private String extend;

	private String ip;

	/**
	 * 完整机型
	 */
	private String fullDevice;
	/**
	 * 打标
	 * @see CommentFlag
	 */
	private Long flag;

	private FlagHelper replyFlag = FlagHelper.build();

	/**
	 *
	 * @return 是否含有敏感词
	 */
	public boolean hasSensitiveWord() {

		if (flag == null) {
			return false;
		}
		return FlagHelper.hasFlag(flag,CommentFlag.HAS_SENSITIVE);
	}
}
