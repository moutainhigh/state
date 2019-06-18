package com.shinemo.score.core.score.service;

import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.score.client.score.domain.ScoreCountDO;
import com.shinemo.score.client.score.domain.ScoreDO;
import com.shinemo.score.client.score.query.ScoreQuery;

import java.util.List;

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

    /**
     * 查询电影评分列表
     * @param query
     * @return
     */
    Result<ListVO<ScoreDO>> findScores(ScoreQuery query);
    /**
     * 得到电影评分
     * @param
     * @return
     */
    Result<List<ScoreCountDO>> getScoreCounts(List<Long> videoIds);



}
