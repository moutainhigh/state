package com.shinemo.score.test.dal.user.wrapper;

import com.shinemo.score.client.user.query.UserInfoQuery;
import com.shinemo.score.dal.user.mapper.UserInfoMapper;
import com.shinemo.score.dal.user.wrapper.UserInfoWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * UserInfoWrapper 测试类
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
@ComponentScan(basePackages = {
        "com.shinemo.score.dal.user.wrapper",
})
@TestPropertySource(properties = {
        "spring.config.location=classpath:conf/application.yaml",
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserInfoWrapperTest {
    @Resource
    private UserInfoWrapper userInfoWrapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 测试获取用户姓名
     *
     * @return void
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Test
    public void findUserInfoOrderByName() {
        Integer pageSize = null;
        Integer currentPage = null;
        Boolean asc = null;
        System.out.printf("pageSize=%s, currentPage=%s, asc=%s, result=%s%n",
                pageSize,
                currentPage,
                asc,
                userInfoWrapper.findUserInfoOrderByName(pageSize, currentPage, asc)
        );
    }

    /**
     * 测试随机更新数据的事务回滚
     *
     * @return void
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Test
    public void randomUpdateUsers() {
        UserInfoQuery query = new UserInfoQuery();
        String before = String.format("param=%s, result=%s%n", query, userInfoMapper.find(query));
        System.out.printf(before);
        Integer pageSize = null;
        Integer currentPage = null;
        Boolean asc = null;
        try {
            // 测试事务请设置 @Transactional(propagation = Propagation.REQUIRES_NEW)，因为运行测试方法默认会开启事务
            System.out.printf("pageSize=%s, currentPage=%s, asc=%s, result=%s%n",
                    pageSize,
                    currentPage,
                    asc,
                    userInfoWrapper.randomUpdateUsers(pageSize, currentPage, asc)
            );
        } catch (Exception e){
            e.printStackTrace();
        }
        String after = String.format("param=%s, result=%s%n", query, userInfoMapper.find(query));
        System.out.println(after.equals(before));
    }
}
