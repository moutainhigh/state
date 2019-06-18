package com.shinemo.score.client.comment.domain;

import com.shinemo.client.common.BaseDO;
import com.shinemo.client.common.ListVO;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.score.client.reply.domain.ReplyDO;
import com.shinemo.score.client.reply.domain.ReplyVO;
import com.shinemo.score.client.utils.RegularUtils;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wenchao.li
 * @since 2019-06-10
 */
@Data
public class CommentVO extends BaseDO {

    private Long commentId;

    private Long gmtCreate;

    private String content;

    private Long likeNum;

    private Integer replyNum;

    // 默认没点赞
    private boolean like;

    private String userPortrait;

    private String netType;

    private String userName;

    private String device;

    private String mobile;

    // 回复
    // 列表页给 List<ReplyVO>
    // 详情页给 ListVO<ReplyVO>，分页展示
    private Object reply;


    public CommentVO(CommentDO commentDO) {
        this(commentDO, null);
    }

    /**
     * @param listVO 不为null则为回复详情，分页展示
     */
    public CommentVO(CommentDO commentDO, ListVO<ReplyDO> listVO) {

        commentId = commentDO.getId();
        gmtCreate = commentDO.getGmtCreate().getTime();
        content = commentDO.getContent();
        userPortrait = commentDO.getAvatarUrl();
        netType = commentDO.getNetType();
        mobile = RegularUtils.ignorePhone(commentDO.getMobile());
        userName = commentDO.getName() + RegularUtils.ignorePhone(commentDO.getMobile());
        device = commentDO.getDevice();
        likeNum = commentDO.getLikeNum();
        replyNum = commentDO.getReplyNum();

        if (listVO != null) {

            List<ReplyDO> rows = listVO.getRows();
            List<ReplyVO> result = new ArrayList<>();

            rows.forEach(v -> result.add(new ReplyVO(v)));
            reply = ListVO.list(result, listVO.getTotalCount(), listVO.getCurrentPage(), listVO.getPageSize());

        } else {
            List<ReplyVO> result = new ArrayList<>();
            if (!StringUtils.isEmpty(commentDO.getHistoryReply())) {
                List<ReplyDO> replys = GsonUtil.fromJsonToList(commentDO.getHistoryReply(), ReplyDO[].class);
                replys.forEach(v -> {
                    ReplyVO replyVO = new ReplyVO(v);
                    result.add(replyVO);
                });
            }
            reply = result;
        }
    }
}
