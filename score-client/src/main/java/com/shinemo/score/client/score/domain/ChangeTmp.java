package com.shinemo.score.client.score.domain;


import lombok.Getter;
import lombok.Setter;

import com.shinemo.client.common.BaseDO;


/**
 * 实体类
 * @ClassName: ChangeTmp
 * @author zhangyan
 * @Date 2019-08-20 21:33:29
 */
@Getter
@Setter
public class ChangeTmp extends BaseDO {
	private Long id;
	/**
	* 音频id
	*/
	private String videoId;
	/**
	* 音频id
	*/
	private String thirdVideoId;
	/**
	* 名字
	*/
	private String name;
}
