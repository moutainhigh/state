package com.shinemo.score.test.core.service;


import com.shinemo.score.core.score.service.InnerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@Slf4j
public class ScoreServiceTest {

    private static final Logger logger = LoggerFactory.getLogger("APPLICATION");
    @Resource
    private InnerService innerService;

    @Test
    public void test(){
        innerService.fixScoreNum();
    }

    @Test
    public void test1(){
        System.out.println("test_log");
        logger.info("test_log");
    }

}
