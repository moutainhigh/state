package com.shinemo.score.core.score.facade.impl;

import com.shinemo.client.common.Result;
import com.shinemo.score.client.score.facade.CalculationFacadeService;
import com.shinemo.score.core.score.service.ScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
@Slf4j
public class CalculationFacadeServiceImpl implements CalculationFacadeService {

    @Resource
    private ScoreService scoreService;

    @Override
    public Result<Void> calculationByHours() {
        return null;
    }

    @Override
    public Result<Void> calculationByDays() {
        return null;
    }
}
