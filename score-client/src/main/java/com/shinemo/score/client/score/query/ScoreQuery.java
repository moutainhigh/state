package com.shinemo.score.client.score.query;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.shinemo.client.common.QueryBase;

/**
 * 查询类
 * @ClassName: ScoreQuery
 * @author zhangyan
 * @Date 2019-06-12 09:52:15
 */
@Getter
@Setter
public class ScoreQuery extends QueryBase {
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
    * 用户id
    */
	private Long uid;
    /**
    * 版本
    */
	private Long version;
	/**
	 * 第三方音频id
	 */
	private String thirdVideoId;
	/**
	 * 开始更新时间
	 */
	private Date startModifyTime;
	/**
	 * 结束更新时间
	 */
	private Date endModifyTime;
}
