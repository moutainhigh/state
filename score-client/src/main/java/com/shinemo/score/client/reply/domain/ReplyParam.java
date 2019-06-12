package com.shinemo.score.client.reply.domain;

import lombok.Data;

/**
 * @author wenchao.li
 * @since 2019-06-12
 */
@Data
public class ReplyParam {

    private Long commentId;

    private String comment;

    private String netType;
}
