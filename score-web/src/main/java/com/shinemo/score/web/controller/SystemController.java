package com.shinemo.score.web.controller;

import com.shinemo.jce.core.AaceServer;
import com.shinemo.jce.util.Container;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统操作 controller
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@RestController
public class SystemController {

    /**
     * 检查工程启动状态
     *
     * @return java.lang.String
     * @author zhangyan
     * @date 2018-05-29
     **/
    @GetMapping(value = "/checkstatus")
    public String check() {
        return "success\n";
    }

    /**
     * shutdown jce
     *
     * @return java.lang.String
     * @author zhangyan
     * @date 2018-05-29
     **/
    @GetMapping(value = "/shutdown")
    public String shutdown() {
        AaceServer aaceServer = Container.get(AaceServer.class);
        aaceServer.shutdown();
        return "success\n";
    }

}
