package com.shinemo.score.client.like.query;

import lombok.Data;

/**
 * @author wenchao.li
 * @since 2019-06-11
 */
@Data
public class LikeRequest {

    private Integer likeAction;

    private Long commentId;

    private Long uid;

}
