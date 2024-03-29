## 资料
[Spring 文档](https://spring.io/guides)

[Spring Web 文档](https://spring.io/guides/gs/serving-web-content/)

[es 社区](https://elasticsearch.cn/explore) #我们前端抄它

[Github deploy key](https://developer.github.com/v3/guides/managing-deploy-keys/#deploy-keys)

[Bootstrap 快速前端](https://v3.bootcss.com/getting-started/)

[Github OAuth 网页登录接入](https://docs.github.com/en/developers/apps/creating-an-oauth-app)

[thymeleaf 文档](https://www.thymeleaf.org/)

[jQuery API Documentation](https://api.jquery.com/)

[Markdown 插件](https://pandao.github.io/editor.md/)

## 工具
[Git](https://git-scm.com/download)

[OkHttp](https://square.github.io/okhttp/) # java 进行 http 请求

[Lombok](https://projectlombok.org/)

## 大致流程

1. 快速创建 SpringBoot 项目
2. 使用 Bootstrap 快速布局导航栏
3. 引入 Github OAuth 实现网页登录接入（后面自己把cookie到数据库的认证丢到redis？）
   1. 从github获得用户信息
   2. 生成对应的token写入cookie和数据库
   3. 下次登录验证cookie
4. 实现登录状态持久化
5. 完成文章发布功能
6. 完成分页功能
7. 拦截器实现
8. 完成文章跟新功能
9. mybatis generator 配置：命令`mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate`，覆盖式
10. 通用处理异常问题以及白页问题
11. 完成阅读数功能
12. 完成评论接口以及异常处理
13. 添加事务
14. 完成二级回复
15. 完成tag的正则匹配查询相关问题
16. 后端校验以规范标签tag
17. 添加回复通知功能
18. 添加markdown支持
19. 腾讯云COS上传图片支持（蛮贵的，建议去掉直接让他去找图床上传自己的图片）
20. sql实现搜索功能

### 脚本

```mysql
create table USER
(
	ID BIGINT auto_increment,
	ACCOUNT_ID VARCHAR(100),
	NAME VARCHAR(50),
	TOKEN CHAR(36),
	GMT_CREATE BIGINT,
	GMT_MODIFIED BIGINT,
	AVATAR_URL VARCHAR(100),
	constraint USER_PK
		primary key (ID)
);
comment on column USER.GMT_CREATE is '格林威治时间gmt';
comment on column USER.GMT_MODIFIED is '格林威治时间gmt';
comment on column USER.AVATAR_URL is '头像';
```

```mysql
create table QUESTION
(
	ID BIGINT auto_increment,
	TITLE VARCHAR(50),
	DESCRIPTION TEXT,
	GMT_CREATE BIGINT,
	GMT_MODIFIED BIGINT,
	CREATOR BIGINT,
	COMMENT_COUNT INT default 0,
	VIEW_COUNT INT default 0,
	LIKE_COUNT INT default 0,
	TAG VARCHAR(256),
	constraint QUESTION_PK
		primary key (ID)
);
comment on column QUESTION.TITLE is '文章标题';
comment on column QUESTION.GMT_CREATE is '创建时间';
comment on column QUESTION.GMT_MODIFIED is '修改时间';
comment on column QUESTION.CREATOR is '创建人id';
comment on column QUESTION.COMMENT_COUNT is '评论数';
comment on column QUESTION.VIEW_COUNT is '阅读数';
comment on column QUESTION.LIKE_COUNT is '点赞数';
comment on column QUESTION.TAG is '标签';
```

```mysql
create table COMMENT
(
	ID BIGINT auto_increment,
	PARENT_ID BIGINT not null,
	TYPE INT,
	COMMENTATOR BIGINT,
	GMT_CREATE BIGINT,
	GMT_MODIFIED BIGINT,
	LIKE_COUNT BIGINT default 0,
	CONTENT VARCHAR(1024),
	COMMENT_COUNT INT default 0,
	constraint COMMENT_PK
		primary key (ID)
);
comment on column COMMENT.PARENT_ID is '关联的question的id';
comment on column COMMENT.TYPE is '父类类型';
comment on column COMMENT.COMMENTATOR is '评论人id';
comment on column COMMENT.GMT_CREATE is '创建时间';
comment on column COMMENT.GMT_MODIFIED is '修改时间';
comment on column COMMENT.LIKE_COUNT is '点赞数';
comment on column COMMENT.CONTENT is '评论';
comment on column COMMENT.COMMENT_COUNT is '评论数';
```

```mysql
create table NOTIFICATION
(
	ID BIGINT auto_increment,
	NOTIFIER BIGINT not null,
	RECEIVER BIGINT not null,
	OUTER_ID BIGINT not null,
	TYPE INT not null,
	GMT_CREATE BIGINT not null,
	STATUS INT default 0 not null,
	NOTIFIER_NAME VARCHAR(100),
	OUTER_TITLE VARCHAR(256),
	constraint NOTIFICATION_PK
		primary key (ID)
);
comment on column NOTIFICATION.NOTIFIER is '发出消息的人';
comment on column NOTIFICATION.RECEIVER is '接收消息的人';
comment on column NOTIFICATION.OUTER_ID is '问题或者回复的id';
comment on column NOTIFICATION.TYPE is '类型：评论或者回复';
comment on column NOTIFICATION.STATUS is '已读标记：0未读 1已读';
comment on column NOTIFICATION.NOTIFIER_NAME is '发出消息的人的名字';
```



## 遇到的问题

1.数据库设计不合理（用户ID类型），修改数据库导致需要修改大量代码。数据库设计的合理性很重要。





## 部署

### 1.部署依赖

#### 1.1 更新仓库

```shell
yum update
```

#### 1.2 安装git

```
yum install git
```

#### 1.3 在root目录下创建项目环境

在 root 目录下创建 App 文件夹

```shell
mkdir App
cd App
```

克隆项目到 App 文件夹

```shell
git clone https://github.com/songzblink/community.git
```

安装 maven

```shell
yum install maven
```

检查是否安装成功

```shell
mvn -v
```

修改 maven 源为国内镜像，通过 `mvn -v` 找到 maven 的安装地址，然后打开 conf 目录中找到 settings.xml 文件，在 `<mirrors>` 标签下添加以下内容：

```xml
<mirror>  
    <id>nexus-aliyun</id>  
    <mirrorOf>central</mirrorOf>    
    <name>Nexus aliyun</name>  
    <url>http://maven.aliyun.com/nexus/content/groups/public</url>  
</mirror>
```

编译打包项目

```shell
mvn clean compile package
```

**问题**

出现错误：

```
The plugin org.apache.maven.plugins:maven-resources-plugin:3.2.0 requires Maven version 3.1.0
```

解决方法：在 pom.xml 中添加 `<plugins>` 标签下添加下面代码，修改 maven 版本号

```xml
<plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-resources-plugin</artifactId>
        <version>3.1.0</version>
</plugin>
```

### 2. 修改相关配置

复制一份配置文件，命名为 application-production.properties，目的是创建一份实际部署使用到的 profile 配置文件。使用的时候可以通过[三种方式去激活 profile](https://blog.csdn.net/iteye_8208/article/details/82680632)。

```shell
cp src/main/resources/application.properties  src/main/resources/application-production.properties
```

重新打包项目

```shell
mvn package
```

### 3. 启动项目

激活 production 的 profile，并启动项目

```shell
java -jar -Dspring.profiles.active=production target/community-0.0.1-SNAPSHOT.jar 
```

这个时候已经可以通过访问服务器的公网 ip 来访问项目的主页。

### 4. 关闭项目

```shell
ps -aux | grep java
```

然后 kill 对应的进程。