package com.shinemo.score.client.score.facade;

import com.shinemo.client.common.Result;
import com.shinemo.score.client.comment.domain.CalculationEnum;

import java.util.Date;

public interface CalculationFacadeService{


    /**
     * 定时任务调用
     * @param startTime
     * @param endTime
     * @param calculationEnum
     * @return
     */
    Result<Void> calculationByTime(Date startTime, Date endTime,CalculationEnum calculationEnum);
    /**
     * 三方id 更新全量评分
     * @param thirdVideoId
     * @return
     */
    Result<Void> calculationByThirdId(String thirdVideoId);


}
