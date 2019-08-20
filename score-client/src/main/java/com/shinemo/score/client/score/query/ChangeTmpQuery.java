package com.shinemo.score.client.score.query;


import lombok.Getter;
import lombok.Setter;

import com.shinemo.client.common.QueryBase;

/**
 * 查询类
 * @ClassName: ChangeTmpQuery
 * @author zhangyan
 * @Date 2019-08-20 21:33:29
 */
@Getter
@Setter
public class ChangeTmpQuery extends QueryBase {
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
