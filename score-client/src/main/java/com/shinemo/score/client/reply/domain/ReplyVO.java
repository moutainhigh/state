package com.shinemo.score.client.reply.domain;

import com.shinemo.client.common.BaseDO;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.score.client.utils.RegularUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;


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

    private Long gmtCreate;

    private String netType;

    private String mobile;

    private String device;

    private boolean isMine;

    private boolean hasSensitive;

    private Long uid;

    public ReplyVO(ReplyDO replyDO, Long currentUid) {
        replyId = replyDO.getId();

        gmtCreate = replyDO.getGmtCreate().getTime();
        userName = replyDO.getName() + RegularUtils.ignorePhone(replyDO.getMobile());
        userPortrait = replyDO.getAvatarUrl();
        netType = replyDO.getNetType();
        device = replyDO.getDevice();
        mobile = RegularUtils.ignorePhone(replyDO.getMobile());
        isMine = replyDO.getUid().equals(currentUid);
        hasSensitive = replyDO.hasSensitiveWord();
        uid = replyDO.getUid();

        if (hasSensitive && StringUtils.isNotBlank(replyDO.getExtend())) {
            ReplyExtend replyExtend = GsonUtil.fromGson2Obj(replyDO.getExtend(), ReplyExtend.class);
            content = replyExtend.getSensitiveContent();
        } else {
            content = replyDO.getContent();
        }
    }
}
