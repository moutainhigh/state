package com.shinemo.score.dal.like.wrapper;

import javax.annotation.Resource;

import com.shinemo.client.dal.mapper.Mapper;
import com.shinemo.client.dal.wrapper.Wrapper;
import com.shinemo.score.client.like.domain.LikeDO;
import com.shinemo.score.client.like.query.LikeQuery;
import com.shinemo.score.dal.like.mapper.LikeMapper;
import org.springframework.stereotype.Service;

/**
 * Wrapper
 * @ClassName: LikeWrapper
 * @author wenchao.li
 * @Date 2019-06-06 10:06:20
 */
@Service
public class LikeWrapper extends Wrapper<LikeQuery, LikeDO> {

    @Resource
    private LikeMapper likeLogMapper;

    @Override
    public Mapper<LikeQuery, LikeDO> getMapper() {
        return likeLogMapper;
    }
}