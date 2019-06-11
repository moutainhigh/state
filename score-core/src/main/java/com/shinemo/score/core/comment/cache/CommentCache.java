package com.shinemo.score.core.comment.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.shinemo.my.redis.service.RedisService;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.core.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

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

    private final LoadingCache<Long, CommentDO> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.SECONDS).initialCapacity(100)
            .build(
                    new CacheLoader<Long, CommentDO>() {
                        public CommentDO load(Long key) {
                            // 先走redis
                            CommentDO comment = redisService.get(keyFormat(key), CommentDO.class);
                            if (comment != null) {
                                return comment;
                            }
                            // 最终走db
                            return commentService.getByIdFromDB(key);
                        }
                    });


    public CommentDO get(Long key) {
        try {
            // 没get到，会走guava loader
            return cache.get(key);
        } catch (Exception e) {
            log.error("[commentCache] get cache has ex", e);
            return null;
        }
    }

    public void put(CommentDO commentDO) {

        redisService.set(keyFormat(commentDO.getId()), CommentDO.class, COMMENT_KEY_EXPIRE);
        cache.put(commentDO.getId(), commentDO);
    }

    // 清除key
    private void remove(Long key) {

        redisService.del(keyFormat(key));
        cache.invalidate(key);
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
