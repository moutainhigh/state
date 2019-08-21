package com.shinemo.score.dal.score.mapper;

import com.shinemo.client.dal.mapper.Mapper;
import com.shinemo.score.client.score.domain.UserTmp;
import com.shinemo.score.client.score.query.UserTmpQuery;
import org.springframework.stereotype.Repository;


/**
 * Mapper
 * @ClassName: UserTmpMapper
 * @author zhangyan
 * @Date 2019-08-21 11:50:36
 */
@Repository
public interface UserTmpMapper extends Mapper<UserTmpQuery, UserTmp> {
}