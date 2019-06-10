package com.shinemo.score.client.error;

import com.shinemo.client.common.ErrorInfo;

/**
 * @author wenchao.li
 * @since 2019-06-06
 */
public interface ScoreErrors {

    ErrorInfo COMMENT_NOT_EXIST = new ErrorInfo(100000L, "COMMENT_NOT_EXIST", "评论不存在");
    ErrorInfo REPLY_NOT_EXIST = new ErrorInfo(100001L, "REPLY_NOT_EXIST", "回复不存在");


}
