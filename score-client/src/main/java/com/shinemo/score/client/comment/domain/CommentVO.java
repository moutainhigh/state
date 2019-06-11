package com.shinemo.score.client.comment.domain;

import com.shinemo.client.common.BaseDO;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.score.client.reply.domain.ReplyVO;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author wenchao.li
 * @since 2019-06-10
 */
@Data
public class CommentVO extends BaseDO {

    private Long commentId;

    private Date gmtCreate;

    private String content;

    private Long likeNum;

    // 默认没点赞
    private boolean like;

    private String userPortrait;

    private String netType;

    private String userName;

    private String device;

    private List<ReplyVO> reply;

    public CommentVO(CommentDO commentDO) {
        commentId = commentDO.getId();
        gmtCreate = commentDO.getGmtCreate();
        content = commentDO.getContent();
        userPortrait = commentDO.getAvatarUrl();
        netType = commentDO.getNetType();
        userName = commentDO.getName();
        device = commentDO.getName();
        if (!StringUtils.isEmpty(commentDO.getHistoryReply())) {
            reply = GsonUtil.fromJsonToList(commentDO.getHistoryReply(), ReplyVO[].class);
        }
    }
}
