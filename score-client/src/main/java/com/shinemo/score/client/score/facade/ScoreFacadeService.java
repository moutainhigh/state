package com.shinemo.score.client.score.facade;


import com.shinemo.client.common.WebResult;
import com.shinemo.score.client.score.domain.MyScoreDTO;
import com.shinemo.score.client.score.domain.MyScoreRequest;
import com.shinemo.score.client.score.domain.ScoreRequest;
import com.shinemo.score.client.video.domain.VideoDO;
import com.shinemo.score.client.video.domain.VideoDTO;

public interface ScoreFacadeService{

    /**
     * 提交评分
     * @param request
     * @return
     */
    WebResult<Void> submitScore(ScoreRequest request);

    /**
     * 获取我的评分
     * @param request
     * @return
     */
    WebResult<MyScoreDTO> getMyScore(MyScoreRequest request);

    /**
     * 获取电影评分
     * @param request
     * @return
     */
    WebResult<VideoDTO> getVideoScore(MyScoreRequest request);



}
