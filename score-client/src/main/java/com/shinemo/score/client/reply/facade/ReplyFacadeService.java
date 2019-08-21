package com.shinemo.score.client.reply.facade;

import com.shinemo.client.common.WebResult;
import com.shinemo.score.client.comment.query.CommentRequest;
import com.shinemo.score.client.reply.domain.ReplyDTO;
import com.shinemo.score.client.reply.domain.ReplyParam;
import com.shinemo.score.client.reply.domain.ReplyVO;
import com.shinemo.score.client.reply.query.ReplyRequest;


/**
 * @author wenchao.li
 * @since 2019-06-12
 */
public interface ReplyFacadeService {

    WebResult<ReplyDTO> submit(ReplyParam replyParam);
}
