package com.shinemo.score.dal.score.mapper;

import com.shinemo.client.dal.mapper.Mapper;
import com.shinemo.score.client.score.domain.ScoreCountDO;
import com.shinemo.score.client.score.domain.ScoreDO;
import com.shinemo.score.client.score.query.ScoreQuery;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Mapper
 *
 * @author zhangyan
 * @ClassName: ScoreMapper
 * @Date 2019-06-12 09:52:15
 */
@Repository
public interface ScoreMapper extends Mapper<ScoreQuery, ScoreDO> {


    List<ScoreCountDO> getScoreCounts(List<Long> list);

    ScoreDO getScoreByMaxNum(ScoreQuery query);
}