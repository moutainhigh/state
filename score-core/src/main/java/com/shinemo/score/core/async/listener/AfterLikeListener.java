package com.shinemo.score.core.async.listener;

import com.google.common.eventbus.Subscribe;
import com.shinemo.client.async.BaseAsync;
import com.shinemo.score.client.comment.domain.LikeTypeEnum;
import com.shinemo.score.client.comment.query.CommentRequest;
import com.shinemo.score.core.async.event.AfterLikeEvent;
import com.shinemo.score.core.comment.cache.CommentLikeCache;
import com.shinemo.score.core.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wenchao.li
 * @since 2019-06-11
 */
@Service
@Slf4j
public class AfterLikeListener extends BaseAsync {

    @Resource
    private CommentService commentService;
    @Resource
    private CommentLikeCache commentLikeCache;

    @Subscribe
    public void handler(AfterLikeEvent event) {

        CommentRequest request = new CommentRequest();
        request.setCommentId(event.getCommentId());
        request.setLikeAction(event.getLikeAction());
        // 评论更新
        commentService.update(request);

        // 该评论下点赞的用户加入缓存
        if (event.getLikeAction().equals(LikeTypeEnum.ADD.getId())) {
            commentLikeCache.putOne(event.getCommentId(), event.getUid());
        } else {
            commentLikeCache.remove(event.getCommentId(), event.getUid());
        }

    }

}
