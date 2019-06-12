package com.shinemo.score.test.dal.user.mapper;


import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * UserInfoMapper 测试类
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@Commit
@MybatisTest
@RunWith(SpringRunner.class)
@MapperScan(basePackages = {
        "com.shinemo.score.dal.user.mapper",
})
@TestPropertySource(properties = {
        "spring.config.location=classpath:conf/application.yaml",
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserInfoMapperTest {


}
