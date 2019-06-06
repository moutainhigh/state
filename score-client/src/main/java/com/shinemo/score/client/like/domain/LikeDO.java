package com.shinemo.score.client.like.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.shinemo.client.common.BaseDO;


/**
 * 实体类
 * @ClassName: LikeDO
 * @author wenchao.li
 * @Date 2019-06-06 10:06:20
 */
@Getter
@Setter
public class LikeDO extends BaseDO {
	private Long id;
	private Date gmtCreate;
	private Date gmtModified;
	/**
	* 主评论id
	*/
	private Long commentId;
	/**
	* 用户id
	*/
	private Long uid;
	/**
	* 1.赞 2.取消赞
	*/
	private Integer type;
}
