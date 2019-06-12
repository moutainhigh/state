package com.shinemo.score.core.like.service;

import com.shinemo.client.common.ListVO;
import com.shinemo.score.client.like.domain.LikeDO;
import com.shinemo.score.client.like.query.LikeQuery;
import com.shinemo.score.client.like.query.LikeRequest;

/**
 * @author wenchao.li
 * @since 2019-06-11
 */
public interface LikeService {

    /**
     * 创建或更新点赞记录
     * 会更新评论的缓存
     */
    LikeDO createOrSave(LikeRequest request);

    LikeDO getByCommentIdAndUid(Long commentId, Long uid);

    LikeDO getByQuery(LikeQuery likeQuery);

    /**
     * @return 根据评论id查找赞记录
     */
    ListVO<LikeDO> findByQuery(LikeQuery query);


    /**
     * 判断用户是否赞过某个评论
     * 只走缓存
     */
    boolean isLike(Long commentId, Long uid);
}
