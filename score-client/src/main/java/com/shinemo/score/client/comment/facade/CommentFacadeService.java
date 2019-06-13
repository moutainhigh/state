package com.shinemo.score.client.comment.facade;

import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.client.common.WebResult;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.client.comment.domain.CommentVO;
import com.shinemo.score.client.comment.query.CommentParam;
import com.shinemo.score.client.comment.query.CommentQuery;

/**
 * @author wenchao.li
 * @since 2019-06-11
 */
public interface CommentFacadeService {


    Result<CommentDO> getById(Long commentId);

    /**
     * @return 查询评论列表
     */
    ListVO<CommentDO> findByQuery(CommentQuery query);

    /**
     * @return 评论列表, 客户端用
     */
    WebResult<ListVO<CommentVO>> findListVO(CommentQuery query);

    Result<Void> submit(CommentParam param);

    WebResult<CommentVO> getDetail(CommentQuery query);
}
