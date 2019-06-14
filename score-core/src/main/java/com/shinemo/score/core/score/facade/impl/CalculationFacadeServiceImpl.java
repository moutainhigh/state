package com.shinemo.score.core.score.facade.impl;

import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CalculationFacadeServiceImpl implements CalculationFacadeService {

    @Resource
    private ScoreService scoreService;

    @Resource
    private VideoService videoService;

    @Override
    public Result<Void> calculationByHours(Date startTime, Date endTime) {

        ScoreQuery query = new ScoreQuery();
        query.setStartModifyTime(startTime);
        query.setEndModifyTime(endTime);
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
        VideoQuery videoQuery = new VideoQuery();
        for(Map.Entry<Long,List<ScoreDO>> entry:map.entrySet()){
            long sumWeight = entry.getValue().size();
            long sumScore = entry.getValue().stream().mapToInt(val->val.getScore()).sum();
            videoQuery.setId(entry.getKey());
            Result<VideoDO> rz = videoService.getVideo(videoQuery);
            if(!rz.hasValue()){
                log.error("[calculationByHours] video notExist:{}",rz);
                continue;
            }
            VideoDO VideoDO = rz.getValue();
            VideoDO.setScore(VideoDO.getScore()+sumScore);
            VideoDO.setWeight(VideoDO.getWeight()+sumWeight);
            //更新video //TODO score 增加标位 增加一个差值分数  当创建时间和更新时间在同一天之内更改 直接更新差值分数
            //统计完毕之后更新差值分数
        }


        return null;
    }

    @Override
    public Result<Void> calculationByDays() {
        return null;
    }
}
