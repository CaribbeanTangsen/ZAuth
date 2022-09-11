# ZAuth 用户权限中心

> 此项目配套JavaSDK即将开源
> 
> 框架可作为单体项目运行或作为权限中心供其他客户端调用
> 
> ***项目详细说明文档即将上线***
> 
> 开源不易，bug或建议请发送邮件到 CaribbeanTangsen@163.com
> 

### 依赖 ZCache

> https://github.com/CaribbeanTangsen/ZCache
> 
> 安装方法 mvn install

## 配置项 & 环境变量
```properties
#端口
PORT=2407
#数据库
DB_USER=root
DB_PWD=root
DB_URL=jdbc:mysql://localhost/zauth
SHOW_SQL=false

#token过期时间
TOKEN_TIME_OUT=1
#刷新token过期时间
REF_TOKEN_TIME_OUT=1
#jwt过期时间
JWT_TIME_OUT=1
#token颁发者
TOKEN_ISSUER=1
#token密钥
TOKEN_SECRET=1
#API 守卫开关
API_GUARD=true
#日志显示模式
LOG_MODE=debug | prod

#redis
REDIS_HOST=localhost
REDIS_PORT=6379


```

## Web访问地址 http://lcoalhost:8080/v1/
## swagger地址 http://lcoalhost:8080/v1/doc.html
> swagger页面仅在API守卫关闭的情况下生效