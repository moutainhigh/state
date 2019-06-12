package com.shinemo.score.client.reply.domain;

import com.shinemo.client.common.BaseDO;
import lombok.Data;

import java.util.Date;

/**
 * @author wenchao.li
 * @since 2019-06-11
 */
@Data
public class ReplyVO extends BaseDO {

    private Long replyId;

    private String content;

    private String userName;

    private String userPortrait;

    private Date gmtCreate;

    public ReplyVO(ReplyDO replyDO){
        replyId = replyDO.getId();
        content = replyDO.getContent();
        gmtCreate = replyDO.getGmtCreate();
        userName = replyDO.getName();
        userPortrait = replyDO.getAvatarUrl();
    }
}
