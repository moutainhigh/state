package com.shinemo.score.client.score.facade;

import com.shinemo.client.common.Result;

import java.util.Date;

public interface CalculationFacadeService{


    Result<Void> calculationByHours(Date startTime, Date endTime);


    Result<Void> calculationByDays();


}
