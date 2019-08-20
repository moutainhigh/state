package com.shinemo.score.core.comment.service.impl;

import com.shinemo.client.common.Errors;
import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.client.common.StatusEnum;
import com.shinemo.client.exception.BizException;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.mgsuggest.client.domain.DistributeConfig;
import com.shinemo.mgsuggest.client.facade.DistributeConfigFacadeService;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.client.comment.domain.CommentFlag;
import com.shinemo.score.client.comment.domain.LikeTypeEnum;
import com.shinemo.score.client.comment.query.CommentQuery;
import com.shinemo.score.client.comment.query.CommentRequest;
import com.shinemo.score.client.error.ScoreErrors;
import com.shinemo.score.core.comment.cache.CommentCache;
import com.shinemo.score.core.comment.service.CommentService;
import com.shinemo.score.core.word.SensitiveWordFilter;
import com.shinemo.score.dal.comment.wrapper.CommentWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @author wenchao.li
 * @since 2019-06-06
 */
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    public static final String SENSITIVE_REPLACE = "*";

    @Resource
    private CommentWrapper commentWrapper;
    @Resource
    private CommentCache commentCache;
    @Resource
    private DistributeConfigFacadeService distributeConfigFacadeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentDO create(CommentRequest request) {

        Assert.hasText(request.getContent(), "内容不能为空");
        Assert.isTrue(request.getContent().length() <= 200, "内容长度不能超过200字");
        Assert.notNull(request.getVideoId(), "videoId not be empty");
        Assert.hasText(request.getMobile(), "mobile not be empty");
        Assert.notNull(request.getVideoType(), "videoType not be empty");
        Assert.notNull(request.getUid(), "uid not be empty");


        // 是否含有敏感词，
        // 这边只打个标,给前端返回含敏感词的评论内容时处理成*
        if (SensitiveWordFilter.isContaintSensitiveWord(request.getContent(),
                SensitiveWordFilter.minMatchTYpe)) {
            log.error("[create_comment] has sensitiveWord ,txt:{}", request.getContent());
            request.getCommentFlag().add(CommentFlag.HAS_SENSITIVE);
        }

        CommentDO commentDO = new CommentDO();
        BeanUtils.copyProperties(request, commentDO);
        // 标位
        commentDO.setFlag(request.getCommentFlag().getValue());
        Result<CommentDO> insertRs = commentWrapper.insert(commentDO);
        if (!insertRs.hasValue()) {
            log.error("[create_comment] has err:commentDO:{},rs:{}", commentDO, insertRs);
            throw new BizException(insertRs.getError());
        }

        CommentDO comment = getByIdFromDB(insertRs.getValue().getId());
        // 加入缓存
        commentCache.put(comment);
        return comment;
    }

    @Override
    public void update(CommentRequest request) {

        Assert.notNull(request.getCommentId(), "commentId not be null");

        boolean upSucc = doUpdate(request);
        int retryTimes = 10;

        while (!upSucc && retryTimes > 0) {
            upSucc = doUpdate(request);
            retryTimes--;
        }

        if (upSucc) {
            // 更新成功refresh缓存
            commentCache.refresh(request.getCommentId());
        } else {
            // 重试10次后还没更新成功，系统繁忙
            throw new BizException(Errors.FAILURE);
        }
    }

    @Override
    public CommentDO getById(Long commentId) {

        Assert.notNull(commentId, "commentId not be null");

        // 先走缓存
        CommentDO cache = commentCache.get(commentId);

        if (cache != null) {
            log.info("load comment from cache,id:{}", commentId);
            return cache;
        }

        return getByIdFromDB(commentId);
    }

    @Override
    public CommentDO getByQuery(CommentQuery query) {

        if (query.getId() != null) {
            // 有id先走缓存
            return getById(query.getId());
        }
        log.info("load comment from mysql db,query:{}", query);
        Result<CommentDO> commentRs = commentWrapper.get(query, ScoreErrors.COMMENT_NOT_EXIST);
        if (!commentRs.hasValue()) {
            throw new BizException(commentRs.getError());
        }
        return commentRs.getValue();
    }

    @Override
    public ListVO<CommentDO> findByQuery(CommentQuery query) {

        Result<ListVO<CommentDO>> listRs = commentWrapper.find(query);
        if (!listRs.hasValue()) {
            throw new BizException(listRs.getError());
        }
        return listRs.getValue();
    }

    @Override
    public ListVO<Long> findIdsByQuery(CommentQuery query) {

        query.setOrderByEnable(true);
        query.putOrderBy("id", false);
        query.setStatus(StatusEnum.NORMAL.getId());
        Result<ListVO<Long>> idListRs = commentWrapper.findIds(query);
        if (!idListRs.hasValue()) {
            throw new BizException(idListRs.getError());
        }

        return idListRs.getValue();
    }

    @Override
    public CommentDO getByIdFromDB(Long commentId) {
        Assert.notNull(commentId, "commentId not be null");

        CommentQuery query = new CommentQuery();
        query.setId(commentId);
        log.info("load comment from mysql db,commentId:{}", commentId);
        Result<CommentDO> commentRs = commentWrapper.get(query, ScoreErrors.COMMENT_NOT_EXIST);
        if (!commentRs.hasValue()) {
            throw new BizException(commentRs.getError());
        }
        return commentRs.getValue();
    }

    @Override
    public void checkCommentOpen() {
        Result<DistributeConfig> configRs = distributeConfigFacadeService.load();
        if (!configRs.hasValue()) {
            // 这边不处理
            log.error("[checkCommentOpen]get config has error,rs:{}", configRs);
            return;
        }
        DistributeConfig config = configRs.getValue();
        if (!config.isCommentOpen()) {
            throw new BizException(ScoreErrors.COMMENT_IS_CLOSED);
        }
    }

    @Override
    public void delete(CommentRequest delReq) {

        Assert.notNull(delReq.getUid(), "uid not be null");
        Assert.notNull(delReq.getCommentId(), "commentId not be null");

        CommentDO commentDO = getById(delReq.getCommentId());

        // 判断是否是自己
        Assert.isTrue(commentDO.getUid().equals(delReq.getUid()), "只能删除自己的评论");

        // 更新为已删除状态
        CommentRequest upReq = new CommentRequest();
        upReq.setCommentId(delReq.getCommentId());
        upReq.setStatus(StatusEnum.DELETE.getId());
        update(upReq);
    }

    @Override
    public void transferSensitiveWord(CommentDO commentDO) {
        // 如果有敏感词
        if (commentDO.hasSensitiveWord()) {
            commentDO.setContent(SensitiveWordFilter.
                    replaceSensitiveWord(commentDO.getContent(), SensitiveWordFilter.maxMatchType, SENSITIVE_REPLACE)
                    + "(含有敏感词)");
        }
    }

    private boolean doUpdate(CommentRequest request) {

        CommentDO oldDO = getById(request.getCommentId());

        CommentDO commentDO = new CommentDO();
        commentDO.setId(request.getCommentId());
        commentDO.setVersion(oldDO.getVersion());
        if (request.getHistoryReply() != null && !request.getHistoryReply().isEmpty()) {
            commentDO.setHistoryReply(GsonUtil.toJson(request.getHistoryReply()));
        }
        if (request.getLikeAction() != null) {
            if (LikeTypeEnum.ADD.getId() == request.getLikeAction()) {
                commentDO.setLikeNum(oldDO.getLikeNum() + 1);
            }
            if (LikeTypeEnum.REMOVE.getId() == request.getLikeAction()) {
                commentDO.setLikeNum(oldDO.getLikeNum() - 1);
            }
        }

        if (request.isIncrReplyNum()) {
            commentDO.setReplyNum(oldDO.getReplyNum() + 1);
        }

        if (request.getStatus() != null) {
            commentDO.setStatus(request.getStatus());
        }

        if (request.getFlag() != null) {
            commentDO.setFlag(request.getFlag());
        }

        Result<CommentDO> updateRs = commentWrapper.update(commentDO);
        if (!updateRs.hasValue()) {
            log.error("[doUpdate] update error param:{},rs:{}", commentDO, updateRs);
            return false;
        }
        return true;
    }
}
