package com.shinemo.score.dal.reply.mapper;

import com.shinemo.client.dal.mapper.Mapper;
import com.shinemo.score.client.reply.domain.ReplyDO;
import com.shinemo.score.client.reply.query.ReplyQuery;

/**
 * Mapper
 * @ClassName: ReplyMapper
 * @author wenchao.li
 * @Date 2019-06-06 10:06:20
 */
public interface ReplyMapper extends Mapper<ReplyQuery, ReplyDO> {

    /**
     * 通过commentID删除回复
     */
    void deleteByCommentId(Long commentId);
}