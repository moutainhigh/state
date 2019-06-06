package com.shinemo.score.client.like.query;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.shinemo.client.common.QueryBase;

/**
 * 查询类
 * @ClassName: LikeQuery
 * @author wenchao.li
 * @Date 2019-06-06 10:06:20
 */
@Getter
@Setter
public class LikeQuery extends QueryBase {
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
