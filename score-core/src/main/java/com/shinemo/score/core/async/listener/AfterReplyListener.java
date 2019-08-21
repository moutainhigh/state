package com.shinemo.score.core.async.listener;

import com.google.common.eventbus.Subscribe;
import com.shinemo.client.async.BaseAsync;
import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.StatusEnum;
import com.shinemo.score.client.comment.domain.CommentFlag;
import com.shinemo.score.client.comment.query.CommentRequest;
import com.shinemo.score.client.reply.domain.ReplyDO;
import com.shinemo.score.client.reply.query.ReplyQuery;
import com.shinemo.score.core.async.event.AfterReplyEvent;
import com.shinemo.score.core.comment.service.CommentService;
import com.shinemo.score.core.reply.service.ReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 回复后事件处理
 *
 * @author wenchao.li
 * @since 2019-06-11
 */
@Slf4j
@Service
public class AfterReplyListener extends BaseAsync {

    @Resource
    private ReplyService replyService;
    @Resource
    private CommentService commentService;


    @Subscribe
    public void handler(AfterReplyEvent event) {

        Long commentId = event.getCommentId();

        ReplyQuery query = new ReplyQuery();
        query.setCommentId(commentId);
        query.setPageSize(3);
        query.setCurrentPage(1);
        query.putOrderBy("id", false);
        query.setOrderByEnable(true);
        query.setStatus(StatusEnum.NORMAL.getId());
        ListVO<ReplyDO> replyDOListVO = replyService.findByQuery(query);

        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setCommentId(commentId);
        commentRequest.setHistoryReply(replyDOListVO.getRows());

        // 如果是删除，并且不是含有敏感词的，有效回复数减1
        if (!event.isDel()) {
            commentRequest.setIncrReplyNum(true);
        } else {
            commentRequest.setSubReplyNum(true);
        }

        // 更新评论最近回复
        commentService.update(commentRequest);
    }
}
