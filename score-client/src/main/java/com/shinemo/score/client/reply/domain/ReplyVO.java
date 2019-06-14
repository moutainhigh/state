package com.shinemo.score.client.reply.domain;

import com.shinemo.client.common.BaseDO;
import com.shinemo.score.client.utils.RegularUtils;
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

    private String netType;

    private String mobile;

    private String device;


    public ReplyVO(ReplyDO replyDO) {
        replyId = replyDO.getId();
        content = replyDO.getContent();
        gmtCreate = replyDO.getGmtCreate();
        userName = replyDO.getName() + RegularUtils.ignorePhone(replyDO.getMobile());
        userPortrait = replyDO.getAvatarUrl();
        netType = replyDO.getNetType();
        device = replyDO.getDevice();
        mobile = RegularUtils.ignorePhone(replyDO.getMobile());
    }
}
