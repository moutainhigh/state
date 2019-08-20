package com.shinemo.score.client.score.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.shinemo.client.common.BaseDO;


/**
 * 实体类
 * @ClassName: ScoreTemp
 * @author zhangyan
 * @Date 2019-08-20 21:33:29
 */
@Getter
@Setter
public class ScoreTemp extends BaseDO {
	private Long id;
	private Date gmtCreate;
	private Date gmtModified;
	/**
	* 删除状态 1-正常 0-删除
	*/
	private Integer status;
	/**
	* 视频表的主键id
	*/
	private Long videoId;
	/**
	* 评分
	*/
	private Integer score;
	/**
	* 用户id
	*/
	private Long uid;
	/**
	* 扩展字段
	*/
	private String extend;
	/**
	* 版本
	*/
	private Long version;
	/**
	* 第几部电影
	*/
	private Long num;
	/**
	* 外部电影id冗余
	*/
	private String thirdVideoId;
}
