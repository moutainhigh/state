package com.shinemo.score.core.comment.facade.impl;

import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.client.common.WebResult;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.jce.Constant;
import com.shinemo.jce.common.config.JceHolder;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.client.comment.domain.CommentVO;
import com.shinemo.score.client.comment.facade.CommentFacadeService;
import com.shinemo.score.client.comment.query.CommentParam;
import com.shinemo.score.client.comment.query.CommentQuery;
import com.shinemo.score.client.comment.query.CommentRequest;
import com.shinemo.score.core.comment.service.CommentService;
import com.shinemo.score.core.like.service.LikeService;
import com.shinemo.ygw.client.migu.UserExtend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wenchao.li
 * @since 2019-06-11
 */
@Service("commentFacadeService")
public class CommentFacadeServiceImpl implements CommentFacadeService {

    private Logger logger = LoggerFactory.getLogger("access");

    @Resource
    private CommentService commentService;
    @Resource
    private LikeService likeService;

    @Override
    public Result<CommentDO> getById(Long commentId) {

        CommentDO comment = commentService.getById(commentId);

        return Result.success(comment);
    }

    @Override
    public WebResult<ListVO<CommentVO>> findListVO(CommentQuery query) {

        UserExtend extend = GsonUtil.fromGson2Obj(JceHolder.get(Constant.USER_EXTEND), UserExtend.class);
        logger.info("[findListVO] query:{},token:{}", query, extend);

        if (extend != null) {
            query.setUid(extend.getUid());
        }

        logger.info("find CommentVO list query:{}", query);

        Assert.hasText(query.getVideoId(), "videoId not be empty");

        ListVO<Long> idsRs = commentService.findIdsByQuery(query);

        List<CommentVO> list = new ArrayList<>();
        for (Long commentId : idsRs.getRows()) {

            CommentDO comment = commentService.getById(commentId);
            CommentVO commentVO = new CommentVO(comment);
            if (query.getUid() != null) {
                commentVO.setLike(likeService.isLike(commentId, query.getUid()));
            }
            list.add(commentVO);
        }
        return WebResult.success(ListVO.list(list, idsRs.getTotalCount(), idsRs.getCurrentPage(), idsRs.getPageSize()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> submit(CommentParam param) {

        UserExtend extend = GsonUtil.fromGson2Obj(JceHolder.get(Constant.USER_EXTEND), UserExtend.class);
        logger.info("[submit] param:{},token:{}", param, extend);

        Assert.notNull(extend, "您尚未登录");
        Assert.hasText(param.getComment(), "comment not be empty");
        Assert.notNull(extend.getUid(), "uid not be empty");
        Assert.notNull(param.getVideoId(), "videoId not be empty");

        // 评论
        CommentRequest request = new CommentRequest();
        request.setNetType(param.getNetType());
        request.setVideoType(param.getVideoType());
        request.setAvatarUrl(extend.getUserPortrait());
        request.setContent(param.getComment());
        request.setDevice(extend.getDeviceModel());
        request.setVideoId(param.getVideoId());
        request.setName(extend.getUserName());
        request.setUid(extend.getUid());
        commentService.create(request);

        return Result.success();
    }
}
