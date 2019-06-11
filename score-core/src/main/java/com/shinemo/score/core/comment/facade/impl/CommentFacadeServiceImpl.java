package com.shinemo.score.core.comment.facade.impl;

import com.shinemo.client.common.Result;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.client.comment.facade.CommentFacadeService;
import com.shinemo.score.core.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wenchao.li
 * @since 2019-06-11
 */
@Slf4j
@Service("commentFacadeService")
public class CommentFacadeServiceImpl implements CommentFacadeService {

    @Resource
    private CommentService commentService;

    @Override
    public Result<CommentDO> getById(Long commentId) {

        CommentDO comment = commentService.getById(commentId);

        return Result.success(comment);
    }
}
