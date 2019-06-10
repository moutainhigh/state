package com.shinemo.score.core.user.service.impl;

import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.client.exception.BizException;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.client.comment.query.CommentQuery;
import com.shinemo.score.client.comment.query.CommentRequest;
import com.shinemo.score.client.error.ScoreErrors;
import com.shinemo.score.core.user.service.CommentService;
import com.shinemo.score.dal.comment.wrapper.CommentWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wenchao.li
 * @since 2019-06-06
 */
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentWrapper commentWrapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentDO create(CommentRequest request) {

        Assert.hasText(request.getContent(), "内容不能为空");
        Assert.isTrue(request.getContent().length() <= 50, "内容长度不能超过50字");
        Assert.hasText(request.getVideoId(), "videoId not be empty");
        Assert.hasText(request.getMobile(), "mobile not be empty");
        Assert.notNull(request.getVideoType(), "videoType not be empty");
        Assert.notNull(request.getUid(), "uid not be empty");


        CommentDO commentDO = new CommentDO();
        BeanUtils.copyProperties(request, commentDO);

        Result<CommentDO> insertRs = commentWrapper.insert(commentDO);
        if (!insertRs.hasValue()) {
            log.error("[create_comment] has err:commentDO:{},rs:{}", commentDO, insertRs);
            throw new BizException(insertRs.getError());
        }

        //TODO:缓存

        return insertRs.getValue();
    }

    @Override
    public CommentDO getById(Long commentId) {

        //TODO:缓存中取

        Assert.notNull(commentId, "commentId not be null");
        CommentQuery query = new CommentQuery();
        query.setId(commentId);
        return getByQuery(query);
    }

    @Override
    public CommentDO getByQuery(CommentQuery query) {

        Result<CommentDO> commentRs = commentWrapper.get(query, ScoreErrors.COMMENT_NOT_EXIST);
        if (!commentRs.hasValue()) {
            throw new BizException(commentRs.getError());
        }
        return commentRs.getValue();
    }

    @Override
    public ListVO<CommentDO> findByQuery(CommentQuery query) {

        Result<ListVO<CommentDO>> listRs = commentWrapper.find(query);
        if (!listRs.hasValue()) {
            throw new BizException(listRs.getError());
        }
        return listRs.getValue();
    }

    @Override
    public List<Long> findIdsByQuery(CommentQuery query) {

        query.setPageEnable(false);
        Result<ListVO<Long>> idListRs = commentWrapper.findIds(query);
        if (!idListRs.hasValue()) {
            throw new BizException(idListRs.getError());
        }
        return idListRs.getValue().getRows();
    }
}
