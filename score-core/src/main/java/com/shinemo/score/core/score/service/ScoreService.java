package com.shinemo.score.core.score.service;

import com.shinemo.client.common.Result;
import com.shinemo.score.client.score.domain.ScoreDO;

public interface ScoreService{


    /**
     * 插入评分
     * @param domain
     * @return
     */
    Result<ScoreDO> insertScore(ScoreDO domain);



}
