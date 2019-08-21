package com.shinemo.score.core.score.facade.impl;

import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.client.util.GsonUtil;
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
import com.shinemo.score.dal.score.mapper.ScoreTempMapper;
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

    @Resource
    private ScoreTempMapper scoreTempMapper;

    @Override
    public Result<Void> calculationByTime(Date startTime, Date endTime,CalculationEnum calculationEnum) {

        ScoreQuery query = new ScoreQuery();
        query.setStartModifyTime(startTime);
        query.setEndModifyTime(endTime);
        query.setPageEnable(false);
        Result<ListVO<ScoreDO>> rs = scoreService.findScores(query);//全量找到昨日所有电影分组取电影id,增量直接根据这次结果计算
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
        if(CalculationEnum.all == calculationEnum){//全量更新 TODO 如果id数组非常大 是否多线程处理更好
            ScoreQuery countQuery = new ScoreQuery();
            List<Long> ids = new ArrayList<>(map.keySet());
            if(ids.size()>100){
                log.error("[calculation] error needChange idSize:{}",ids.size());
            }
            countQuery.setPageEnable(false);
            countQuery.setVideoIds(ids);
            Result<ListVO<ScoreDO>> countRs = scoreService.findScores(countQuery);
            if(countRs.hasValue() && !CollectionUtils.isEmpty(countRs.getValue().getRows())){
                Map<Long,List<ScoreDO>> subMap =  countRs.getValue().getRows().stream().collect(Collectors.groupingBy(ScoreDO::getVideoId));
                for(Map.Entry<Long,List<ScoreDO>> entry:subMap.entrySet()){
                    countMap.put(entry.getKey(),initCountDO(entry.getValue()));
                }
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



    @Override
    public Result<Void> calculationByThirdId(Long videoId,String thirdVideoId) {
        ScoreQuery query = new ScoreQuery();
        query.setVideoId(videoId);
        query.setThirdVideoId(thirdVideoId);
        query.setPageEnable(false);
        Result<ListVO<ScoreDO>> rs = scoreService.findScores(query);
        if(rs.hasValue() && !CollectionUtils.isEmpty(rs.getValue().getRows())){
            ScoreCountDO count = initCountDO(rs.getValue().getRows());
            VideoQuery videoQuery = new VideoQuery();
            videoQuery.setId(rs.getValue().getRows().get(0).getVideoId());
            Result<VideoDO> rz = videoService.getVideo(videoQuery);
            if(!rz.hasValue()){
                log.error("[getVideo] error id:{} result:{}",videoQuery.getVideoId(),rz);
                return Result.error(rz.getError());
            }
            VideoDO videoDO = rz.getValue();
            videoDO.setYesterdayScore(videoDO.getScore());
            videoDO.setYesterdayWeight(videoDO.getWeight());
            videoDO.setScore(videoDO.getInitScore()+count.getScore());
            videoDO.setWeight(videoDO.getInitWeight()+count.getNum());
            Result<VideoDO> uptRs = videoService.updateVideoScore(videoDO);
            if(uptRs.hasValue()){
                log.error("[updateVideoScore] error param:{} result:{}", GsonUtil.toJson(videoDO),uptRs);
                return Result.error(uptRs.getError());
            }
        }
        return Result.success();
    }

    private ScoreCountDO initCountDO(List<ScoreDO> value) {
        ScoreCountDO domain = new ScoreCountDO();
        domain.setNum((long) value.size());
        domain.setScore(value.stream().mapToLong(val->val.getScore()).sum());
        return domain;
    }

}
