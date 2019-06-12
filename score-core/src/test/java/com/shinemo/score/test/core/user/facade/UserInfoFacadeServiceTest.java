package com.shinemo.score.test.core.user.facade;


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
 * UserInfoFacadeService 测试类
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
        "com.shinemo.score.core.user.facade",
})
@TestPropertySource(properties = {
        "spring.config.location=classpath:conf/application.yaml",
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserInfoFacadeServiceTest {


}
