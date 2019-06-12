package com.shinemo.score.client.like.query;

import com.shinemo.client.common.BaseDO;
import lombok.Data;

/**
 * @author wenchao.li
 * @since 2019-06-11
 */
@Data
public class LikeRequest extends BaseDO {

    private Integer likeAction;

    private Long commentId;

    private Long uid;

}
