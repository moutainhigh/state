package com.shinemo.score.core.user.facade.impl;

import com.shinemo.score.client.user.facade.UserInfoFacadeService;
import com.shinemo.score.core.user.service.UserInfoService;
import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户信息 facade 实现
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@Service("userInfoFacadeService")
public class UserInfoFacadeServiceImpl implements UserInfoFacadeService {
    @Resource
    private UserInfoService userInfoService;

    /**
     * 获取用户姓名，根据名称降序排序
     *
     * @param pageSize
     * @param currentPage
     * @return com.shinemo.client.common.Result&lt;com.shinemo.client.common.ListVO&lt;String&gt;&gt;
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Override
    public Result<ListVO<String>> findUserNameByDesc(Integer pageSize, Integer currentPage) {
        return userInfoService.findUserNameByDesc(pageSize, currentPage);
    }
}
