package com.shinemo.score.core.score.facade.impl;

import com.shinemo.client.common.FlagHelper;
import com.shinemo.client.common.Result;
import com.shinemo.client.common.WebResult;
import com.shinemo.client.exception.BizException;
import com.shinemo.score.client.comment.facade.CommentFacadeService;
import com.shinemo.score.client.comment.query.CommentParam;
import com.shinemo.score.client.score.domain.ScoreRequest;
import com.shinemo.score.client.score.facade.ScoreFacadeService;
import com.shinemo.score.client.video.domain.VideoDO;
import com.shinemo.score.client.video.domain.VideoFlag;
import com.shinemo.score.core.score.service.ScoreService;
import com.shinemo.score.core.video.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(rollbackFor = Exception.class)
    public WebResult<Void> submitScore(ScoreRequest request) {

        Assert.notNull(request,"request is null");
        Result<VideoDO> rs = videoService.initVideo(request);
        //插入评分
        if(!rs.hasValue()){
            log.error("[initVideo] error:{}",rs);
            return WebResult.error(rs.getError());
        }
        //评分 或者更新评分
        if(FlagHelper.hasFlag(request.getFlag(), VideoFlag.GRADE) && request.getScore()!=null && request.getScore()>0){

        }




        CommentParam param = initCommentParam(request);
        Result<Void> commentRs = commentFacadeService.submit(param);
        if (!commentRs.isSuccess()) {
            throw new BizException(commentRs.getError());
        }
        return WebResult.success();
    }

    private CommentParam initCommentParam(ScoreRequest request) {
        CommentParam param = new CommentParam();
        param.setComment(request.getComment());
        param.setNetType(request.getNetType());
        param.setVideoId(request.getVideoId());
        param.setVideoType(request.getFlag());
        return param;
    }
}
