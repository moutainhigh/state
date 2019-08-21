package com.shinemo.score.core.async.event;

import lombok.Builder;
import lombok.Data;

/**
 * @author wenchao.li
 * @since 2019-08-21
 */
@Data
@Builder
public class AfterDeleteCommentEvent {

    private Long commentId;

}
