package com.shinemo.score.client.reply.facade;

import com.shinemo.client.common.WebResult;
import com.shinemo.score.client.reply.domain.ReplyParam;


/**
 * @author wenchao.li
 * @since 2019-06-12
 */
public interface ReplyFacadeService {

    WebResult<Void> submit(ReplyParam replyParam);
}
