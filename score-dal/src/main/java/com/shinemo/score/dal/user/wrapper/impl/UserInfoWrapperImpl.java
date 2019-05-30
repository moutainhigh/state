package com.shinemo.score.dal.user.wrapper.impl;

import com.shinemo.score.client.user.domain.UserInfoDO;
import com.shinemo.score.client.user.query.UserInfoQuery;
import com.shinemo.score.dal.user.mapper.UserInfoMapper;
import com.shinemo.score.dal.user.wrapper.UserInfoWrapper;
import com.shinemo.client.common.ErrorInfo;
import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.client.dal.mapper.Mapper;
import com.shinemo.client.dal.wrapper.Wrapper;
import com.shinemo.client.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * 用户信息 wrapper 实现类
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@Slf4j
@Service("userInfoWrapper")
public class UserInfoWrapperImpl extends Wrapper<UserInfoQuery, UserInfoDO> implements UserInfoWrapper {
    @Resource
    private UserInfoMapper userInfoMapper;

    protected Mapper<UserInfoQuery, UserInfoDO> getMapper() {
        return userInfoMapper;
    }

    /**
     * 获取用户姓名，根据名称排序
     *
     * @param pageSize
     * @param currentPage
     * @param asc
     * @return com.shinemo.client.common.Result&lt;com.shinemo.client.common.ListVO&lt;String&gt;&gt;
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Override
    public Result<ListVO<UserInfoDO>> findUserInfoOrderByName(Integer pageSize, Integer currentPage, Boolean asc) {
        UserInfoQuery query = new UserInfoQuery();
        query.setPageSize(pageSize);
        query.setCurrentPage(currentPage);
        query.putOrderBy("name", asc);
        return find(query);
    }

    /**
     * 随机更新用户，当更新用户之后抛出异常，事物回滚
     *
     * @param pageSize
     * @param currentPage
     * @param asc
     * @return com.shinemo.client.common.Result&lt;Integer&gt;
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> randomUpdateUsers(Integer pageSize, Integer currentPage, Boolean asc) {
        Result<ListVO<UserInfoDO>> usersResult = findUserInfoOrderByName(pageSize, currentPage, asc);
        if (!usersResult.hasValue()) {
            log.error("[randomUpdateUsers] findUserInfoOrderByName has error, param:{},{},{}, result:{}", pageSize, currentPage, asc, usersResult);
            return Result.error(usersResult);
        }
        List<UserInfoDO> list = usersResult.getValue().getRows();
        Random random = new Random();
        int age = 0;
        int updateCount = 0;
        int updateSuccessCount = 0;
        for (UserInfoDO userInfoDO : list) {
            age = random.nextInt(20) + 10;
            if (age > 20) {
                userInfoDO.setAge(age);
                Result<UserInfoDO> userResult = update(userInfoDO);
                if (userResult.isSuccess()) {
                    updateCount++;
                    if (!userResult.isEmpty()) {
                        updateSuccessCount++;
                    }
                }
            }
        }
        log.info("test transactional: updateCount:{}, updateSuccessCount:{}", updateCount, updateSuccessCount);
        if(updateSuccessCount > 0){
            throw new BizException(new ErrorInfo(0, "TEST_TRANSACTION", "test transaction"));
        }
        return Result.success(updateSuccessCount);
    }
}
