package com.shinemo.score.core.comment.cache;

import com.shinemo.my.redis.service.RedisService;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.core.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wenchao.li
 * @since 2019-06-10
 */
@Component
@Slf4j
public class CommentCache {

    @Resource
    private CommentService commentService;

    @Resource
    private RedisService redisService;

    // comment redis key
    private final static String COMMENT_KEY = "migu_comment_%s";
    // comment redis key expired time 1天
    private final static Integer COMMENT_KEY_EXPIRE = 60 * 60 * 24;


    public CommentDO get(Long key) {
        try {
            // 先走redis
            CommentDO comment = redisService.get(keyFormat(key), CommentDO.class);
            if (comment != null) {
                return comment;
            }
            // 最终走db
            comment = commentService.getByIdFromDB(key);
            // 加入缓存
            put(comment);
            return comment;
        } catch (Exception e) {
            log.error("[commentCache] get cache has ex", e);
            return null;
        }
    }

    public void put(CommentDO commentDO) {
        redisService.set(keyFormat(commentDO.getId()), commentDO, COMMENT_KEY_EXPIRE);
    }

    // 清除key
    public void remove(Long key) {
        redisService.del(keyFormat(key));
    }

    // 刷新缓存
    public void refresh(Long key) {

        // 先删除
        remove(key);

        put(commentService.getByIdFromDB(key));
    }


    private String keyFormat(Long key) {
        return String.format(COMMENT_KEY, key);
    }
}
