package com.shinemo.score.client.like.facade;

import com.shinemo.client.common.WebResult;
import com.shinemo.score.client.like.query.LikeParam;

/**
 * @author wenchao.li
 * @since 2019-06-12
 */
public interface LikeFacadeService {


    /**
     * @return 点赞或取消赞
     */
    WebResult<Void> operateLike(LikeParam param);

}
