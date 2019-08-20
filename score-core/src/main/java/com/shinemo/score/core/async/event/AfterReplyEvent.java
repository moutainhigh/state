package com.shinemo.score.core.async.event;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AfterReplyEvent {

    private Long commentId;

    /**
     * 该回复是否含有敏感词
     */
    private boolean hasSensitive;

    private boolean del;

}