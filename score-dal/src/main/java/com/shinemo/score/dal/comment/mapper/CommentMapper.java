package com.shinemo.score.dal.comment.mapper;

import com.shinemo.client.common.ListVO;
import com.shinemo.client.dal.mapper.Mapper;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.client.comment.query.CommentQuery;

import java.util.List;

/**
 * Mapper
 *
 * @author wenchao.li
 * @ClassName: CommentMapper
 * @Date 2019-06-06 10:06:20
 */
public interface CommentMapper extends Mapper<CommentQuery, CommentDO> {

    List<Long> findIds(CommentQuery query);
}