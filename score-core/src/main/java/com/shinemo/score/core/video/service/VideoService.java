package com.shinemo.score.core.video.service;

import com.shinemo.client.common.Result;
import com.shinemo.score.client.score.domain.ScoreRequest;
import com.shinemo.score.client.video.domain.VideoDO;
import com.shinemo.score.client.video.domain.VideoFlag;
import com.shinemo.score.client.video.query.VideoQuery;

public interface VideoService {

    /**
     * 初始化电影
     * @param request
     * @return
     */
    Result<VideoDO> initVideo(ScoreRequest request);


    Result<VideoDO> getVideo(VideoQuery query);

    /**
     * 更新视频分数
     * @param domain
     * @return
     */
    Result<VideoDO> updateVideoScore(VideoDO domain);

}
