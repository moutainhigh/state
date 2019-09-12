package com.shinemo.score.core.comment.facade.impl;

import com.shinemo.client.async.InternalEventBus;
import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.client.comment.domain.VerifyRequest;
import com.shinemo.score.client.comment.domain.VerifyStatusEnum;
import com.shinemo.score.client.comment.facade.AdminCommentFacadeService;
import com.shinemo.score.client.comment.query.CommentQuery;
import com.shinemo.score.client.error.ScoreErrors;
import com.shinemo.score.dal.comment.wrapper.CommentWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;


@Service
@Slf4j
public class AdminCommentFacadeServiceImpl implements AdminCommentFacadeService {


    @Resource
    private CommentWrapper commentWrapper;

    @Resource
    private InternalEventBus internalEventBus;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> verifyComment(VerifyRequest request){
        //TODO 获取操作人信息
        Assert.notNull(request,"request is null");
        Assert.isTrue(!CollectionUtils.isEmpty(request.getCommentIds()),"id is null");
        Assert.notNull(request.getVerifyStatus(),"verifystatus is null");
        Assert.isTrue(VerifyStatusEnum.pass.getId()==request.getVerifyStatus()||
                VerifyStatusEnum.refuse.getId()==request.getVerifyStatus(),"status is error");
        CommentQuery query = new CommentQuery();
        query.setCommentIds(request.getCommentIds());
        Result<ListVO<CommentDO>> result = commentWrapper.find(query);
        if(!result.hasValue()||!result.getValue().getTotalCount().equals(request.getCommentIds().size())){
            log.error("[verifyComment] result:{} requestSize:{}",result,request.getCommentIds().size());
            return Result.error(ScoreErrors.PARAM_ERROR);
        }
        return commentWrapper.verfiyComment(request);
    }

    @Override
    public Result<Void> deleteComment(VerifyRequest request) {
        return null;
    }
}
