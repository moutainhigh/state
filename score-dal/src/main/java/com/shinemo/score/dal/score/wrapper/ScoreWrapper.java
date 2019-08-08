package com.shinemo.score.dal.score.wrapper;

import javax.annotation.Resource;

import com.shinemo.client.common.Errors;
import com.shinemo.client.common.Result;
import com.shinemo.score.client.error.ScoreErrors;
import com.shinemo.score.client.score.domain.ScoreCountDO;
import com.shinemo.score.client.score.domain.ScoreDO;
import com.shinemo.score.client.score.query.ScoreQuery;
import com.shinemo.score.dal.score.mapper.ScoreMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import com.shinemo.client.dal.mapper.Mapper;
import com.shinemo.client.dal.wrapper.Wrapper;
import org.springframework.util.Assert;

import java.util.List;


/**
 * Wrapper
 *
 * @author zhangyan
 * @ClassName: ScoreWrapper
 * @Date 2019-06-12 09:52:15
 */
@Service
@Slf4j
public class ScoreWrapper extends Wrapper<ScoreQuery, ScoreDO> {

    @Resource
    private ScoreMapper scoreMapper;

    @Override
    public Mapper<ScoreQuery, ScoreDO> getMapper() {
        return scoreMapper;
    }

    public Result<List<ScoreCountDO>> getScoreCounts(List<Long> list) {
        Assert.isTrue(!CollectionUtils.isEmpty(list), "list is null");
        try {
            List<ScoreCountDO> ret = scoreMapper.getScoreCounts(list);
            return Result.success(ret);
        } catch (Exception e) {
            log.error("[getScoreCounts] error", e);
            return Result.error(ScoreErrors.SQL_ERROR_COUNT);
        }
    }

    public Result<ScoreDO> getScoreByMaxNum(ScoreQuery query) {

        ScoreDO scoreDO = scoreMapper.getScoreByMaxNum(query);
        if (scoreDO == null) {
            return Result.error(Errors.DATA_NOT_EXIST);
        }
        return Result.success(scoreDO);
    }
}