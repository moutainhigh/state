package com.shinemo.score.dal.reply.wrapper;

import javax.annotation.Resource;

import com.shinemo.client.common.Errors;
import com.shinemo.client.common.Result;
import com.shinemo.client.exception.BizException;
import com.shinemo.score.client.reply.domain.ReplyDO;
import com.shinemo.score.client.reply.query.ReplyQuery;
import com.shinemo.score.dal.reply.mapper.ReplyMapper;
import org.springframework.stereotype.Service;
import com.shinemo.client.dal.mapper.Mapper;
import com.shinemo.client.dal.wrapper.Wrapper;


/**
 * Wrapper
 *
 * @author wenchao.li
 * @ClassName: ReplyWrapper
 * @Date 2019-06-06 10:06:20
 */
@Service
public class ReplyWrapper extends Wrapper<ReplyQuery, ReplyDO> {

    @Resource
    private ReplyMapper replyMapper;

    @Override
    public Mapper<ReplyQuery, ReplyDO> getMapper() {
        return replyMapper;
    }

    public Result<Void> deleteByComment(Long commentId) {
        try {
            replyMapper.deleteByCommentId(commentId);
        } catch (Exception e) {
            throw new BizException(Errors.SQL_ERROR_UPDATE, e);
        }
        return Result.success();
    }
}