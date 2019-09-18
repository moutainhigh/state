package com.shinemo.score.client.score.domain;

import lombok.Data;

/**
 * @author wenchao.li
 * @since 2019-06-17
 */
@Data
public class ScoreDTO {

    private Long commentId;

    // 评论内容
    private String content;
    /**
     * 评论模式
     */
    private Integer commentConfig;
    /**
     * 提示语
     */
    private String tipsMsg;
}
