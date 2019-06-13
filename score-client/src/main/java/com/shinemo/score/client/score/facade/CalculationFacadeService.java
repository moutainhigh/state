package com.shinemo.score.client.score.facade;

import com.shinemo.client.common.Result;

public interface CalculationFacadeService{


    Result<Void> calculationByHours();


    Result<Void> calculationByDays();


}
