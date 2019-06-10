package com.shinemo.score.core.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.core.user.service.CommentService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author wenchao.li
 * @since 2019-06-10
 */
@Component
public class CommentCache {

    @Resource
    private CommentService commentService;

    private final LoadingCache<Long, CommentDO> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(6, TimeUnit.HOURS).initialCapacity(100).build(new CacheLoader<Long, CommentDO>() {
                public CommentDO load(Long key) {
                    return commentService.getByIdFromDB(key);
                }
            });


    public CommentDO get(Long key) throws ExecutionException {

        // 没get到，会走guava loader
        return cache.get(key);
    }

    public void put(CommentDO commentDO) {

        remove(commentDO.getId());

        cache.put(commentDO.getId(), commentDO);
    }

    // 清除key
    private void remove(Long key) {
        cache.invalidate(key);
    }
}
