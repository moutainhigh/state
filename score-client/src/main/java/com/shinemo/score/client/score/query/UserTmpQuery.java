package com.shinemo.score.client.score.query;


import lombok.Getter;
import lombok.Setter;

import com.shinemo.client.common.QueryBase;

/**
 * 查询类
 * @ClassName: UserTmpQuery
 * @author zhangyan
 * @Date 2019-08-21 11:50:36
 */
@Getter
@Setter
public class UserTmpQuery extends QueryBase {
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
