package com.shinemo.score.core.score.facade.impl;

import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.score.client.comment.domain.CalculationEnum;
import com.shinemo.score.client.score.domain.ScoreCountDO;
import com.shinemo.score.client.score.domain.ScoreDO;
import com.shinemo.score.client.score.facade.CalculationFacadeService;
import com.shinemo.score.client.score.query.ScoreQuery;
import com.shinemo.score.client.video.domain.VideoDO;
import com.shinemo.score.client.video.domain.VideoFlag;
import com.shinemo.score.client.video.query.VideoQuery;
import com.shinemo.score.core.score.service.ScoreService;
import com.shinemo.score.core.video.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CalculationFacadeServiceImpl implements CalculationFacadeService {

    @Resource
    private ScoreService scoreService;

    @Resource
    private VideoService videoService;

    @Override
    public Result<Void> calculationByTime(Date startTime, Date endTime,CalculationEnum calculationEnum,Long id) {

        ScoreQuery query = new ScoreQuery();
        query.setStartModifyTime(startTime);
        query.setEndModifyTime(endTime);
        query.setVideoId(id);
        Result<ListVO<ScoreDO>> rs = scoreService.findScores(query);
        if(!rs.hasValue() ){
            log.error("[calculationByHours]  findScores result:{}",rs);
            return Result.error(rs.getError());
        }
        if(CollectionUtils.isEmpty(rs.getValue().getRows())){
            log.info("[calculationByHours]  empty startTime:{},endTime:{}",startTime,endTime);
            return Result.success();
        }
        Map<Long,List<ScoreDO>> map =  rs.getValue().getRows().stream().collect(Collectors.groupingBy(ScoreDO::getVideoId));
        Map<Long,ScoreCountDO> countMap = new HashMap<>();
        if(CalculationEnum.all == calculationEnum){//全量更新
            List<Long> ids = new ArrayList<>(map.keySet());
            Result<List<ScoreCountDO>> countRs = scoreService.getScoreCounts(ids);
            if(countRs.hasValue()){
                countMap = countRs.getValue().stream().collect(Collectors.toMap(val->val.getVideoId(),val->val));
            }
        }
        VideoQuery videoQuery = new VideoQuery();
        for(Map.Entry<Long,List<ScoreDO>> entry:map.entrySet()){
            Long videoId = entry.getKey();
            videoQuery.setId(videoId);
            Result<VideoDO> rz = videoService.getVideo(videoQuery);
            VideoDO videoDO = rz.getValue();
            if(!rz.hasValue()){
                log.error("[calculationByHours] video notExist:{}",rz);
                continue;
            }
            if(CalculationEnum.increment == calculationEnum){//增量更新
                long sumWeight = entry.getValue().size();
                long sumScore = entry.getValue().stream().mapToInt(val->val.getScore()).sum();
                videoDO.setScore(videoDO.getScore()+sumScore);
                videoDO.setWeight(videoDO.getWeight()+sumWeight);
            }else{//全量
                ScoreCountDO dto = countMap.get(videoId);
                if(dto == null){
                    log.error("[calculationByHours] ScoreCountDO null id:{}",videoId);
                    continue;
                }
                videoDO.setYesterdayScore(videoDO.getScore());
                videoDO.setYesterdayWeight(videoDO.getWeight());
                videoDO.setScore(videoDO.getInitScore()+dto.getScore());
                videoDO.setWeight(videoDO.getInitWeight()+dto.getNum());
            }
            Result<VideoDO> uptRs = videoService.updateVideoScore(videoDO);
            if(!uptRs.hasValue()){
                log.error("[updateVideoScore] upt result:{}",uptRs);
            }
        }
        return Result.success();
    }

}
