package com.shinemo.score.core.comment.facade.impl;

import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.client.common.StatusEnum;
import com.shinemo.client.common.WebResult;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.jce.Constant;
import com.shinemo.jce.common.config.JceHolder;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.client.comment.domain.CommentFlag;
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
import com.shinemo.ygw.client.migu.UserExtend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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
    public Result<ListVO<CommentDO>> findByQuery(CommentQuery query) {
        ListVO<CommentDO> rs = commentService.findByQuery(query);
        return Result.success(rs);
    }

    @Override
    public WebResult<ListVO<CommentVO>> findListVO(CommentQuery query) {

        // 校验评论是否打开
        commentService.checkCommentOpen();

        UserExtend extend = GsonUtil.fromGson2Obj(JceHolder.get(Constant.USER_EXTEND), UserExtend.class);

        logger.info("[findListVO] query:{},token:{}", query, extend);

        Assert.hasText(query.getVideoId(), "videoId not be empty");

        if (extend != null) {
            query.setSensitiveUid(extend.getUid());
        }
        ListVO<Long> idsRs = commentService.findIdsByQuery(query);

        List<CommentVO> list = new ArrayList<>();
        for (Long commentId : idsRs.getRows()) {

            CommentDO comment = commentService.getById(commentId);

            // 敏感词转换
            commentService.transferSensitiveWord(comment);

            CommentVO commentVO;
            if (extend != null && extend.getUid() != null) {
                commentVO = new CommentVO(comment, extend.getUid());
                commentVO.setLike(likeService.isLike(commentId, extend.getUid()));
            } else {
                commentVO = new CommentVO(comment);
            }
            list.add(commentVO);
        }
        return WebResult.success(ListVO.list(list, idsRs.getTotalCount(), idsRs.getCurrentPage(), idsRs.getPageSize()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<CommentDO> submit(CommentParam param) {

        // 校验评论是否打开
        commentService.checkCommentOpen();

        // 没评论成功
        if (StringUtils.isEmpty(param.getComment())) {
            return Result.success();
        }

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
        if (param.showDeviceType()) {
            request.setDevice(extend.getDeviceModel());
        }
        request.setVideoId(param.getVideoId());
        request.setFullDevice(param.getFullDevice());
        request.setName(extend.getUserName());
        request.setUid(extend.getUid());
        request.setMobile(extend.getMobile());
        request.setIp(param.getIp());

        CommentDO commentDO = commentService.create(request);

        // 敏感词处理一下
        commentService.transferSensitiveWord(commentDO);

        return Result.success(commentDO);
    }

    @Override
    public WebResult<CommentVO> getDetail(CommentQuery query) {

        // 校验评论是否打开
        commentService.checkCommentOpen();

        UserExtend extend = GsonUtil.fromGson2Obj(JceHolder.get(Constant.USER_EXTEND), UserExtend.class);

        logger.info("[findListVO] query:{},token:{}", query, extend);

        CommentDO comment = commentService.getById(query.getCommentId());

        // 评论敏感词转换
        commentService.transferSensitiveWord(comment);

        ReplyQuery replyQuery = new ReplyQuery();
        replyQuery.setCommentId(query.getCommentId());
        replyQuery.setPageSize(query.getPageSize());
        replyQuery.setCurrentPage(query.getCurrentPage());
        replyQuery.setOrderByEnable(true);
        replyQuery.putOrderBy("id", false);
        replyQuery.setStatus(StatusEnum.NORMAL.getId());
        // 只看没有敏感词的
        replyQuery.getReplyFlag().remove(CommentFlag.HAS_SENSITIVE);
        // 自己的敏感词评论可以看
        replyQuery.setIgnoreOtherSensitive(true);
        ListVO<ReplyDO> replys = replyService.findByQuery(replyQuery);

        // 回复敏感词转换
        replys.getRows().forEach(v -> replyService.transferSensitiveWord(v));

        CommentVO vo = null;
        if (extend != null && extend.getUid() != null) {
            vo = new CommentVO(comment, extend.getUid(), replys);
            vo.setLike(likeService.isLike(query.getCommentId(), extend.getUid()));
        } else {
            new CommentVO(comment, null, replys);
        }
        return WebResult.success(vo);
    }
}
