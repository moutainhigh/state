package com.shinemo.score.core.comment.facade.impl;

import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.client.common.WebResult;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.client.util.UserAgentUtils;
import com.shinemo.jce.Constant;
import com.shinemo.jce.common.config.JceHolder;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.client.comment.domain.CommentVO;
import com.shinemo.score.client.comment.facade.CommentFacadeService;
import com.shinemo.score.client.comment.query.CommentParam;
import com.shinemo.score.client.comment.query.CommentQuery;
import com.shinemo.score.client.comment.query.CommentRequest;
import com.shinemo.score.client.reply.domain.ReplyDO;
import com.shinemo.score.client.reply.query.ReplyQuery;
import com.shinemo.score.core.comment.service.CommentService;
import com.shinemo.score.core.like.service.LikeService;
import com.shinemo.score.core.reply.service.ReplyService;
import com.shinemo.ygw.client.HeaderExtend;
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
    @Resource
    private ReplyService replyService;

    @Override
    public Result<CommentDO> getById(Long commentId) {

        CommentDO comment = commentService.getById(commentId);

        return Result.success(comment);
    }

    @Override
    public ListVO<CommentDO> findByQuery(CommentQuery query) {
        return commentService.findByQuery(query);
    }

    @Override
    public WebResult<ListVO<CommentVO>> findListVO(CommentQuery query) {

        UserExtend extend = GsonUtil.fromGson2Obj(JceHolder.get(Constant.USER_EXTEND), UserExtend.class);

        logger.info("[findListVO] query:{},token:{}", query, extend);

        Assert.hasText(query.getVideoId(), "videoId not be empty");

        ListVO<Long> idsRs = commentService.findIdsByQuery(query);

        List<CommentVO> list = new ArrayList<>();
        for (Long commentId : idsRs.getRows()) {

            CommentDO comment = commentService.getById(commentId);
            CommentVO commentVO = new CommentVO(comment);
            if (extend != null && extend.getUid() != null) {
                commentVO.setLike(likeService.isLike(commentId, extend.getUid()));
            }
            list.add(commentVO);
        }
        return WebResult.success(ListVO.list(list, idsRs.getTotalCount(), idsRs.getCurrentPage(), idsRs.getPageSize()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> submit(CommentParam param) {

        UserExtend extend = GsonUtil.fromGson2Obj(JceHolder.get(Constant.USER_EXTEND), UserExtend.class);
        HeaderExtend header = GsonUtil.fromGson2Obj(JceHolder.get(Constant.HEADER_EXTEND), HeaderExtend.class);

        logger.info("[submit] param:{},token:{},header:{}", param, extend, header);

        Assert.notNull(extend, "您尚未登录");
        Assert.hasText(param.getComment(), "comment not be empty");
        Assert.notNull(extend.getUid(), "uid not be empty");
        Assert.notNull(param.getVideoId(), "videoId not be empty");

        String userAgent = header.getHeaders().get("user-agent");
        // 评论
        CommentRequest request = new CommentRequest();
        request.setNetType(param.getNetType());
        request.setVideoType(param.getVideoType());
        request.setAvatarUrl(extend.getUserPortrait());
        request.setContent(param.getComment());
        request.setDevice(UserAgentUtils.getDeviceType(userAgent));
        request.setVideoId(param.getVideoId());
        request.setName(extend.getUserName());
        request.setUid(extend.getUid());
        request.setMobile(extend.getMobile());
        commentService.create(request);

        return Result.success();
    }

    @Override
    public WebResult<CommentVO> getDetail(CommentQuery query) {

        UserExtend extend = GsonUtil.fromGson2Obj(JceHolder.get(Constant.USER_EXTEND), UserExtend.class);

        logger.info("[findListVO] query:{},token:{}", query, extend);

        CommentDO comment = commentService.getById(query.getCommentId());

        ReplyQuery replyQuery = new ReplyQuery();
        replyQuery.setCommentId(query.getCommentId());
        replyQuery.setPageSize(query.getPageSize());
        replyQuery.setCurrentPage(query.getCurrentPage());

        ListVO<ReplyDO> replys = replyService.findByQuery(replyQuery);
        CommentVO vo = new CommentVO(comment, replys);
        if (extend != null && extend.getUid() != null) {
            vo.setLike(likeService.isLike(query.getCommentId(), extend.getUid()));
        }
        return WebResult.success(vo);
    }
}
