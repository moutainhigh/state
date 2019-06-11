package com.shinemo.score.client.reply.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author wenchao.li
 * @since 2019-06-11
 */
@Data
public class ReplyVO {

    private Long replyId;

    private String content;

    private String userName;

    private String userPortrait;

    private Date gmtCreate;
}
