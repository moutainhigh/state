package com.shinemo.score.client.score.facade;


import com.shinemo.client.common.WebResult;
import com.shinemo.score.client.score.domain.ScoreRequest;

public interface ScoreFacadeService{

    /**
     * 提交评分
     * @param request
     * @return
     */
    WebResult<Void> submitScore(ScoreRequest request);



}
