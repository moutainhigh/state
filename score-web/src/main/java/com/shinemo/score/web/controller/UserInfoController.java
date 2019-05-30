package com.shinemo.score.web.controller;

import com.shinemo.score.client.user.facade.UserInfoFacadeService;
import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 用户信息 controller
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Resource
    private UserInfoFacadeService userInfoFacadeService;

    /**
     * 获取用户名称列表
     *
     * @param pageSize
     * @param currentPage
     * @return Result&lt;ListVO&lt;String&gt;&gt;
     * @author zhangyan
     * @date 2018-05-29
     **/
    @GetMapping(value = "/findUserNameByDesc")
    public Result<ListVO<String>> findUserNameByDesc(
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "currentPage", required = false) Integer currentPage) {
        return userInfoFacadeService.findUserNameByDesc(pageSize, currentPage);
    }

    /**
     * 服务器时间
     *
     * @return java.util.Date
     * @author zhangyan
     * @date 2018-05-29
     **/
    @GetMapping(value = "/server/time")
    public Date serverTime() {
        return new Date();
    }
}
