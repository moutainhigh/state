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

    LikeDO create(LikeRequest request);

    LikeDO update(LikeRequest request);

    LikeDO getByCommentIdAndUid(Long commentId, Long uid);

    LikeDO getByQuery(LikeQuery likeQuery);

    /**
     * @return 根据评论id查找赞记录
     */
    ListVO<LikeDO> findByQuery(LikeQuery query);
}
