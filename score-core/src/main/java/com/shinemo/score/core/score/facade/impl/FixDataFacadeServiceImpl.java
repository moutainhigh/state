package com.shinemo.score.core.score.facade.impl;

import com.shinemo.client.common.Result;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.score.client.common.domain.DeleteStatusEnum;
import com.shinemo.score.client.score.domain.UserTmp;
import com.shinemo.score.client.score.domain.VideoTmp;
import com.shinemo.score.client.score.facade.FixDataFacadeService;
import com.shinemo.score.client.score.query.VideoTmpQuery;
import com.shinemo.score.client.video.domain.VideoDO;
import com.shinemo.score.client.video.domain.VideoFlag;
import com.shinemo.score.client.video.query.VideoQuery;
import com.shinemo.score.dal.score.mapper.VideoTmpMapper;
import com.shinemo.score.dal.video.mapper.VideoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
@Slf4j
public class FixDataFacadeServiceImpl implements FixDataFacadeService {

    @Resource
    private VideoTmpMapper videoTmpMapper;

    @Resource
    private UserTmp userTmp;

    @Resource
    private VideoMapper videoMapper;

    @Override
    public Result<Void> fixVideo(){
        long start = System.currentTimeMillis();
        log.info("[fixVideoFinish] start:{}",start);
        VideoTmpQuery query = new VideoTmpQuery();
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
            video.setInitScore(Long.parseLong(String.valueOf(initScore)));
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
            video.setInitScore(Long.parseLong(String.valueOf(initScore)));
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

    @Override
    public Result<Void> initScore() {
        return null;
    }

    @Override
    public Result<Void> addOnlineScore() {
        return null;
    }

    @Override
    public Result<Void> fixNum() {
        return null;
    }
}
