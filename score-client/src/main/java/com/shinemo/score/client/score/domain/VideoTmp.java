package com.shinemo.score.client.score.domain;


import lombok.Getter;
import lombok.Setter;

import com.shinemo.client.common.BaseDO;


/**
 * 实体类
 * @ClassName: VideoTmp
 * @author zhangyan
 * @Date 2019-08-20 21:33:29
 */
@Getter
@Setter
public class VideoTmp extends BaseDO {
	private Long id;
	/**
	* 分数
	*/
	private Float score;
	/**
	* 音频id
	*/
	private String videoId;
	/**
	* 权重
	*/
	private Long weight;

	private String videoName;
	private String xmVideoId;
}
