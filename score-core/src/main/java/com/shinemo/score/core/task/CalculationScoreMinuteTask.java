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

@Slf4j
@Service
public class CalculationScoreMinuteTask {

    private static String KEY = "migu_score_minute_lock";

    @Resource
    private CalculationFacadeService calculationFacadeService;

    @Resource
    private RedisLock redisLock;


    public void execute(){
        Date startDate = new Date();
        log.info("[CalculationScoreDayTask] start:{}",startDate);
        if (EnvUtil.isPre()) {
            return;
        }
        boolean hasLock = redisLock.lock(LockContext.create(KEY, 20));
        if (!hasLock) {
            log.error("[foodTask] not has key to exx");
            return;
        }
        Date start = DateUtil.getFutureMinute(startDate,-10);
        Date end = DateUtil.getFutureMinute(startDate,-5);
        Result<Void> rs =  calculationFacadeService.calculationByTime(start,end, CalculationEnum.all,null,null);
        if(!rs.isSuccess()){
            log.error("[calculationByTime] error result:{}",rs);
        }
        long endTime = System.currentTimeMillis();
        log.info("[CalculationScoreDayTask] start:{},end:{},cost:{}",startDate.getTime(),endTime,endTime-startDate.getTime());
    }
}
