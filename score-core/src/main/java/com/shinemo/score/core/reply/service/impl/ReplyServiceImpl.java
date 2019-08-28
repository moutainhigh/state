package com.shinemo.score.core.reply.service.impl;

import com.shinemo.client.async.InternalEventBus;
import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.client.common.StatusEnum;
import com.shinemo.client.exception.BizException;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.client.comment.domain.CommentFlag;
import com.shinemo.score.client.comment.query.CommentRequest;
import com.shinemo.score.client.error.ScoreErrors;
import com.shinemo.score.client.reply.domain.ReplyDO;
import com.shinemo.score.client.reply.domain.ReplyExtend;
import com.shinemo.score.client.reply.query.ReplyQuery;
import com.shinemo.score.client.reply.query.ReplyRequest;
import com.shinemo.score.core.async.event.AfterReplyEvent;
import com.shinemo.score.core.reply.service.ReplyService;
import com.shinemo.score.core.word.SensitiveWordFilter;
import com.shinemo.score.dal.reply.wrapper.ReplyWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @author wenchao.li
 * @since 2019-06-10
 */
@Service
@Slf4j
public class ReplyServiceImpl implements ReplyService {

    public static final String SENSITIVE_REPLACE = "*";

    @Resource
    private ReplyWrapper replyWrapper;
    @Resource
    private InternalEventBus internalEventBus;

    @Override
    public ReplyDO create(ReplyRequest request) {

        Assert.hasText(request.getContent(), "内容不能为空");
        Assert.isTrue(request.getContent().length() <= 200, "内容长度不能超过200字");
        Assert.hasText(request.getMobile(), "mobile not be empty");
        Assert.notNull(request.getUid(), "uid not be empty");
        Assert.notNull(request.getCommentId(), "commentId not be empty");

        // 是否含有敏感词，
        // 这边只打个标,给前端返回含敏感词的评论内容时处理成*
        boolean containSensitiveWord = SensitiveWordFilter.isContaintSensitiveWord(request.getContent(),
                SensitiveWordFilter.minMatchType);
        if (containSensitiveWord) {
            log.error("[create_reply] has sensitiveWord ,txt:{}", request.getContent());
            request.getReplyFlag().add(CommentFlag.HAS_SENSITIVE);
            // 扩展字段存一下处理后的敏感词
            ReplyExtend extend = new ReplyExtend();
            extend.setSensitiveContent(SensitiveWordFilter.
                    replaceSensitiveWord(request.getContent(), SensitiveWordFilter.maxMatchType, SENSITIVE_REPLACE));
            request.setExtend(GsonUtil.toJson(extend));
        }

        ReplyDO replyDO = new ReplyDO();
        BeanUtils.copyProperties(request, replyDO);

        // 标位
        replyDO.setFlag(replyDO.getReplyFlag().getValue());
        Result<ReplyDO> insertRs = replyWrapper.insert(replyDO);
        if (!insertRs.hasValue()) {
            log.error("[create_reply] has err:replyDO:{},rs:{}", replyDO, insertRs);
            throw new BizException(insertRs.getError());
        }

        // 异步更新评论
        internalEventBus.post(
                AfterReplyEvent
                        .builder()
                        .commentId(request.getCommentId())
                        .build());
        return insertRs.getValue();
    }

    @Override
    public ReplyDO getById(Long replyId) {

        ReplyQuery query = new ReplyQuery();
        query.setId(replyId);

        Result<ReplyDO> replyRs = replyWrapper.get(query, ScoreErrors.REPLY_NOT_EXIST);
        if (!replyRs.hasValue()) {
            throw new BizException(replyRs.getError());
        }
        return replyRs.getValue();
    }

    @Override
    public ListVO<ReplyDO> findByQuery(ReplyQuery query) {

        Result<ListVO<ReplyDO>> listRs = replyWrapper.find(query);
        if (!listRs.hasValue()) {
            throw new BizException(listRs.getError());
        }
        return listRs.getValue();
    }

    @Override
    public void delete(ReplyRequest delReq) {

        Assert.notNull(delReq.getUid(), "uid not be null");
        Assert.notNull(delReq.getReplyId(), "replyId not be null");

        ReplyDO replyDO = getById(delReq.getReplyId());

        // 判断是否是自己
        Assert.isTrue(replyDO.getUid().equals(delReq.getUid()), "只能删除自己的评论");

        // 更新为已删除状态
        ReplyDO upDO = new ReplyDO();
        upDO.setId(replyDO.getId());
        upDO.setCommentId(delReq.getCommentId());
        upDO.setStatus(StatusEnum.DELETE.getId());
        replyWrapper.update(upDO);

        // 异步更新评论
        internalEventBus.post(
                AfterReplyEvent
                        .builder()
                        .commentId(replyDO.getCommentId())
                        .del(true)
                        .build());
    }

    @Override
    public void deleteByComment(Long commentId) {
        replyWrapper.deleteByComment(commentId);
    }
}
