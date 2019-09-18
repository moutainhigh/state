package com.shinemo.score.core.comment.facade.impl;

import com.shinemo.client.async.InternalEventBus;
import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.client.exception.BizException;
import com.shinemo.management.client.config.domain.SystemConfigEnum;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.client.comment.domain.VerifyRequest;
import com.shinemo.score.client.comment.domain.VerifyStatusEnum;
import com.shinemo.score.client.comment.facade.AdminCommentFacadeService;
import com.shinemo.score.client.comment.query.CommentQuery;
import com.shinemo.score.client.common.domain.DeleteStatusEnum;
import com.shinemo.score.client.error.ScoreErrors;
import com.shinemo.score.core.async.event.AfterDeleteCommentEvent;
import com.shinemo.score.core.comment.cache.CommentCache;
import com.shinemo.score.dal.comment.wrapper.CommentWrapper;
import com.shinemo.ygw.client.miguadmin.UserExtend;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;


@Service("adminCommentFacadeService")
@Slf4j
public class AdminCommentFacadeServiceImpl implements AdminCommentFacadeService {


    @Resource
    private CommentWrapper commentWrapper;

    @Resource
    private InternalEventBus internalEventBus;

    @Resource
    private CommentCache commentCache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> verifyComment(VerifyRequest request){

        Assert.notNull(request,"request is null");
        Assert.isTrue(!CollectionUtils.isEmpty(request.getCommentIds()),"id is null");
        Assert.notNull(request.getVerifyStatus(),"verifystatus is null");
        Assert.isTrue(VerifyStatusEnum.pass.getId()==request.getVerifyStatus()||
                VerifyStatusEnum.refuse.getId()==request.getVerifyStatus(),"status is error");
        request.setOperateUid(UserExtend.getUserId());
        request.setOperateUserName(UserExtend.getUserExtend().getName());
        CommentQuery query = new CommentQuery();
        query.setCommentIds(request.getCommentIds());
        Result<ListVO<CommentDO>> result = commentWrapper.find(query);
        if(!result.hasValue()||result.getValue().getTotalCount().intValue()!=request.getCommentIds().size()){
            log.error("[verifyComment] result:{} requestSize:{}",result,request.getCommentIds().size());
            return Result.error(ScoreErrors.PARAM_ERROR);
        }
        if(commentCache.getCommentConfig() == SystemConfigEnum.COMMENT_VERIFY_LAST.getId() &&
               VerifyStatusEnum.refuse.getId() == request.getVerifyStatus()){
            request.setStatus(DeleteStatusEnum.DELETE.getId());
        }
        return commentWrapper.verfiyComment(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deleteComment(VerifyRequest request){

        Assert.notNull(request,"request is null");
        Assert.isTrue(!CollectionUtils.isEmpty(request.getCommentIds()),"id is null");
        CommentQuery commentQuery = new CommentQuery();
        commentQuery.setCommentIds(request.getCommentIds());
        Result<ListVO<CommentDO>> result = commentWrapper.find(commentQuery);
        if(!result.hasValue()||result.getValue().getTotalCount().intValue()!=request.getCommentIds().size()){
            log.error("[deleteComment] result:{} requestSize:{}",result,request.getCommentIds().size());
            return Result.error(ScoreErrors.PARAM_ERROR);
        }
        CommentDO delete = new CommentDO();
        delete.setStatus(DeleteStatusEnum.DELETE.getId());
        delete.setOperateUid(UserExtend.getUserId());
        delete.setOperateUserName(UserExtend.getUserExtend().getName());
        for(CommentDO iter:result.getValue().getRows()){
            delete.setId(iter.getId());
            delete.setVersion(iter.getVersion());
            Result<CommentDO> upt = commentWrapper.update(delete);
            if(!upt.hasValue()){
                log.error("[deleteComment] error result:{}",upt);
                throw new BizException(ScoreErrors.SQL_ERROR_UPDATE);
            }
            commentCache.remove(delete.getId());
            internalEventBus.post(
                    AfterDeleteCommentEvent.builder()
                            .commentId(delete.getId())
                            .build());
        }
        return Result.success();
    }
}
