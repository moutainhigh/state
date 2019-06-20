package com.shinemo.score.core.score.service.impl;

import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.score.client.score.domain.ScoreDO;
import com.shinemo.score.client.score.query.ScoreQuery;
import com.shinemo.score.core.score.service.InnerService;
import com.shinemo.score.dal.score.wrapper.ScoreWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InnerServiceImpl implements InnerService{

    @Resource
    private ScoreWrapper scoreWrapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fixScoreNum(){
        log.info("[fixScoreNum] start");
        long startTime = System.currentTimeMillis();
        ScoreQuery query = new ScoreQuery();
        query.setPageEnable(false);
        Result<ListVO<ScoreDO>> rs = scoreWrapper.find(query);
        List<ScoreDO> list = rs.getValue().getRows();
        Map<Long,List<ScoreDO>> uidMap = list.stream().collect(Collectors.groupingBy(ScoreDO::getUid));
        for(Map.Entry<Long,List<ScoreDO>> entry:uidMap.entrySet()){
            List<ScoreDO> subList = entry.getValue();
            for(int i=0;i<subList.size();i++){
                long num = i+1;
                ScoreDO scoreDO = new ScoreDO();
                scoreDO.setId(subList.get(i).getId());
                scoreDO.setNum(num);
                scoreDO.setVersion(subList.get(i).getVersion());
                Result<ScoreDO> upt = scoreWrapper.update(subList.get(i));
                if(!upt.hasValue()){
                    log.error("[update] error:{}",upt);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("[fixScoreNum] start:{},end:{} cost:{}",startTime,endTime,endTime-startTime);
    }
}
