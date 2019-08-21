package com.shinemo.score.client.score.domain;


import lombok.Getter;
import lombok.Setter;

import com.shinemo.client.common.BaseDO;


/**
 * 实体类
 * @ClassName: UserTmp
 * @author zhangyan
 * @Date 2019-08-21 11:50:36
 */
@Getter
@Setter
public class UserTmp extends BaseDO {
	private Long id;
	/**
	* 手机号码
	*/
	private String mobile;
	/**
	* 音频id
	*/
	private String videoId;
	/**
	* 分数
	*/
	private Integer score;
}
