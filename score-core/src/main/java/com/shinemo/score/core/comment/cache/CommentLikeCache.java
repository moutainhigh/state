package com.shinemo.score.core.comment.cache;

import com.shinemo.client.common.ListVO;
import com.shinemo.my.redis.service.RedisService;
import com.shinemo.score.client.comment.domain.LikeTypeEnum;
import com.shinemo.score.client.like.domain.LikeDO;
import com.shinemo.score.client.like.query.LikeQuery;
import com.shinemo.score.core.like.service.LikeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 缓存一条评论下点赞的用户
 * redis set类型存储
 *
 * @author wenchao.li
 * @since 2019-06-11
 */
@Component
public class CommentLikeCache {

    @Resource
    private RedisService redisService;
    @Resource
    private LikeService likeService;

    // redis键 评论id为参数，值为用户id
    private final static String COMMENT_LIKE_KEY = "miku_comment_like_users_%s";
    // 1小时的失效时间
    private final static Long COMMENT_LIKE_KEY_EXPIRE = 60 * 60 * 1000L;

    public void putOne(Long commentId, Long uid) {
        String key = keyFormat(commentId);
        redisService.sAdd(key, uid.toString());
        redisService.pexpire(key, COMMENT_LIKE_KEY_EXPIRE);
    }

    public void putAll(Long commentId, List<String> uids) {
        String key = keyFormat(commentId);
        redisService.sAdd(key, uids.toArray(new String[0]));
        redisService.pexpire(key, COMMENT_LIKE_KEY_EXPIRE);
    }

    public boolean isLike(Long commentId, Long uid) {
        Set<String> uids = get(commentId);
        if (uids == null || uids.isEmpty()) {
            return false;
        }
        return uids.contains(uid.toString());
    }

    public Set<String> get(Long commentId) {
        String key = keyFormat(commentId);
        boolean exist = redisService.exist(key);
        if (exist) {
            return redisService.smembers(key);
        } else {
            LikeQuery query = new LikeQuery();
            query.setCommentId(commentId);
            query.setType(LikeTypeEnum.ADD.getId());

            List<String> uids = new ArrayList<>();

            ListVO<LikeDO> listRs = likeService.findByQuery(query);
            listRs.getRows().forEach(v -> uids.add(v.getUid().toString()));

            if (uids.isEmpty()) {
                // 没有的话，value给个-1,避免没有赞的评论每次都要走db
                uids.add("-1");
            }
            putAll(commentId, uids);
            return new HashSet<>(uids);
        }
    }

    public void remove(Long commentId, Long uid) {
        redisService.srem(keyFormat(commentId), uid.toString());
    }


    private String keyFormat(Long commentId) {
        return String.format(COMMENT_LIKE_KEY, String.valueOf(commentId));
    }
}
