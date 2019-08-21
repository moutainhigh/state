package com.shinemo.score.core.score.facade.impl;

import com.shinemo.client.common.Result;
import com.shinemo.client.util.DateUtil;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.muic.client.token.facade.TokenFacadeService;
import com.shinemo.muic.client.user.domain.UserBaseInfoDO;
import com.shinemo.my.redis.service.RedisService;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


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

    @Resource
    private CalculationFacadeService calculationFacadeService;

    @Resource
    private RedisService redisService;


    private static final Executor poolExecutor = Executors.newFixedThreadPool(50);


    private String getRedisUidKey(String mobile){
        return String.format("MIGU_FIX_SCORE_USERBASEINFO_%s",mobile);
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
        }
        return true;
    }

    private void subRun(List<VideoTmp> list,int j){
        long start = System.currentTimeMillis();
        long count = 0;
        log.info("[initScore] page:{},start:{}",j,start);
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
        log.info("[initScore] finish page:{} errorCount:{} start:{},finish:{},cost:{}",j,count,start,end,end-start);

    }

    @Override
    public Result<Void> initScore(){
        long count = 0;
        VideoTmpQuery query = new VideoTmpQuery();
        query.setPageEnable(false);
        List<VideoTmp> list = videoTmpMapper.find(query);
        int size = list.size();
        if (size % 1000 == 0) {
            count = size / 1000;
        } else {
            count =  size / 1000 +1; ;
        }
        for (int i = 0; i < count; i++) {
            List<VideoTmp> subList = list.subList(i * 1000, ((i + 1) * 1000 > size ? size : 1000 * (i + 1)));
            int j = i;
            poolExecutor.execute(()->{
                subRun(subList,j);
            });
        }
        return Result.success();
    }

    private boolean insert(List<UserTmp>userList, VideoTmp tmp){

        VideoQuery query = new VideoQuery();
        query.setVideoId(tmp.getXmVideoId());
        VideoDO newVideoDO = videoMapper.get(query);
        if(newVideoDO == null){
            log.error("[video] not exist id:{}",tmp.getXmVideoId());
            return false;
        }
        ScoreQuery tmpQuery = new ScoreQuery();
        ScoreQuery numQuery = new ScoreQuery();
        for(UserTmp iter:userList){
            ScoreDO domain = new ScoreDO();
            domain.setStatus(1);
            domain.setVideoId(newVideoDO.getId());
            UserBaseInfoDO userBaseInfoDO = redisService.get(getRedisUidKey(iter.getMobile()),UserBaseInfoDO.class);
            if(userBaseInfoDO!=null){
                domain.setUid(userBaseInfoDO.getId());
            }else{
                Result<UserBaseInfoDO> rs = tokenFacadeService.getUserByMobile(iter.getMobile());
                if(!rs.hasValue()){
                    log.error("[getUserByMobile] error mobile:{}",iter.getMobile());
                    continue;
                }
                domain.setUid(rs.getValue().getId());
                redisService.set(getRedisUidKey(iter.getMobile()),rs.getValue(),3600);
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
            try {
                scoreTempMapper.insert(domain);
            } catch (Exception e) {
                log.error("[scoreTempMapper] insert error,",e);
            }
        }
        return true;
    }

    private void subOnine(List<ScoreDO> list,String videoId){

        VideoQuery videoquery = new VideoQuery();
        videoquery.setVideoId(videoId);
        VideoDO newVideoDO = videoMapper.get(videoquery);
        if(newVideoDO ==null){
            newVideoDO = new VideoDO();
            newVideoDO.setStatus(DeleteStatusEnum.NORMAL.getId());;
            newVideoDO.setVersion(1L);
            newVideoDO.setVideoId(videoId);
            newVideoDO.addVideoFlag(VideoFlag.GRADE);
            newVideoDO.setInitScore(7000L);
            newVideoDO.setInitWeight(1000L);
            newVideoDO.setWeight(newVideoDO.getInitWeight());
            newVideoDO.setScore(newVideoDO.getInitScore());
            newVideoDO.setYesterdayScore(newVideoDO.getInitScore());
            newVideoDO.setYesterdayWeight(newVideoDO.getInitWeight());
            videoMapper.insert(newVideoDO);
        }
        Long id = newVideoDO.getId();
        long count = 0;
        int size = list.size();
        if (size % 300 == 0) {
            count = size / 300;
        } else {
            count =  size / 300 +1; ;
        }
        for (int i = 0; i < count; i++) {
            List<ScoreDO> subList = list.subList(i * 300, ((i + 1) * 300 > size ? size : 300 * (i + 1)));
            poolExecutor.execute(()->{
                subAndSubRun(subList,id);
            });
        }
    }


    private void subAndSubRun(List<ScoreDO> list,Long videoId){
        ScoreQuery tempQuery = new ScoreQuery();
        for(ScoreDO iter:list){
            tempQuery.setUid(iter.getUid());
            ScoreDO scoreDO = scoreTempMapper.getScoreByMaxNum(tempQuery);
            if(scoreDO!=null){
                iter.setNum(scoreDO.getNum()+1);
            }else{
                iter.setNum(1L);
            }
            iter.setVideoId(videoId);
            scoreTempMapper.insert(iter);
        }
    }

    @Override
    public Result<Void> addOnlineScore(Long minId){
        ScoreQuery query = new ScoreQuery();
        query.setPageEnable(false);
        query.setMinId(minId);
        List<ScoreDO> list = scoreMapper.find(query);
        Map<String,List<ScoreDO>> vMap = list.stream().collect(Collectors.groupingBy(ScoreDO::getThirdVideoId));
        vMap.forEach((K,V)-> {
            poolExecutor.execute(()->{
                subOnine(V,K);
            });
        });
        return Result.success();
    }

    @Override
    public Result<Void> calculateScore(){
        VideoTmpQuery query = new VideoTmpQuery();
        query.setPageEnable(false);
        List<VideoTmp> rs = videoTmpMapper.find(query);
        long count = 0;
        int size = rs.size();
        if (size % 500 == 0) {
            count = size / 500;
        } else {
            count =  size / 500 +1; ;
        }
        for (int i = 0; i < count; i++) {
            List<VideoTmp> subList = rs.subList(i * 500, ((i + 1) * 500 > size ? size : 500 * (i + 1)));
            int j = i;
            poolExecutor.execute(()->{
                subCalculate(subList,j);
            });
        }
        return Result.success();
    }

    private void subCalculate(List<VideoTmp> list,int j){
        long start = System.currentTimeMillis();
        log.info("[subCalculate] page:{} start:{}",j,start);
        for(VideoTmp iter:list){
            Result<Void> ret = calculationFacadeService.calculationByThirdId(null,iter.getXmVideoId());
            if(!ret.isSuccess()){
                log.error("[calculateScore] error videoId:{}",iter.getXmVideoId());
            }
        }
        long end = System.currentTimeMillis();
        log.info("[subCalculate] finish page:{} start:{} end:{} cost:{}",j,start,end,end-start);
    }
}
