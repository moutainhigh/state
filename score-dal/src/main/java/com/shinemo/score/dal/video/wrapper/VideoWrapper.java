package com.shinemo.score.dal.video.wrapper;

import javax.annotation.Resource;

import com.shinemo.score.client.video.domain.VideoDO;
import com.shinemo.score.client.video.query.VideoQuery;
import com.shinemo.score.dal.video.mapper.VideoMapper;
import org.springframework.stereotype.Service;
import com.shinemo.client.dal.mapper.Mapper;
import com.shinemo.client.dal.wrapper.Wrapper;


/**
 * Wrapper
 * @ClassName: VideoWrapper
 * @author zhangyan
 * @Date 2019-06-12 09:51:48
 */
@Service
public class VideoWrapper extends Wrapper<VideoQuery, VideoDO> {

    @Resource
    private VideoMapper videoMapper;

    @Override
    public Mapper<VideoQuery, VideoDO> getMapper() {
        return videoMapper;
    }
}