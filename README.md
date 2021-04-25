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
   1. 从github获得用户信息
   2. 生成对应的token写入cookie和数据库
   3. 下次登录验证cookie
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

```mysql
create table QUESTION
(
	ID INT auto_increment,
	TITLE VARCHAR(50),
	DESCRIPTION TEXT,
	GMT_CREATE BIGINT,
	GMT_MODIFIED BIGINT,
	CREATOR INT,
	COMMENT_COUNT INT default 0,
	VIEW_COUNT INT default 0,
	LIKE_COUNT INT default 0,
	TAG VARCHAR(256),
	constraint QUESTION_PK
		primary key (ID)
);

comment on column QUESTION.ID is '文章id';
comment on column QUESTION.TITLE is '文章标题';
comment on column QUESTION.GMT_CREATE is '创建时间';
comment on column QUESTION.GMT_MODIFIED is '修改时间';
comment on column QUESTION.CREATOR is '创建人id';
comment on column QUESTION.COMMENT_COUNT is '评论数';
comment on column QUESTION.VIEW_COUNT is '阅读数';
comment on column QUESTION.LIKE_COUNT is '点赞数';
comment on column QUESTION.TAG is '标签';
```

