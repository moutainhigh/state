package com.shinemo.score.core.user.service.impl;

import com.shinemo.client.common.ListVO;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.client.comment.query.CommentQuery;
import com.shinemo.score.client.comment.query.CommentRequest;
import com.shinemo.score.core.user.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author wenchao.li
 * @since 2019-06-06
 */
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    @Override
    public CommentDO create(CommentRequest request) {

        Assert.hasText(request.getContent(),"content not be empty");
        Assert.notNull(request.getVideoId(),"videoId not be empty");
        Assert.notNull(request.getVideoType(),"videoType not be empty");


        return null;
    }

    @Override
    public CommentDO getById(Long commentId) {
        return null;
    }

    @Override
    public CommentDO getByQuery(CommentQuery query) {
        return null;
    }

    @Override
    public ListVO<CommentDO> findByQuery(CommentQuery query) {
        return null;
    }
}
