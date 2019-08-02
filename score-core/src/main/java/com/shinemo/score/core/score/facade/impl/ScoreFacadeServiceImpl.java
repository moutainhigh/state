package com.shinemo.score.core.score.facade.impl;

import com.shinemo.client.common.FlagHelper;
import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.client.common.WebResult;
import com.shinemo.client.exception.BizException;
import com.shinemo.my.redis.service.RedisService;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.client.comment.domain.CommentVO;
import com.shinemo.score.client.comment.facade.CommentFacadeService;
import com.shinemo.score.client.comment.query.CommentParam;
import com.shinemo.score.client.comment.query.CommentQuery;
import com.shinemo.score.client.common.domain.DeleteStatusEnum;
import com.shinemo.score.client.error.ScoreErrors;
import com.shinemo.score.client.score.domain.*;
import com.shinemo.score.client.score.facade.ScoreFacadeService;
import com.shinemo.score.client.score.query.ScoreQuery;
import com.shinemo.score.client.video.domain.VideoDO;
import com.shinemo.score.client.video.domain.VideoDTO;
import com.shinemo.score.client.video.domain.VideoFlag;
import com.shinemo.score.client.video.query.VideoQuery;
import com.shinemo.score.core.score.service.ScoreService;
import com.shinemo.score.core.video.service.VideoService;
import com.shinemo.ygw.client.migu.UserExtend;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author wenchao.li
 * @since 2019-06-12
 */
@Service("scoreFacadeService")
@Slf4j
public class ScoreFacadeServiceImpl implements ScoreFacadeService {


    private final static long FIRST = 1;

    @Resource
    private CommentFacadeService commentFacadeService;

    @Resource
    private VideoService videoService;

    @Resource
    private ScoreService scoreService;

    @Resource
    private RedisService redisService;

    private static final String KEY = "MIGU_SCORE_VIDEO_ID_%s";


    @Override
    @Transactional(rollbackFor = Exception.class)
    public WebResult<ScoreDTO> submitScore(ScoreRequest request) {

        Assert.notNull(request, "request is null");
        if((request.getScore() == null || request.getScore() == 0) && StringUtils.isBlank(request.getComment())){
            log.error("[submitScore] score and comment is null");
            return WebResult.error(ScoreErrors.PARAM_ERROR);
        }
        Result<VideoDO> rs = videoService.initVideo(request);
        if (!rs.hasValue()) {
            log.error("[initVideo] error:{}", rs);
            return WebResult.error(rs.getError());
        }
        //评分 或者更新评分
        if (FlagHelper.hasFlag(request.getFlag(), VideoFlag.GRADE) && request.getScore() != null && request.getScore() > 0) {
            ScoreDO scoreDomain = initScoreDO(request, rs.getValue().getId());
            Result<ScoreDO> rt = scoreService.insertScore(scoreDomain);
            if (!rt.hasValue()) {
                return WebResult.error(rt.getError());
            }
        }
        CommentParam param = initCommentParam(request);
        Result<CommentDO> commentRs = commentFacadeService.submit(param);
        if (!commentRs.isSuccess()) {
            throw new BizException(commentRs.getError());
        }

        if (commentRs.hasValue()) {
            ScoreDTO result = new ScoreDTO();
            result.setCommentId(commentRs.getValue().getId());
            return WebResult.success(result);
        }
        return WebResult.success();
    }

    @Override
    public WebResult<MyScoreDTO> getMyScore(MyScoreRequest request) {

        MyScoreDTO ret = new MyScoreDTO();
        long num = FIRST;
        ScoreQuery query = new ScoreQuery();
        query.setUid(UserExtend.getUserId());
        query.setOrderByEnable(true);
        query.putOrderBy("num", false);
        Result<ScoreDO> rs = scoreService.getScore(query);
        if (rs.hasValue()) {
            num = rs.getValue().getNum() + FIRST;
        }
        ret.setNumber(num);
        //查询我评论过的音频信息
        if (!StringUtils.isBlank(request.getVideoId())) {
            query.setThirdVideoId(request.getVideoId());
            Result<ScoreDO> rt = scoreService.getScore(query);
            if (rt.hasValue()) {
                ret.setNumber(rt.getValue().getNum());
                ret.setVideoId(request.getVideoId());
                ret.setScore(Double.valueOf(rt.getValue().getScore()));
            }
            CommentQuery commentQuery = new CommentQuery();
            commentQuery.setUid(UserExtend.getUserId());
            commentQuery.setVideoId(request.getVideoId());
            commentQuery.setPageEnable(false);
            WebResult<ListVO<CommentVO>> commentRs = commentFacadeService.findListVO(commentQuery);
            if (commentRs.isSuccess() && commentRs.getData() != null && !CollectionUtils.isEmpty(commentRs.getData().getRows())) {
                ret.setComments(commentRs.getData().getRows());
            }
        }
        return WebResult.success(ret);
    }

    @Override
    public WebResult<VideoDTO> getVideoScore(MyScoreRequest request) {

        Assert.notNull(request, "videoId is null");
        Assert.hasText(request.getVideoId(), "videoId is null");
        VideoDTO result = getByCache(request);
        if(result != null){
            return WebResult.success(result);
        }
        VideoQuery query = new VideoQuery();
        query.setVideoId(request.getVideoId());
        Result<VideoDO> rs = videoService.getVideo(query);
        if (!rs.hasValue()) {
            ScoreRequest scoreRequest = new ScoreRequest();
            scoreRequest.setVideoName(request.getVideoName());
            scoreRequest.setVideoId(request.getVideoId());
            scoreRequest.setExtend(request.getExtend());
            scoreRequest.setFlag(VideoFlag.GRADE.getIndex());
            Result<VideoDO> initRs = videoService.initVideo(scoreRequest);
            if (!initRs.hasValue()) {
                return WebResult.error(initRs.getError());
            }
            rs.setValue(initRs.getValue());
        }
        VideoDTO dto = new VideoDTO();
        double scoreCount = Double.valueOf(rs.getValue().getScore());
        double score = scoreCount / rs.getValue().getWeight();
        BigDecimal scoreDecimal = new BigDecimal(score);
        dto.setScore(scoreDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
        dto.setVideoId(request.getVideoId());
        dto.setWeight(rs.getValue().getWeight());
        setCache(dto);

        return WebResult.success(dto);
    }

    private void setCache(VideoDTO dto) {
        try {
            redisService.set(String.format(KEY,dto.getVideoId()),dto,3600);
        } catch (Exception e) {
            log.error("[getVideoScore] setCache error",e);
        }
    }

    private VideoDTO getByCache(MyScoreRequest request) {
        try {
            return redisService.get(String.format(KEY,request.getVideoId()),VideoDTO.class);
        } catch (Exception e) {
            log.error("[getVideoScore] getCache error",e);
            return null;
        }
    }

    private ScoreDO initScoreDO(ScoreRequest request, Long id) {
        ScoreDO scoreDomain = new ScoreDO();
        scoreDomain.setScore(request.getScore());
        scoreDomain.setStatus(DeleteStatusEnum.NORMAL.getId());
        scoreDomain.setUid(UserExtend.getUserId());
        scoreDomain.setVideoId(id);
        scoreDomain.setThirdVideoId(request.getVideoId());
        //scoreDomain.setExtend(request.getExtend());
        return scoreDomain;
    }

    private CommentParam initCommentParam(ScoreRequest request) {
        CommentParam param = new CommentParam();
        param.setComment(request.getComment());
        param.setNetType(request.getNetType());
        param.setVideoId(request.getVideoId());
        param.setVideoType(request.getFlag());
        param.setShowDevice(request.getShowDevice());
        return param;
    }
}
