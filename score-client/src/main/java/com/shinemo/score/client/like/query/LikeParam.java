package com.shinemo.score.client.like.query;

import com.shinemo.client.common.BaseDO;
import lombok.Data;

/**
 * @author wenchao.li
 * @since 2019-06-12
 */
@Data
public class LikeParam extends BaseDO {

    private Integer opType;

    private Long commentId;
}
