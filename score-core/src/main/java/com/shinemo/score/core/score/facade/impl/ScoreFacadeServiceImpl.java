package com.shinemo.score.core.score.facade.impl;

import com.shinemo.client.common.Result;
import com.shinemo.client.common.WebResult;
import com.shinemo.client.exception.BizException;
import com.shinemo.score.client.comment.facade.CommentFacadeService;
import com.shinemo.score.client.comment.query.CommentParam;
import com.shinemo.score.client.score.domain.ScoreRequest;
import com.shinemo.score.client.score.facade.ScoreFacadeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wenchao.li
 * @since 2019-06-12
 */
@Service("scoreFacadeService")
public class ScoreFacadeServiceImpl implements ScoreFacadeService {

    @Resource
    private CommentFacadeService commentFacadeService;

    @Override
    public WebResult<Void> submitScore(ScoreRequest request) {




        CommentParam param = initCommentParam(request);
        Result<Void> commentRs = commentFacadeService.createCommentOrReply(param);
        if (!commentRs.isSuccess()) {
            throw new BizException(commentRs.getError());
        }
        return WebResult.success();
    }

    private CommentParam initCommentParam(ScoreRequest request) {
        CommentParam param = new CommentParam();
        param.setComment(request.getComment());
        param.setCommentId(request.getCommentId());
        param.setNetType(request.getNetType());
        param.setVideoId(request.getVideoId());
        param.setVideoType(request.getFlag());
        param.setExtend(request.getExtend());
        return param;
    }
}
