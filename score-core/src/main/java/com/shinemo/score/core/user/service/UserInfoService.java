package com.shinemo.score.core.user.service;

import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;

/**
 * 用户信息 service
 *
 * @author zhangyan
 * @date 2018-05-29
 */
public interface UserInfoService {
    /**
     * 获取用户姓名，根据名称降序排序
     *
     * @param pageSize
     * @param currentPage
     * @return com.shinemo.client.common.Result&lt;com.shinemo.client.common.ListVO&lt;String&gt;&gt;
     * @author zhangyan
     * @date 2018-05-29
     **/
    Result<ListVO<String>> findUserNameByDesc(Integer pageSize, Integer currentPage);
}
