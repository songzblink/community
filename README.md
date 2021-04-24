## 资料
[Spring 文档](https://spring.io/guides)

[Spring Web 文档](https://spring.io/guides/gs/serving-web-content/)

[es 社区](https://elasticsearch.cn/explore) #我们前端抄它

[Github deploy key](https://developer.github.com/v3/guides/managing-deploy-keys/#deploy-keys)

[Bootstrap 快速前端](https://v3.bootcss.com/getting-started/)

[Github OAuth 网页登录接入](https://docs.github.com/en/developers/apps/creating-an-oauth-app)
## 工具
[Git](https://git-scm.com/download)

[OkHttp](https://square.github.io/okhttp/) # java 进行 http 请求

## 大致流程

1. 快速创建 SpringBoot 项目
2. 使用 Bootstrap 快速布局导航栏
3. 引入 Github OAuth 实现网页登录接入（后面自己把cookie到数据库的认证丢到redis？）
4. 



### 脚本

```mysql
create table USER
(
	ID INT auto_increment,
	ACCOUNT_ID VARCHAR(100),
	NAME VARCHAR(50),
	TOKEN CHAR(36),
	GMT_CREATE BIGINT,
	GMT_MODIFIED BIGINT,
	constraint USER_PK
		primary key (ID)
);
comment on column USER.GMT_CREATE is '格林威治时间gmt';
comment on column USER.GMT_MODIFIED is '格林威治时间gmt';


```

