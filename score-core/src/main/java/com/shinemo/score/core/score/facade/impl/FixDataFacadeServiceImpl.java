package com.shinemo.score.core.score.facade.impl;

import com.google.common.collect.Lists;
import com.shinemo.client.common.ListVO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
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

    @Override
    public void fixScoreNum() {
        long startTime = System.currentTimeMillis();
        ScoreQuery query = new ScoreQuery();
        query.setPageEnable(false);
        List<Long> list = scoreTempMapper.findUid(query);
        long count=0;
        int size = list.size();
        if (size % 2000 == 0) {
            count = size / 2000;
        } else {
            count =  size / 2000 +1; ;
        }
        for (int i = 0; i < count; i++) {
            List<Long> subList = list.subList(i * 2000, ((i + 1) * 2000 > size ? size : 2000 * (i + 1)));
            int j = i;
            poolExecutor.execute(()->{
                updateNum(subList,j);
            });
        }
        long endTime = System.currentTimeMillis();
        log.info("[fixScoreNum] start:{},end:{} cost:{}",startTime,endTime,endTime-startTime);
    }

    public static  void main(String args[]){


        ScoreDO a = new ScoreDO();
        a.setId(1L);
        a.setNum(null);
        ScoreDO b= new ScoreDO();
        b.setId(2L);
        b.setNum(2L);
        ScoreDO c = new ScoreDO();
        c.setId(3L);
        c.setNum(3L);

        ScoreDO d = new ScoreDO();
        d.setId(3L);
        d.setNum(null);
        List<ScoreDO> scoreDOList = Lists.newArrayList(a,b,c,d);
        Collections.sort(scoreDOList, new Comparator<ScoreDO>() {
            public int compare(ScoreDO arg0, ScoreDO arg1) {
                if( arg0.getNum() == null){
                    return 1;
                }
                if(arg1.getNum() == null){
                    return -1;
                }
                if(arg0.getNum() > arg1.getNum()){
                    return 1;
                }
                return -1;
            }
        });
        System.out.println(GsonUtil.toJson(scoreDOList));

    }

    private void updateNum(List<Long>list,int j){
        long startTime = System.currentTimeMillis();
        log.info("[updateNum] page:{} start:{}",j,startTime);
        ScoreQuery query = new ScoreQuery();
        query.setPageEnable(false);
        for(Long iter:list){
            query.setUid(iter);
            List<ScoreDO> scoreDOList = scoreTempMapper.find(query);
            Collections.sort(scoreDOList, new Comparator<ScoreDO>() {
                public int compare(ScoreDO arg0, ScoreDO arg1) {
                    if( arg0.getNum() == null){
                        return 1;
                    }
                    if(arg1.getNum() == null){
                        return -1;
                    }
                    if(arg0.getNum() > arg1.getNum()){
                        return 1;
                    }
                    return -1;
                }
            });
            for(int i=0;i<scoreDOList.size();i++){
                if(scoreDOList.get(i).getNum()!=null){
                    continue;
                }
                long num = i+1;
                ScoreDO scoreDO = new ScoreDO();
                scoreDO.setId(scoreDOList.get(i).getId());
                scoreDO.setVersion(scoreDOList.get(i).getVersion());
                scoreDO.setNum(num);
                int upt = scoreTempMapper.update(scoreDO);
                if(upt<1){
                    log.error("[update] error:{}",upt);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("[updateNum] page:{} start:{},end:{} cost:{}",j,startTime,endTime,endTime-startTime);
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
        subAndSubRun(list,id);;
    }


    private void subAndSubRun(List<ScoreDO> list,Long videoId){
        ScoreQuery tempQuery = new ScoreQuery();
        for(ScoreDO iter:list){
            tempQuery.setUid(iter.getUid());
            tempQuery.setVideoId(videoId);
            ScoreDO check = scoreTempMapper.get(tempQuery);
            if(check!=null){
                log.error("[check] score domain:{}",GsonUtil.toJson(iter));
                continue;
            }
            iter.setId(null);
            tempQuery.setVideoId(null);
            ScoreDO scoreDO = scoreTempMapper.getScoreByMaxNum(tempQuery);
            if(scoreDO!=null){
                iter.setNum(scoreDO.getNum()+1);
            }else{
                iter.setNum(1L);
            }
            iter.setVideoId(videoId);
            try {
                scoreTempMapper.insert(iter);
            } catch (Exception e) {
                log.error("[scoreTempMapper] inserrt error",e);
            }
        }
    }

    @Override
    public Result<Void> addOnlineScore(Long minId){

        long startTime = System.currentTimeMillis();
        log.info("[addOnlineScore] start:{}",startTime);
        ScoreQuery query = new ScoreQuery();
        query.setPageEnable(false);
        query.setMinId(minId);
        List<ScoreDO> list = scoreMapper.find(query);
        Map<String,List<ScoreDO>> vMap = list.stream().collect(Collectors.groupingBy(ScoreDO::getThirdVideoId));
        vMap.forEach((K,V)-> {
            subOnine(V,K);
        });
        long endTime = System.currentTimeMillis();
        log.info("[addOnlineScore] start:{},end:{} cost:{}",startTime,endTime,endTime-startTime);
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
            Result<Void> ret = calculationFacadeService.calculationByThirdId(iter.getXmVideoId());
            if(!ret.isSuccess()){
                log.error("[calculateScore] error videoId:{}",iter.getXmVideoId());
            }
        }
        long end = System.currentTimeMillis();
        log.info("[subCalculate] finish page:{} start:{} end:{} cost:{}",j,start,end,end-start);
    }
}
