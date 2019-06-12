package com.shinemo.score.client.comment.query;

import lombok.Data;

/**
 * @author wenchao.li
 * @since 2019-06-12
 */
@Data
public class CommentParam {

    private Long commentId;
    private String comment;
    private Long videoId;
    private String netType;
    private Integer videoType;
    private String extend;
}
