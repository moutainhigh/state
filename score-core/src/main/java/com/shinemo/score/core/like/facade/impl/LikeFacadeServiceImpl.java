package com.shinemo.score.core.like.facade.impl;

import com.shinemo.client.common.WebResult;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.jce.Constant;
import com.shinemo.jce.common.config.JceHolder;
import com.shinemo.score.client.like.facade.LikeFacadeService;
import com.shinemo.score.client.like.query.LikeParam;
import com.shinemo.score.client.like.query.LikeRequest;
import com.shinemo.score.core.like.service.LikeService;
import com.shinemo.ygw.client.migu.UserExtend;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @author wenchao.li
 * @since 2019-06-12
 */
@Service("likeFacadeService")
public class LikeFacadeServiceImpl implements LikeFacadeService {

    @Resource
    private LikeService likeService;

    @Override
    public WebResult<Void> operateLike(LikeParam param) {

        UserExtend extend = GsonUtil.fromGson2Obj(JceHolder.get(Constant.USER_EXTEND), UserExtend.class);

        Assert.notNull(extend, "您尚未登录");
        Assert.notNull(param.getOpType(), "opType not be empty");
        Assert.notNull(param.getCommentId(), "commentId not be empty");

        LikeRequest request = new LikeRequest();
        request.setCommentId(param.getCommentId());
        request.setLikeAction(param.getOpType());
        request.setUid(extend.getUid());

        likeService.createOrSave(request);

        return WebResult.success();
    }
}
