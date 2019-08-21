package com.shinemo.score.client.score.facade;

import com.shinemo.client.common.Result;
import com.shinemo.score.client.comment.domain.CalculationEnum;

import java.util.Date;

public interface CalculationFacadeService{


    Result<Void> calculationByTime(Date startTime, Date endTime,CalculationEnum calculationEnum,Long videoId,String thirdVideoId);





}
