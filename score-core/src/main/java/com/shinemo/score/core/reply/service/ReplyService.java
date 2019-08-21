package com.shinemo.score.core.reply.service;

import com.shinemo.client.common.ListVO;
import com.shinemo.score.client.reply.domain.ReplyDO;
import com.shinemo.score.client.reply.query.ReplyQuery;
import com.shinemo.score.client.reply.query.ReplyRequest;

/**
 * @author wenchao.li
 * @since 2019-06-10
 */
public interface ReplyService {

    /**
     * @return 创建回复
     */
    ReplyDO create(ReplyRequest replyRequest);

    ReplyDO getById(Long replyId);

    /**
     * @return 查询回复列表
     */
    ListVO<ReplyDO> findByQuery(ReplyQuery query);

    void delete(ReplyRequest replyRequest);
}
