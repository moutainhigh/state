package com.shinemo.score.test.core.user.service;

import com.shinemo.score.core.user.service.UserInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * UserInfoService 测试类
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@MybatisTest
@RunWith(SpringRunner.class)
@MapperScan(basePackages = {
        "com.shinemo.score.dal.user.mapper",
})
@ComponentScan(basePackages = {
        "com.shinemo.score.dal.user.wrapper",
        "com.shinemo.score.core.user.service",
})
@TestPropertySource(properties = {
        "spring.config.location=classpath:conf/application.yaml",
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserInfoServiceTest {

    @Resource
    private UserInfoService userInfoService;

    /**
     * 测试是否注入对象
     *
     * @return void
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Test
    public void test(){
        System.out.println(userInfoService);
    }
}
