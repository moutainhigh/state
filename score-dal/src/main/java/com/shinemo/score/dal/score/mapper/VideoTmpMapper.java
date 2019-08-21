package com.shinemo.score.dal.score.mapper;

import com.shinemo.client.dal.mapper.Mapper;
import com.shinemo.score.client.score.domain.VideoTmp;
import com.shinemo.score.client.score.query.VideoTmpQuery;
import org.springframework.stereotype.Repository;


/**
 * Mapper
 * @ClassName: VideoTmpMapper
 * @author zhangyan
 * @Date 2019-08-20 21:33:29
 */
@Repository
public interface VideoTmpMapper extends Mapper<VideoTmpQuery, VideoTmp> {
}