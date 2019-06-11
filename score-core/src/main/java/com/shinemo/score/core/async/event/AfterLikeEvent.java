package com.shinemo.score.core.async.event;

import lombok.Builder;
import lombok.Data;

/**
 * @author wenchao.li
 * @since 2019-06-11
 */
@Data
@Builder
public class AfterLikeEvent {

    private Long commentId;

    private Integer likeAction;
}
