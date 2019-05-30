package com.shinemo.score.client.user.query;

import com.shinemo.client.common.QueryBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息查询对象
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoQuery extends QueryBase {
    private Long id;
    private String name;
    private Integer age;
}
