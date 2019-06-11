package com.shinemo.score.client.comment.facade;

import com.shinemo.client.common.Result;
import com.shinemo.score.client.comment.domain.CommentDO;

/**
 * @author wenchao.li
 * @since 2019-06-11
 */
public interface CommentFacadeService {


    Result<CommentDO> getById(Long commentId);
}
