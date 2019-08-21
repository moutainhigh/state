package com.shinemo.score.core.async.listener;

import com.google.common.eventbus.Subscribe;
import com.shinemo.client.async.BaseAsync;
import com.shinemo.score.core.async.event.AfterDeleteCommentEvent;
import com.shinemo.score.core.reply.service.ReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wenchao.li
 * @since 2019-08-21
 */
@Service
@Slf4j
public class AfterDeleteCommentListener extends BaseAsync {

    @Resource
    private ReplyService replyService;

    @Subscribe
    public void handler(AfterDeleteCommentEvent event) {
        replyService.deleteByComment(event.getCommentId());
    }

}
