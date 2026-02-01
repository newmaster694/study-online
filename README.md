# Study-Online 项目文档

## 📌 项目概述

学成在线 是一个基于微服务架构的在线学习平台，主要聚焦于课程管理与媒体资源处理。该项目采用模块化设计，通过多个独立的服务模块实现不同的业务功能，并使用 Spring Cloud 进行服务治理。

### 核心功能

- **内容管理**：支持课程基本信息、分类、教师信息、教学计划等内容的管理。
- **媒体服务**：提供对媒体文件的上传、存储及处理功能。
- **认证服务**：
- **统一网关**：作为系统请求的统一入口，进行路由转发和权限控制。
- **基础服务**：封装通用的功能模块，如异常处理、配置管理等。

---

## 🧱 系统架构

### 架构模式
- **微服务架构**：整个系统被拆分为多个独立的服务模块（如 `content-service`, `gateway-service`, `media-service`）。
- **分层架构**：每个服务内部遵循典型的三层架构（Controller - Service - Mapper）。
- **服务治理**：使用 Spring Cloud 和 Spring Cloud Alibaba 实现服务注册发现、配置中心等功能。

### 技术栈
| 技术名                                      | 版本        | 备注                                                         |
| ------------------------------------------- | ----------- | ------------------------------------------------------------ |
| spring boot                                 | 3.2.0       | 简化配置开发                                                 |
| spring cloud                                | 2023.0.0    | 微服务框架                                                   |
| spring cloud alibaba                        | 2023.0.1.0  | 微服务框架                                                   |
| mybatis-plus                                | 3.5.5       | ORM框架                                                      |
| MySQL                                       | 8.3.0       | 数据库                                                       |
| Elasticsearch                               | 8.17.5      | 收索引擎                                                     |
| Fastjson2                                   | 2.0.53      | JSON序列化与反序列化框架                                     |
| Druid                                       | 1.2.19      | 数据库连接池                                                 |
| Hutool                                      | 5.8.25      | 集成工具包                                                   |
| OkHttp                                      | 4.12.0      | 远程调用(RPC)工具包，主要是应用OpenFeign框架使用             |
| Redis                                       | 6.2         | 分布式缓存数据库                                             |
| RabbitMQ                                    |             | 消息队列框架，需要安装并启用`rabbitmq_delayed_message_exchange`插件 |
| Nacos                                       | 2.2.3+      | 服务治理与配置文件管理框架                                   |
| XXL-JOB                                     | 3.0.0       | 分布式计划任务处理工具                                       |
| spring-security                             |             | 安全管理框架                                                 |
| spring-security-oauth2-authorization-server |             | OAuth2服务器权限下发框架                                     |
| kaptcha                                     | 2.3.3       | 图片验证码生成工具                                           |
| JustAuth                                    | 1.16.6      | 简化Oauth2登录工具                                           |
| IJPay                                       | 2.9.12.1-17 | 简化第三方支付集成工具                                       |
| commons-lang3                               | 3.17.0      | 集成工具包                                                   |
| minio                                       |             | 分布式对象存储服务器                                         |
| lombok                                      | 1.18.42     | 简化实体类模板代码生成工具                                   |
| spring-boot-starter-test                    |             | 测试框架                                                     |
|                                             |             |                                                              |



#### 开发环境要求
- **JDK**：Java 21
- **Maven**：3.8.1 或更高版本
- **IDE**：IntelliJ IDEA 2025.x 推荐

---

## 📁 项目目录结构

```
study-online/
├── content-service/          # 课程内容管理模块
├── gateway-service/          # 网关服务模块
├── media-service/            # 媒体资源管理模块
├── study-online-base/        # 公共基础依赖模块
├── system-service/           # 系统相关服务模块（未完全展示）
├── README.md
└── pom.xml                   # 根级 Maven 配置文件
```


### 主要模块说明
| 模块名称 | 功能描述 |
|----------|----------|
| `content-service` | 提供课程管理核心功能，包括课程信息、分类、教师信息、教学计划等。 |
| `gateway-service` | 作为系统的统一入口，负责请求路由、鉴权、限流等功能。 |
| `media-service` | 负责媒体资源（如视频、图片）的上传、下载及处理。 |
| `study-online-base` | 封装了通用的基础类、异常处理、工具类、常量定义等。 |
| `auth-service` | 认证服务，提供了账号密码认证与基于Oauth2的微信登录认证 |
| `captcha-service` | 验证码服务，负责生成与校验验证码 |
| `search-sercice` | 搜索服务，调用elasticsearch框架，主要搜索课程详情信息 |
| `study-online-api` | open-feign框架进行微服务之间的远程接口调用 |
| `study-online-message-sdk` | 消息队列信息处理服务 |

---

## ⚙️ 快速启动指南

### 1. 安装依赖
确保已安装以下工具：
- JDK 21
- Maven 3.8.1+
- MySQL 8.3.0+

### 2. 下载项目

```bash
git clone https://github.com/newmaster694/study-online.git
cd study-online
```

### 3. 构建项目

```bash
mvn clean install
```


### 4. 启动服务
分别运行各个模块的主类以启动服务。

> [!Tip]
>
> 注意:请先启动mysql服务器、Redis服务器、Nacos服务治理中心、elasticsearch搜索服务中心、minio对象存储服务器、xxl-job分布式定时任务处理框架。然后再启动项目。

### 5. 访问接口

默认访问地址为：http://localhost:8080

> 请注意，这个项目采用的是mybatis-plus的分页插件实现的分页效果，在分页的相关HTTP接口返回的是`com.baomidou.mybatisplus.extension.plugins.pagination.Page`类而不是教程中的`PageHelper`的相关类，所以前端在接收时的相关数据类型应当正确处理才能展示正确信息

---

## 📦 数据库配置

本项目使用 Nacos 作为配置中心，数据库连接信息如下：

```yaml
spring:
    datasource:
        url: "jdbc:mysql://${study.online.mysql.host:127.0.0.1}:3306/${study.online.mysql.database}?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai"
        driver-class-name: com.mysql.cj.jdbc.Driver
        username: ${study.online.mysql.username}
        password: "${study.online.mysql.password}"
mybatis-plus:
    configuration:
        map-underscore-to-camel-case: true # mybatis驼峰转换配置
```


> 注意：请确保在 Nacos 中正确配置数据库相关信息。
---

## 🐇 RabbitMQ 配置

本项目使用 RabbitMQ 实现异步任务处理，包括：
- 视频处理超时任务（延迟队列）
- 文件中断清理任务（延迟队列）

### 配置示例：

```yaml
spring:
    rabbitmq:
        host: "${rabbitmq.host:127.0.0.1}"
        port: 5672
        username: "${rabbitmq.username}"
        password: "${rabbitmq.password}"
```

> **注意**：如果使用延迟队列，请确保 RabbitMQ 已安装并启用了 `rabbitmq_delayed_message_exchange` 插件。

---

## ✈ Openfeign相关配置

关于OpenFeign的相关服务作者将其抽成了一个独立的模块`study-online-api`，然后在需要远程调用的服务中(例如`content-service`)引入该模块。

> [!note]
> 在 Spring Boot 3.x 之后就废弃了`CommonsMultipartFile`这个实现类,所以直接导入教程的`MultipartSupportConfig.java`会报错`无法解析 CommonsMultipartFile`。参考网上的案例,作者直接把这个缺失的类拷贝到了`content-service`模块下的`util`工具包下作为解决方案。

---

## 🔒认证与授权模块

由于本项目采用的是==spring-boot 3.2.0==，对应的安全框架是`spring-security 6.2.0`与`spring-oauth2-authentication-server 1.2.0`，配置文件改为了标准DSL语法与Lambda表达式调用，另外相较于教程中的`spring-oauth2-server`框架，本项目的oauth2服务器框架默认只支持Oauth2.1的安全协议（简单点说，不支持密码模式，想使用密码模式只能手动扩展了）

by the way，spring security框架实在是太难配了，后期可能会换成 sa-token 这样的轻量级框架。

---

## 📞 联系方式

如有任何问题或需要技术支持，请联系：
- Email: newmaster695@gmail.com
- GitHub: [https://github.com/newmaster694/study-online](https://github.com/newmaster694/study-online)