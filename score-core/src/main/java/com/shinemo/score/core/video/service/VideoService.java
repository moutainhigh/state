package com.shinemo.score.core.video.service;

import com.shinemo.client.common.Result;
import com.shinemo.score.client.score.domain.ScoreRequest;
import com.shinemo.score.client.video.domain.VideoDO;

public interface VideoService {

    /**
     * 初始化电影
     * @param request
     * @return
     */
    Result<VideoDO> initVideo(ScoreRequest request);

}
