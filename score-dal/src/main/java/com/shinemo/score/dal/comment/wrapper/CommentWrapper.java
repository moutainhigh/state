package com.shinemo.score.dal.comment.wrapper;

import javax.annotation.Resource;

import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.client.comment.query.CommentQuery;
import com.shinemo.score.dal.comment.mapper.CommentMapper;
import org.springframework.stereotype.Service;
import com.shinemo.client.dal.mapper.Mapper;
import com.shinemo.client.dal.wrapper.Wrapper;

/**
 * Wrapper
 * @ClassName: CommentWrapper
 * @author wenchao.li
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
}