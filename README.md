## MyBoot

<div align=center>
<img src="https://i.loli.net/2021/11/02/5dZV1Oqoxc4R76G.png" width=300" height="300" />
</div>
<div align=center>
<img src="https://img.shields.io/badge/springboot-2.4.8-blue"/>
<img src="https://img.shields.io/badge/vue-2.6.1-brightgreen"/>
<img src="https://img.shields.io/badge/antdv-1.7.6-green"/>
<img src="https://img.shields.io/badge/mybatis-3.5-red"/>
</div>

### 项目简介 

> spring-myboot 是一个基于 [vue](https://vuejs.org) 和 [SpringBoot](https://spring.io/)，开发的全栈前后端分离的后台管理系统，集成jwt鉴权，JPA + Mybatis-Plus 任意切换，动态路由，动态菜单，Spring Security 鉴权，表单生成器，代码生成器等功能，提供多种示例文件，让您把更多时间专注在业务开发上。


#### 相关项目
- [gin-myboot](https://github.com/westhack/gin-myboot): https://github.com/westhack/gin-myboot

- [laravel-myboot](https://github.com/westhack/laravel-myboot): https://github.com/westhack/laravel-myboot

- [grpc-myboot - grpc 版](https://github.com/westhack/grpc-myboot): https://github.com/westhack/grpc-myboot


## 使用说明

```
- node版本 > v8.6.0
- java版本 >= v1.8.0
- IDE推荐：Idea
- Maven：>=3.x
```

### server项目

使用 `Idea` 等编辑工具，打开server目录，不可以打开 spring-myboot 根目录

```bash

# 克隆项目
git clone https://gitee.com/westhack/spring-myboot.git

# 进入server文件夹
cd server

# 打包 
mvn clean package

# 在有pom.xml文件夹即根目录下执行`mvn clean package`命令，执行完毕后拷贝生成的 `myboot-admin/tagert` 文件夹中的.jar文件至服务器
java -jar -Dfile.encoding=utf-8 myboot-admin-1.0-SNAPSHOT.jar &
```

#### web项目

```bash
# 进入web文件夹
cd backend-ui

# 安装依赖
yarn install || npm install

# 启动web项目
yarn serve || npm serve
```

### API文档

在浏览器输入 [http://localhost:8889/doc.html](http://localhost:8889/doc.html) 即可查看swagger文档


### 截图预览

![1.png](https://i.loli.net/2021/11/02/4UikFAHnQO7lJsb.png)

![2.png](https://i.loli.net/2021/11/02/sHGh3qwnoNLptRO.png)

![3.png](https://i.loli.net/2021/11/02/z95V1ntGjKr48xo.png)

![4.png](https://i.loli.net/2021/11/02/AH9vaCQGq2en6uR.png)

![5.png](https://i.loli.net/2021/11/02/xhRFwXJfuHIKZcT.png)

### 前端所用技术
- Vue 2.6.x、Vue Cli 4.x、antdv、Vuex、Vue Router、ES6、webpack、axios、echarts、cookie等

### 后端所用技术

##### 各框架依赖版本皆使用目前最新版本
- Spring Boot
- SpringMVC
- Spring Security
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/2.2.2.RELEASE/reference/html/)
- [MyBatis-Plus](http://mp.baomidou.com)：已更新至3.x版本
- [Elasticsearch](https://github.com/Exrick/xmall/blob/master/study/Elasticsearch.md)：基于Lucene分布式搜索引擎
- [Druid](http://druid.io/)：阿里高性能数据库连接池（偏监控 注重性能可使用默认HikariCP） [Druid配置官方中文文档](https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter)
- [Json Web Token(JWT)](https://jwt.io/)
- [Quartz](http://www.quartz-scheduler.org)：定时任务
- [Beetl](http://ibeetl.com/guide/#beetl)：模版引擎 代码生成使用
- [Thymeleaf](https://www.thymeleaf.org/)：发送模版邮件使用
- [Hutool](http://hutool.mydoc.io/)：Java工具包
- [Jasypt](https://github.com/ulisesbocchio/jasypt-spring-boot)：配置文件加密(thymeleaf作者开发)
- 第三方SDK或服务
    - [七牛云文件存储服务](https://developer.qiniu.com/kodo/sdk/1239/java)
    - [腾讯位置服务](https://lbs.qq.com/webservice_v1/guide-ip.html)：需申请填入key后免费使用
    - [阿里云短信服务](https://dysms.console.aliyun.com)
- 其它开发工具
    - [Lombok](https://projectlombok.org/)
    - [JRebel](https://github.com/Exrick/xmall/blob/master/study/JRebel.md)：开发秒级热部署
    - [阿里JAVA开发规约插件](https://github.com/alibaba/p3c)
- [JRebel](https://www.jrebel.com/)： 开发环境推荐使用 `JRebel`

### 主要功能

- 权限管理：基于`jwt`和`Spring Security`实现的权限管理。
- 文件上传下载：实现基于`七牛云`, 的文件上传操作(请开发自己去各个平台的申请对应 `token` 或者对应`key`)。
- 用户管理：系统管理员分配用户角色和角色权限。
- 角色管理：创建权限控制的主要对象，可以给角色分配不同api权限和菜单权限。
- 菜单管理：实现用户动态菜单配置，实现不同角色不同菜单。
- api管理：不同用户可调用的api接口的权限不同。
- 配置管理：配置文件可前台修改。
- 缓存管理：管理reids缓存。
- 条件搜索：动态自定义多条件搜索。
- 支持滑块验证码，图片验证码，短信验证码。
- 表单生成器：参考 [/backend-ui/src/modules/demo/views/view1.vue](https://github.com/westhack/gin-myboot/blob/master/backend-ui/src/modules/demo/views/view1.vue) 。
- 代码生成器：后台基础逻辑以及简单curd的代码生成器。
