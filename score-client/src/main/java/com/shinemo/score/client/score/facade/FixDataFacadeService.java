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

    Result<Void> addOnlineScore();

    Result<Void> fixNum();
}
