package com.shinemo.score.core.video.service.impl;

import com.shinemo.client.common.FlagHelper;
import com.shinemo.client.common.Result;
import com.shinemo.score.client.common.domain.DeleteStatusEnum;
import com.shinemo.score.client.score.domain.ScoreRequest;
import com.shinemo.score.client.video.domain.VideoDO;
import com.shinemo.score.client.video.domain.VideoFlag;
import com.shinemo.score.client.video.query.VideoQuery;
import com.shinemo.score.core.video.service.VideoService;
import com.shinemo.score.dal.configuration.ShineMoProperties;
import com.shinemo.score.dal.video.wrapper.VideoWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
@Slf4j
public class VideoServiceImpl implements VideoService{

    @Resource
    private VideoWrapper videoWrapper;

    @Resource
    private ShineMoProperties shineMoProperties;

    @Override
    public Result<VideoDO> initVideo(ScoreRequest request){

        VideoQuery query = new VideoQuery();
        query.setVideoId(request.getVideoId());
        Result<VideoDO> rs = videoWrapper.get(query);
        if(rs.hasValue()){
            return rs;
        }
        VideoDO videoDO = initVideoDO(request);
        return videoWrapper.insert(videoDO);//TODO 考虑并发
    }

    private VideoDO initVideoDO(ScoreRequest request) {
        VideoDO videoDO = new VideoDO();
        videoDO.setExtend(request.getExtend());
        videoDO.setStatus(DeleteStatusEnum.NORMAL.getId());
        videoDO.setVideoName(request.getVideoName());
        videoDO.setVersion(1L);
        videoDO.setVideoId(request.getVideoId());
        if(FlagHelper.hasFlag(request.getFlag(), VideoFlag.GRADE)){
            videoDO.addVideoFlag(VideoFlag.GRADE);
            long score = shineMoProperties.getInitScore() * shineMoProperties.getInitWeight();
            videoDO.setInitScore(score);
            videoDO.setInitWeight(shineMoProperties.getInitWeight());
            videoDO.setWeight(shineMoProperties.getInitWeight());
            videoDO.setScore(score);
            videoDO.setYesterdayScore(score);
            videoDO.setYesterdayWeight(shineMoProperties.getInitWeight());
        }
        return videoDO;
    }
}
