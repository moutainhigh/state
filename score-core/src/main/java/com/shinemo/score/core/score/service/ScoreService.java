package com.shinemo.score.core.score.service;

import com.shinemo.client.common.Result;
import com.shinemo.score.client.score.domain.ScoreDO;
import com.shinemo.score.client.score.query.ScoreQuery;

public interface ScoreService{


    /**
     * 插入评分
     * @param domain
     * @return
     */
    Result<ScoreDO> insertScore(ScoreDO domain);

    /**
     * 查询单个电影评分
     * @param query
     * @return
     */
    Result<ScoreDO> getScore(ScoreQuery query);



}
