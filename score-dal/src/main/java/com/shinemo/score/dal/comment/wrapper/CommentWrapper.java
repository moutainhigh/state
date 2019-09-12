package com.shinemo.score.dal.comment.wrapper;

import javax.annotation.Resource;

import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.client.exception.BizException;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.client.comment.domain.VerifyRequest;
import com.shinemo.score.client.comment.query.CommentQuery;
import com.shinemo.score.dal.comment.mapper.CommentMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import com.shinemo.client.dal.mapper.Mapper;
import com.shinemo.client.dal.wrapper.Wrapper;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

import static com.shinemo.client.common.Errors.SQL_ERROR_FIND;

/**
 * Wrapper
 *
 * @author wenchao.li
 * @ClassName: CommentWrapper
 * @Date 2019-06-06 10:06:20
 */
@Service
public class CommentWrapper extends Wrapper<CommentQuery, CommentDO> {

    @Resource
    private CommentMapper commentMapper;

    @Override
    public Mapper<CommentQuery, CommentDO> getMapper() {
        return commentMapper;
    }

    public Result<ListVO<Long>> findIds(CommentQuery query) {
        try {
            if (query.isPageEnable()) {
                long count = commentMapper.count(query);
                if (count < 1) {
                    return Result.success(ListVO.list(Collections.emptyList(), 0L, query.getCurrentPage(), query.getPageSize()));
                }
                if (query.getStartRow() >= count) {
                    return Result.success(ListVO.list(Collections.emptyList(), count, query.getCurrentPage(), query.getPageSize()));
                }
                query.putTotalItem(count);
                return Result.success(ListVO.list(commentMapper.findIds(query), query.getTotalItem(), query.getCurrentPage(), query.getPageSize()));
            }
            List<Long> list = commentMapper.findIds(query);
            return Result.success(ListVO.list(list, (long) list.size(), 1L, (long) list.size()));
        } catch (Throwable e) {
            throw new BizException(SQL_ERROR_FIND, e);
        }
    }

    public Result<Void> verfiyComment(VerifyRequest request) {
        Assert.notNull(request,"request is null");
        Assert.isTrue(!CollectionUtils.isEmpty(request.getCommentIds()),"id is null");
        Assert.notNull(request.getVerifyStatus(),"verifystatus is null");
        try {
            commentMapper.verfiyComment(request);
        } catch (Throwable e) {
            throw new BizException(SQL_ERROR_FIND, e);
        }
        return Result.success();
    }
}