package com.shinemo.score.client.score.facade;

import com.shinemo.client.common.Result;

public interface FixDataFacadeService{
    /**
     * 修正音频初始化信息
     * @return
     */
    Result<Void> fixVideo();
    /**
     * 根据表格插入初始化用户评分
     * @return
     */
    Result<Void> initScore();
    /**
     * 添加上线之后的用户评分
     * @return
     */
    Result<Void> addOnlineScore(Long minId);
    /**
     *重新全量计算一次评分
     * @return
     */
    Result<Void> calculateScore();
}
