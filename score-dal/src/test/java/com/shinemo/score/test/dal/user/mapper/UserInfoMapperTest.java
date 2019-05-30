package com.shinemo.score.test.dal.user.mapper;

import com.shinemo.score.client.user.domain.UserInfoDO;
import com.shinemo.score.client.user.query.UserInfoQuery;
import com.shinemo.score.dal.user.mapper.UserInfoMapper;
import org.junit.Assert;
import org.junit.Test;
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

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 测试统计数量
     *
     * @return void
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Test
    public void count() {
        UserInfoQuery query = new UserInfoQuery();
        System.out.printf("param=%s, result=%s%n", query, userInfoMapper.count(query));
    }

    /**
     * 测试获取列表
     *
     * @return void
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Test
    public void find() {
        UserInfoQuery query = new UserInfoQuery();
        System.out.printf("param=%s, result=%s%n", query, userInfoMapper.find(query));
    }

    /**
     * 测试获取单条记录
     *
     * @return void
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Test
    public void get() {
        UserInfoQuery query = new UserInfoQuery();
        List<UserInfoDO> list = userInfoMapper.find(query);
        Assert.assertNotNull("list == null", list);
        Assert.assertFalse(list.isEmpty());
        query = UserInfoQuery.builder().id(list.get(list.size() - 1).getId()).build();
        System.out.printf("param=%s, result=%s%n", query, userInfoMapper.find(query));
    }

    /**
     * 测试插入
     *
     * @return void
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Test
    public void insert() {
        UserInfoDO value = UserInfoDO.builder().name(UUID.randomUUID().toString()).age(new Random().nextInt(100)).build();
        System.out.printf("param=%s, result=%s%n", value, userInfoMapper.insert(value));
    }

    /**
     * 测试修改
     *
     * @return void
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Test
    public void update() {
        UserInfoQuery query = new UserInfoQuery();
        List<UserInfoDO> list = userInfoMapper.find(query);
        Assert.assertNotNull("list == null", list);
        Assert.assertFalse(list.isEmpty());
        UserInfoDO value = UserInfoDO.builder().id(list.get(list.size() - 1).getId()).age(new Random().nextInt(100)).build();
        System.out.printf("param=%s, result=%s%n", value, userInfoMapper.update(value));
    }
}
