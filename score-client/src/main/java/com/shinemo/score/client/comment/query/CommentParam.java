package com.shinemo.score.client.comment.query;

import com.shinemo.client.common.BaseDO;
import lombok.Data;

/**
 * @author wenchao.li
 * @since 2019-06-12
 */
@Data
public class CommentParam extends BaseDO {

    private String comment;
    private String videoId;
    private String netType;
    private Integer videoType;

    private Integer commentId;
}
