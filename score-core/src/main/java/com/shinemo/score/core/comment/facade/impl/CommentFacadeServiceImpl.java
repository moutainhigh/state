package com.shinemo.score.core.comment.facade.impl;

import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.client.common.StatusEnum;
import com.shinemo.client.common.WebResult;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.jce.Constant;
import com.shinemo.jce.common.config.JceHolder;
import com.shinemo.score.client.comment.domain.*;
import com.shinemo.score.client.comment.facade.CommentFacadeService;
import com.shinemo.score.client.comment.query.CommentParam;
import com.shinemo.score.client.comment.query.CommentQuery;
import com.shinemo.score.client.comment.query.CommentRequest;
import com.shinemo.score.client.reply.domain.ReplyDO;
import com.shinemo.score.client.reply.domain.ReplyVO;
import com.shinemo.score.client.reply.query.ReplyQuery;
import com.shinemo.score.client.reply.query.ReplyRequest;
import com.shinemo.score.core.comment.service.CommentService;
import com.shinemo.score.core.like.service.LikeService;
import com.shinemo.score.core.reply.service.ReplyService;
import com.shinemo.score.core.word.SensitiveWordFilter;
import com.shinemo.ygw.client.migu.UserExtend;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CommentFacadeServiceImpl implements CommentFacadeService {

    private Logger logger = LoggerFactory.getLogger("access");

    private static final int DeleteTypeCommentType = 1;
    private static final int DeleteTypeReplyType = 2;

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

            CommentVO commentVO;
            if (extend != null && extend.getUid() != null) {
                commentVO = new CommentVO(comment, extend.getUid());
                commentVO.setLike(likeService.isLike(commentId, extend.getUid()));
            } else {
                commentVO = new CommentVO(comment);
            }

            // 1.最近三条回复，如果含有敏感词的情况，把其他人的敏感词删除
            // 2.如果extend中存在大于等于三条记录，然后因为敏感词删除了任意条，则重新从库中拉最新三条数据
            // 只有登录过的才处理
            if (extend != null) {
                @SuppressWarnings("unchecked")
                List<ReplyVO> replys = (List<ReplyVO>) commentVO.getReply();
                if (!replys.isEmpty()) {
                    int originSize = replys.size();
                    // 删除
                    replys.removeIf(next -> next.isHasSensitive() && !next.getUid().equals(extend.getUid()));

                    if (originSize >= 3 && originSize != replys.size()) {
                        // 重新构造最近三条记录,可以看自己含敏感词的回复
                        ReplyQuery replyQuery = new ReplyQuery();
                        replyQuery.setCommentId(commentId);
                        replyQuery.setPageSize(3);
                        replyQuery.setCurrentPage(1);
                        replyQuery.putOrderBy("id", false);
                        replyQuery.setOrderByEnable(true);
                        replyQuery.setSensitiveUid(extend.getUid());
                        replyQuery.setStatus(StatusEnum.NORMAL.getId());
                        replyQuery.setContainMySensitive(true);
                        replyQuery.getReplyFlag().remove(CommentFlag.HAS_SENSITIVE);
                        ListVO<ReplyDO> replyDOListVO = replyService.findByQuery(replyQuery);

                        List<ReplyVO> result = new ArrayList<>();
                        replyDOListVO.getRows().forEach(v -> {
                            ReplyVO replyVO = new ReplyVO(v, extend.getUid());
                            result.add(replyVO);
                        });
                        commentVO.setReply(result);
                    }else{
                        commentVO.setReply(replys);
                    }
                }
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

        return Result.success(commentDO);
    }

    @Override
    public WebResult<CommentVO> getDetail(CommentQuery query) {

        // 校验评论是否打开
        commentService.checkCommentOpen();

        UserExtend extend = GsonUtil.fromGson2Obj(JceHolder.get(Constant.USER_EXTEND), UserExtend.class);

        logger.info("[findListVO] query:{},token:{}", query, extend);

        CommentDO comment = commentService.getById(query.getCommentId());

        ReplyQuery replyQuery = new ReplyQuery();
        replyQuery.setCommentId(query.getCommentId());
        replyQuery.setPageSize(query.getPageSize());
        replyQuery.setCurrentPage(query.getCurrentPage());
        replyQuery.setOrderByEnable(true);
        replyQuery.putOrderBy("id", false);
        replyQuery.setStatus(StatusEnum.NORMAL.getId());
        // 只看没有敏感词的
        replyQuery.getReplyFlag().remove(CommentFlag.HAS_SENSITIVE);

        if (extend != null) {
            replyQuery.setSensitiveUid(extend.getUid());
        }
        // 自己的敏感词评论可以看
        replyQuery.setContainMySensitive(true);
        ListVO<ReplyDO> replys = replyService.findByQuery(replyQuery);

        CommentVO vo = null;
        if (extend != null && extend.getUid() != null) {
            vo = new CommentVO(comment, extend.getUid(), replys);
            vo.setLike(likeService.isLike(query.getCommentId(), extend.getUid()));
        } else {
            vo = new CommentVO(comment, null, replys);
        }
        return WebResult.success(vo);
    }

    @Override
    public WebResult<Void> delete(DeleteParam deleteParam) {

        UserExtend extend = GsonUtil.fromGson2Obj(JceHolder.get(Constant.USER_EXTEND), UserExtend.class);

        logger.info("[delete_comment] param:{},token:{}", deleteParam, extend);

        Assert.notNull(extend, "您尚未登录");
        Assert.notNull(extend.getUid(), "uid not be empty");
        Assert.notNull(deleteParam.getId(), "id not be null");
        Assert.notNull(deleteParam.getType(), "type not be null");

        // 评论
        if (DeleteTypeCommentType == deleteParam.getType()) {

            CommentRequest request = new CommentRequest();
            request.setCommentId(deleteParam.getId());
            request.setUid(extend.getUid());
            commentService.delete(request);
        }

        // 回复
        if (DeleteTypeReplyType == deleteParam.getType()) {
            ReplyRequest replyRequest = new ReplyRequest();
            replyRequest.setUid(extend.getUid());
            replyRequest.setReplyId(deleteParam.getId());
            replyService.delete(replyRequest);
        }

        return WebResult.success();
    }

    @Override
    public Result<SensitiveDTO> checkSensitive(SensitiveRequest sensitiveReq) {

        SensitiveDTO sensitiveDTO = new SensitiveDTO();
        // 默认false
        sensitiveDTO.setHasSensitiveWord(false);
        if (SensitiveWordFilter.isContaintSensitiveWord(sensitiveReq.getTxt(),
                SensitiveWordFilter.minMatchType)) {
            log.info("[checkSensitive] has sensitiveWord ,txt:{}", sensitiveReq.getTxt());
            sensitiveDTO.setHasSensitiveWord(true);
            sensitiveDTO.setSensitiveContent(SensitiveWordFilter.
                    replaceSensitiveWord(sensitiveReq.getTxt(), SensitiveWordFilter.maxMatchType, sensitiveReq.getReplaceChar()));
        }
        return Result.success(sensitiveDTO);
    }
}
