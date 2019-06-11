package com.shinemo.score.core.async.listener;

import com.google.common.eventbus.Subscribe;
import com.shinemo.client.async.BaseAsync;
import com.shinemo.score.core.async.event.AfterReplyEvent;
import com.shinemo.score.core.reply.service.ReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 回复后事件处理
 *
 * @author wenchao.li
 * @since 2019-06-11
 */
@Slf4j
@Service
public class AfterReplyListener extends BaseAsync {

    @Resource
    private ReplyService replyService;


    @Subscribe
    public void handler(AfterReplyEvent event) {

        // 更新评论
        Long commentId = event.getCommentId();



    }
}
