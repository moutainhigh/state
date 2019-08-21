package com.shinemo.score.client.score.query;


import lombok.Getter;
import lombok.Setter;

import com.shinemo.client.common.QueryBase;

/**
 * 查询类
 * @ClassName: VideoTmpQuery
 * @author zhangyan
 * @Date 2019-08-20 21:33:29
 */
@Getter
@Setter
public class VideoTmpQuery extends QueryBase{

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
