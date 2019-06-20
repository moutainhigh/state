package com.shinemo.score.core.task;


import com.shinemo.client.common.Result;
import com.shinemo.client.util.DateUtil;
import com.shinemo.client.util.EnvUtil;
import com.shinemo.my.redis.domain.LockContext;
import com.shinemo.my.redis.service.RedisLock;
import com.shinemo.score.client.comment.domain.CalculationEnum;
import com.shinemo.score.client.score.facade.CalculationFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Slf4j
public class CalculationScoreHourTask {


    private static String KEY = "migu_score_hour_lock";

    @Resource
    private CalculationFacadeService calculationFacadeService;

    @Resource
    private RedisLock redisLock;


    public void execute(){
        long beginTime = System.currentTimeMillis();
        if (EnvUtil.isPre()) {
            return;
        }
        boolean hasLock = redisLock.lock(LockContext.create(KEY, 20));
        if (!hasLock) {
            log.error("[foodTask] not has key to exx");
            return;
        }
        Date start = DateUtil.getFutureHour(new Date(),-2);
        Date end = DateUtil.getFutureHour(new Date(),-1);
        Result<Void> rs =  calculationFacadeService.calculationByTime(start,end, CalculationEnum.increment);
        if(!rs.isSuccess()){
            log.error("[calculationByTime] error result:{}",rs);
        }
        long endTime = System.currentTimeMillis();
        log.info("[CalculationScoreHourTask] start:{},end:{},cost:{}",beginTime,endTime,endTime-beginTime);
    }
}
