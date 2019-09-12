package com.shinemo.score.client.comment.facade;

import com.shinemo.client.common.Result;
import com.shinemo.score.client.comment.domain.VerifyRequest;

public interface AdminCommentFacadeService{
    /**
     * 审核评论
     * @param request
     * @return
     */
    Result<Void> verifyComment(VerifyRequest request);
    /**
     * 删除评论
     * @param request
     * @return
     */
    Result<Void> deleteComment(VerifyRequest request);


}
