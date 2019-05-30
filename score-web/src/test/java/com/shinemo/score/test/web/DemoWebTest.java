package com.shinemo.score.test.web;

import com.shinemo.score.web.application.Application;
import com.shinemo.score.web.controller.SystemController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * UserInfoController 测试类
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "app_env=-1",
                "app_name=score",
                "config_url=http://10.0.10.62/myconf/conf/dispatch",
        },
        classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
public class DemoWebTest {

    @Resource
    private SystemController systemController;

    /**
     * 测试是否注入对象
     *
     * @return void
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Test
    public void test() {
        System.out.println(systemController);
    }
}
