package com.shinemo.score.core.user.service.impl;

import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.client.exception.BizException;
import com.shinemo.score.client.error.ScoreErrors;
import com.shinemo.score.client.reply.domain.ReplyDO;
import com.shinemo.score.client.reply.query.ReplyQuery;
import com.shinemo.score.client.reply.query.ReplyRequest;
import com.shinemo.score.core.user.service.ReplyService;
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

    @Resource
    private ReplyWrapper replyWrapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReplyDO create(ReplyRequest request) {

        Assert.hasText(request.getContent(), "内容不能为空");
        Assert.isTrue(request.getContent().length() <= 50, "内容长度不能超过50字");
        Assert.hasText(request.getMobile(), "mobile not be empty");
        Assert.notNull(request.getUid(), "uid not be empty");
        Assert.notNull(request.getCommentId(), "commentId not be empty");
        Assert.hasText(request.getName(), "name not be empty");

        ReplyDO replyDO = new ReplyDO();
        BeanUtils.copyProperties(request, replyDO);

        Result<ReplyDO> insertRs = replyWrapper.insert(replyDO);
        if (!insertRs.hasValue()) {
            log.error("[create_reply] has err:replyDO:{},rs:{}", replyDO, insertRs);
            throw new BizException(insertRs.getError());
        }

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
        return null;
    }
}
