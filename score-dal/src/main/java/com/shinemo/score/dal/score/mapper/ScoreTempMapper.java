package com.shinemo.score.dal.score.mapper;

import com.shinemo.client.dal.mapper.Mapper;
import com.shinemo.score.client.score.domain.ScoreCountDO;
import com.shinemo.score.client.score.domain.ScoreDO;
import com.shinemo.score.client.score.query.ScoreQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreTempMapper extends Mapper<ScoreQuery, ScoreDO> {



}