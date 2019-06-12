package com.shinemo.score.dal.score.wrapper;

import javax.annotation.Resource;

import com.shinemo.score.client.score.domain.ScoreDO;
import com.shinemo.score.client.score.query.ScoreQuery;
import com.shinemo.score.dal.score.mapper.ScoreMapper;
import org.springframework.stereotype.Service;
import com.shinemo.client.dal.mapper.Mapper;
import com.shinemo.client.dal.wrapper.Wrapper;


/**
 * Wrapper
 * @ClassName: ScoreWrapper
 * @author zhangyan
 * @Date 2019-06-12 09:52:15
 */
@Service
public class ScoreWrapper extends Wrapper<ScoreQuery, ScoreDO> {

    @Resource
    private ScoreMapper scoreMapper;

    @Override
    public Mapper<ScoreQuery, ScoreDO> getMapper() {
        return scoreMapper;
    }
}