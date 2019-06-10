package com.shinemo.score.core.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.shinemo.power.redis.service.RedisService;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.core.user.service.CommentService;
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
            .expireAfterWrite(6, TimeUnit.HOURS).initialCapacity(100).build(new CacheLoader<Long, CommentDO>() {
                public CommentDO load(Long key) {
                    return commentService.getByIdFromDB(key);
                }
            });


    public CommentDO get(Long key) {
        try {
            // 没get到，会走guava loader
            CommentDO comment = cache.get(key);
            if (comment == null) {
                // 走redis
                comment = redisService.get(keyFormat(key), CommentDO.class);
                if (comment != null) {
                    cache.put(comment.getId(), comment);
                }
            }
            return comment;
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
