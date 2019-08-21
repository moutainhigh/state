package com.shinemo.score.core.score.facade.impl;

import com.shinemo.client.common.Result;
import com.shinemo.client.util.DateUtil;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.muic.client.token.facade.TokenFacadeService;
import com.shinemo.muic.client.user.domain.UserBaseInfoDO;
import com.shinemo.score.client.comment.domain.CalculationEnum;
import com.shinemo.score.client.common.domain.DeleteStatusEnum;
import com.shinemo.score.client.score.domain.ScoreDO;
import com.shinemo.score.client.score.domain.UserTmp;
import com.shinemo.score.client.score.domain.VideoTmp;
import com.shinemo.score.client.score.facade.CalculationFacadeService;
import com.shinemo.score.client.score.facade.FixDataFacadeService;
import com.shinemo.score.client.score.query.ScoreQuery;
import com.shinemo.score.client.score.query.UserTmpQuery;
import com.shinemo.score.client.score.query.VideoTmpQuery;
import com.shinemo.score.client.video.domain.VideoDO;
import com.shinemo.score.client.video.domain.VideoFlag;
import com.shinemo.score.client.video.query.VideoQuery;
import com.shinemo.score.dal.score.mapper.ScoreMapper;
import com.shinemo.score.dal.score.mapper.ScoreTempMapper;
import com.shinemo.score.dal.score.mapper.UserTmpMapper;
import com.shinemo.score.dal.score.mapper.VideoTmpMapper;
import com.shinemo.score.dal.video.mapper.VideoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class FixDataFacadeServiceImpl implements FixDataFacadeService {

    @Resource
    private VideoTmpMapper videoTmpMapper;

    @Resource
    private UserTmpMapper userTmpMapper;

    @Resource
    private VideoMapper videoMapper;

    @Resource
    private ScoreTempMapper scoreTempMapper;

    @Resource
    private TokenFacadeService tokenFacadeService;

    @Resource
    private ScoreMapper scoreMapper;

    private Map<String, UserBaseInfoDO> userMap;

    private Map<String,VideoDO> videoDOMap;

    private Map<Long,Long> userNumMap;

    @Resource
    private CalculationFacadeService calculationFacadeService;



    @PostConstruct
    public void init(){
        userMap = new HashMap<>(800000);
        videoDOMap = new HashMap<>(20000);
        userNumMap = new HashMap<>(800000);
    }



    @Override
    public Result<Void> fixVideo(){
        long start = System.currentTimeMillis();
        log.info("[fixVideoFinish] start:{}",start);
        VideoTmpQuery query = new VideoTmpQuery();
        query.setPageEnable(false);
        List<VideoTmp> rs = videoTmpMapper.find(query);
        long count = 0;
        for(VideoTmp iter:rs){
            if(!insertOrUpdateVideo(iter)){
                count++;
            }
        }
        long end = System.currentTimeMillis();
        log.info("[fixVideoFinish] finish errorCount:{} start:{},finish:{},cost:{}",count,start,end,end-start);
        return Result.success();
    }

    private boolean insertOrUpdateVideo(VideoTmp iter) {
        VideoQuery query = new VideoQuery();
        query.setVideoId(iter.getXmVideoId());
        VideoDO video = videoMapper.get(query);
        double initScore = iter.getScore()*iter.getWeight();
        if(video!=null){
            videoDOMap.put(video.getVideoId(),video);
            video.setInitScore(new Double(initScore).longValue());
            video.setInitWeight(iter.getWeight());
            if(StringUtils.isBlank(iter.getVideoName())){
                video.setVideoName(iter.getVideoName());
            }
            video.setYesterdayScore(video.getScore());
            video.setYesterdayWeight(video.getWeight());
            int rs = videoMapper.update(video);
            if(rs<1){
                log.error("[insertOrUpdateVideo] updateError iter:{}", GsonUtil.toJson(iter));
                return false;
            }
        }else{
            video = new VideoDO();
            video.setStatus(DeleteStatusEnum.NORMAL.getId());
            video.setVideoName(iter.getVideoName());
            video.setVersion(1L);
            video.setVideoId(iter.getXmVideoId());
            video.addVideoFlag(VideoFlag.GRADE);
            video.setInitScore(new Double(initScore).longValue());
            video.setInitWeight(iter.getWeight());
            video.setWeight(video.getInitWeight());
            video.setScore(video.getInitScore());
            video.setYesterdayScore(video.getInitScore());
            video.setYesterdayWeight(video.getInitWeight());
            long rs = videoMapper.insert(video);
            if(rs<1){
                log.error("[insertOrUpdateVideo] insertError iter:{}", GsonUtil.toJson(iter));
                return false;
            }
            videoDOMap.put(video.getVideoId(),video);
        }
        return true;
    }

    @Override
    public Result<Void> initScore(){
        long start = System.currentTimeMillis();
        long count = 0;
        log.info("[initScore] start:{}",start);
        VideoTmpQuery query = new VideoTmpQuery();
        query.setPageEnable(false);
        List<VideoTmp> list = videoTmpMapper.find(query);
        UserTmpQuery userTemQuery = new UserTmpQuery();
        userTemQuery.setPageEnable(false);
        for(VideoTmp iter:list){
            userTemQuery.setVideoId(iter.getVideoId());
            List<UserTmp> userList = userTmpMapper.find(userTemQuery);
            if(CollectionUtils.isEmpty(userList)){
                continue;
            }
            if(!insert(userList,iter)){
                log.error("[initScore] batchInsert error videoId:{}",iter.getXmVideoId());
                count++;
            }
        }
        long end = System.currentTimeMillis();
        log.info("[initScore] finish errorCount:{} start:{},finish:{},cost:{}",count,start,end,end-start);
        return Result.success();
    }

    private boolean insert(List<UserTmp>userList, VideoTmp tmp){
        VideoQuery query = new VideoQuery();
        ScoreQuery tmpQuery = new ScoreQuery();
        for(UserTmp iter:userList){



            ScoreDO domain = new ScoreDO();
            domain.setStatus(1);
            VideoDO videoDO = videoDOMap.get(tmp.getXmVideoId());
            if(videoDO!=null){
                domain.setVideoId(videoDO.getId());
            }else{
                query.setVideoId(tmp.getXmVideoId());
                VideoDO newVideoDO = videoMapper.get(query);
                if(newVideoDO!=null){
                    domain.setVideoId(newVideoDO.getId());
                    videoDOMap.put(tmp.getXmVideoId(),newVideoDO);
                }else{
                    log.error("[video] not exist id:{}",tmp.getXmVideoId());
                    continue;
                }
            }
            UserBaseInfoDO userBaseInfoDO = userMap.get(iter.getMobile());
            if(userBaseInfoDO!=null){
                domain.setUid(userBaseInfoDO.getId());
            }else{
                Result<UserBaseInfoDO> rs = tokenFacadeService.getUserByMobile(iter.getMobile());
                if(!rs.hasValue()){
                    log.error("[getUserByMobile] error mobile:{}",iter.getMobile());
                    continue;
                }
                domain.setUid(rs.getValue().getId());
                userMap.put(iter.getMobile(),rs.getValue());
            }
            tmpQuery.setUid(domain.getUid());
            tmpQuery.setVideoId(domain.getVideoId());
            ScoreDO check = scoreTempMapper.get(tmpQuery);
            if(check!=null){
                log.error("[check] score domain:{}",GsonUtil.toJson(domain));
                continue;
            }
            domain.setGmtCreate(DateUtil.format("2019-08-01","yyyy-MM-dd"));
            domain.setGmtModified(DateUtil.format("2019-08-01","yyyy-MM-dd"));
            domain.setScore(iter.getScore());
            domain.setVersion(1L);
            domain.setThirdVideoId(tmp.getXmVideoId());
            Long num = userNumMap.get(domain.getUid());
            if(num == null){
                num = 1L;
            }
            domain.setNum(num);
            scoreTempMapper.insert(domain);
            userNumMap.put(domain.getUid(),num+1);
        }
        return true;
    }

    @Override
    public Result<Void> addOnlineScore(Long minId){
        ScoreQuery query = new ScoreQuery();
        query.setPageEnable(false);
        query.setMinId(minId);
        List<ScoreDO> list = scoreMapper.find(query);
        ScoreQuery tempQuery = new ScoreQuery();
        for(ScoreDO iter:list){
            tempQuery.setUid(iter.getUid());
            ScoreDO scoreDO = scoreTempMapper.getScoreByMaxNum(tempQuery);
            if(scoreDO!=null){
                iter.setNum(scoreDO.getNum());
            }else{
                iter.setNum(1L);
            }
            scoreTempMapper.insert(iter);
        }
        return Result.success();
    }

    @Override
    public Result<Void> calculateScore(){
        VideoTmpQuery query = new VideoTmpQuery();
        query.setPageEnable(false);
        List<VideoTmp> rs = videoTmpMapper.find(query);
        for(VideoTmp iter:rs){
            Result<Void> ret = calculationFacadeService.calculationByTime(null,null,
                    CalculationEnum.all,null,iter.getXmVideoId());
            if(!ret.isSuccess()){
                log.error("[calculateScore] error videoId:{}",iter.getXmVideoId());
            }
        }
        return Result.success();
    }


}
