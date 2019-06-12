package com.shinemo.score.core.score.facade.impl;

import com.shinemo.client.common.FlagHelper;
import com.shinemo.client.common.Result;
import com.shinemo.client.common.WebResult;
import com.shinemo.client.exception.BizException;
import com.shinemo.score.client.comment.facade.CommentFacadeService;
import com.shinemo.score.client.comment.query.CommentParam;
import com.shinemo.score.client.common.domain.DeleteStatusEnum;
import com.shinemo.score.client.score.domain.ScoreDO;
import com.shinemo.score.client.score.domain.ScoreRequest;
import com.shinemo.score.client.score.facade.ScoreFacadeService;
import com.shinemo.score.client.video.domain.VideoDO;
import com.shinemo.score.client.video.domain.VideoFlag;
import com.shinemo.score.core.score.service.ScoreService;
import com.shinemo.score.core.video.service.VideoService;
import com.shinemo.ygw.client.migu.UserExtend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @author wenchao.li
 * @since 2019-06-12
 */
@Service("scoreFacadeService")
@Slf4j
public class ScoreFacadeServiceImpl implements ScoreFacadeService {

    @Resource
    private CommentFacadeService commentFacadeService;

    @Resource
    private VideoService videoService;

    @Resource
    private ScoreService scoreService;

    @Override
    public WebResult<Void> submitScore(ScoreRequest request) {

        Assert.notNull(request,"request is null");
        Result<VideoDO> rs = videoService.initVideo(request);
        if(!rs.hasValue()){
            log.error("[initVideo] error:{}",rs);
            return WebResult.error(rs.getError());
        }
        //评分 或者更新评分
        if(FlagHelper.hasFlag(request.getFlag(), VideoFlag.GRADE) && request.getScore()!=null && request.getScore()>0){
            ScoreDO scoreDomain = initScoreDO(request,rs.getValue().getId());
            Result<ScoreDO> rt = scoreService.insertScore(scoreDomain);
            if(!rt.hasValue()){
                return WebResult.error(rt.getError());
            }
        }
        CommentParam param = initCommentParam(request,rs.getValue().getId());
        Result<Void> commentRs = commentFacadeService.submit(param);
        if (!commentRs.isSuccess()) {
            throw new BizException(commentRs.getError());
        }
        return WebResult.success();
    }

    private ScoreDO initScoreDO(ScoreRequest request, Long id) {
        ScoreDO scoreDomain = new ScoreDO();
        scoreDomain.setScore(request.getScore());
        scoreDomain.setStatus(DeleteStatusEnum.NORMAL.getId());
        scoreDomain.setUid(UserExtend.getUserId());
        scoreDomain.setVideoId(id);
        scoreDomain.setExtend(request.getExtend());
        return scoreDomain;
    }

    private CommentParam initCommentParam(ScoreRequest request,Long videoId) {
        CommentParam param = new CommentParam();
        param.setComment(request.getComment());
        param.setNetType(request.getNetType());
        param.setVideoId(videoId);
        param.setVideoType(request.getFlag());
        return param;
    }
}
