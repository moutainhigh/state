# 咪咕评分系统http接口

* 所有接口访问的Content-Type:application/json


## 1. 用户登录
* 接口名称： 支付接口
* 接口地址： `ygw/muic/login`
* 请求方式： `post`
* 请求说明
````
{
    "mToken":"gjwogjwiejgiwjeigjw"//解析如下
}
````

* mToken解析
````
{
    "mobile":"18390236566",
    "thirdUid":"abc",
    "userName":"张三",
    "userPortrait":"http://*.img",//用户头像
    "gender":1//1-男 2-女
    "deviceId":"abcd",//设备id
    "clientVersion":"version_1",//客户端版本号
    "clientIp":"10.0.14.45"//客户端ip
    "extend":"agwgwe",//扩展信息
}
````

* 返回结果：
* 成功示例：
````

{
    code: 0,
    success：true
}
````



## 2. 用户评分
* 接口名称： 支付接口
* 接口地址： `ygw/score/submit`
* 请求方式： `post`
* 请求说明

````
{
    "videoId":12345,//外部电影id
    "flag":1,//1-电影  0-非电影
    "videoName":"战狼",
    "extend":""//扩展信息(例如海报)
    "comment:"我觉得电影很好看",//电影评论
    "score":8,
    "netType":"wifi"
}
````

* 返回结果：
* 成功示例：

````

{
    code: 0,
    success：true
    data:{
        "videoId":12345
    }
}
````


## 3. 获取电影评分
* 接口名称： 支付接口
* 接口地址： `ygw/score/getVideoScore`
* 请求方式： `post`
* 请求说明

````
{
    "videoId":12345,//外部音频id
}
````

* 返回结果：
* 成功示例：

````

{
    code: 0,
    success：true
    data:{
        "videoId":12345,
        "score":7.8,
        "weight":1800,//权重(人数),
    }
}
````


## 4. 获取我的评分
* 接口名称： 支付接口
* 接口地址： `ygw/score/getMyScore`
* 请求方式： `post`

````
{
    "videoId":12345,//传入电影id时返回针对单个电影的评分以及评论
}
````

* 返回结果：
* 成功示例：

````

{
    code: 0,
    success：true
    data:{
        "number":"3",//第几部电影
        "movieId":12345,
        "score":7.8,
        "comment":[
            {
                "uuid":"abc",
                "contend":"很好看"
            },
            {
                "contend":"我又觉得不好看了"
            }
        ]
    }
}
````


## 5. 获取电影评论列表
* 接口名称： 获取电影评论列表
* 接口地址： `ygw/comment/list`
* 请求方式： `post`
* 请求说明
````
{
    "movieId":12345, // 电影id
    "pageSize":20, // 一页的数量
    "currentPage":1, // 第几页
}
````

* 返回结果：
* 成功示例：

````
{
    "code": 200,
    "success": true,
    "data": {
        "totalCount": 3,  // 总数量
        "currentPage": 1, // 当前页码
        "pageSize": 20, //
        "rows": [
                    {
                        "commentId":101,  // 评论id
                        "gmtCreate":"2056-08-27 09:35:57.467",// 创建时间
                        "content":"啊哈哈哈哈", // 评论内容
                        "likeNum":90,// 赞的数量
                        "isLike":true,// 登录人是否点赞
                        "userPortrait":"url",// 用户头像
                        "netType":wifi,// 网络类型
                        "userName":"",// 用户名称
                        "device":"", // 设备
                        "reply":[{   // 回复，这边最多3个
                              "replyId":3232, // 回复id
                              "content":"啊哈哈哈哈", // 回复内容
                              "userName":"",// 用户名称
                        }]
                    },
        ]
    }
}
````

## 6. 获取电影评论详情
* 接口名称： 获取电影评论详情
* 接口地址： `ygw/comment/detail`
* 请求方式： `post`


````
{
    "commentId":12345, // 评论id
    "pageSize":20, // 一页的数量
    "currentPage":1, // 第几页
}
````

* 返回结果：
* 成功示例：

````
{
    "code": 200,
    "success": true,
    "data": {
         "commentId":101,  // 评论id
         "gmtCreate":"2056-08-27 09:35:57.467",// 创建时间
         "content":"啊哈哈哈哈", // 评论内容
         "likeNum":90,// 赞的数量
         "isLike":true,// 登录人是否点赞
         "userPortrait":"url",// 用户头像
         "netType":"wifi",// 网络类型
         "userName":"",// 用户名称
        "device":"", // 设备
        "reply":{  // 回复
            "totalCount": 3,
            "currentPage": 1,
            "pageSize": 20,
            "rows": [
                    {
                        "replyId":"", // 回复id
                        "gmtCreate":"2056-08-27 09:35:57.467",// 创建时间
                        "content":"啊哈哈哈哈", // 评论内容
                        "userName":"",// 用户名称
                    },
        ]
    }
}
````



## 7. 点赞、取消赞
* 接口名称：点赞、取消赞
* 接口地址： `ygw/comment/operateLike`
* 请求方式： `post`


````
{
    "commentId":12345, // 评论id
    "opType":1,   // 1.赞 2.取消赞
}
````

* 返回结果：
* 成功示例：

````
{
    "code": 200,
    "success": true
}
----
````

## 8. 用户举报
* 接口名称： 用户举报
* 接口地址： `ygw/user/report`
* 请求方式： `POST`
* 请求参数:
````
{
    "from_id": 12, //举报人id 必传
    "to_id": 11, //被举报人id 必传
    "comment_id": 13 //举报评论id 必传
}
````

* 返回结果：
* 成功示例：

````
{

}
````

## 9. 问题反馈选项列表
* 接口名称： 问题反馈选项列表
* 接口地址： `ygw/problem/items`
* 请求方式： `POST`
````
[
    {
        "id": 11,
        "name": "登录问题",
        "children":[
            {
                "id": 12,
                "name": "验证码登录失败"
            },
            {
                "id": 13,
                "name": "切换账号登录失败"
            }
        ]
    }
]
````

## 10. 问题反馈
* 接口名称： 问题反馈
* 接口地址： `ygw/problem/commit`
* 请求方式： `POST`
* 请求参数:
````
{
    "user_id": 12, //用户id 必传
    "commit_id": 11, //选项id 必传
    "context": "", //评论内容 必传
    "email" : "", //邮箱
    "model" : "" //机型 1.苹果x
}
````



## 11. 问答内容
* 接口名称： 问答内容
* 接口地址： `ygw/question/answer`
* 请求方式： `POST`
````
[
    {
        "id": 1,
        "title": "", //问题
        "context": "", //回答
    }
]
````

## 12. 登出
* 接口名称： 登出
* 接口地址： `ygw/muic/logout`
* 请求方式： `get`

* 返回结果：
* 成功示例：
[source,json]
````
{
    "code": 200,
    "success": true
}
````


## 13. 是否有权限评分
* 接口名称： 获取信用分
* 接口地址： `ygw/muic/havePower`
* 请求方式： `get`

````
{
    "code": 200,
    "success": true,
    "data":true//true有 false 没有
}
````



## 14. 更新用户信息
* 接口名称： 支付接口
* 接口地址： `ygw/muic/uptUserInfo`
* 请求方式： `post`
* 请求说明
````
{
    "userName":"张三",
    "userPortrait":"http://*.img",//用户头像
    "gender":1//1-男 2-女
    "extend":"agwgwe",//个人扩展信息
}
````

* 返回结果：
* 成功示例：
````

{
    code: 0,
    success：true
}
````


## 15. 查看所有头像
* 接口名称： 支付接口
* 接口地址： `ygw/muic/findAllPic`
* 请求方式： `post`
* 请求说明
````
{
    "gender":1//0-所有 1-男 2-女 
}
````

* 返回结果：
* 成功示例:
````

{
    code: 0,
    success：true
    data:["http://a.png","http://b.png"]
}