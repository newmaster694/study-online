# Study-Online 项目文档

## 📌 项目概述

学成在线 是一个基于微服务架构的在线学习平台，主要聚焦于课程管理与媒体资源处理。该项目采用模块化设计，通过多个独立的服务模块实现不同的业务功能，并使用 Spring Cloud 进行服务治理。

### 核心功能

- **课程管理**：支持课程基本信息、分类、教师信息、教学计划等内容的管理。
- **媒体服务**：提供对媒体文件的上传、存储及处理功能。
- **统一网关**：作为系统请求的统一入口，进行路由转发和权限控制。
- **基础服务**：封装通用的功能模块，如异常处理、配置管理等。

---

## 🧱 系统架构

### 架构模式
- **微服务架构**：整个系统被拆分为多个独立的服务模块（如 `content-service`, `gateway-service`, `media-service`）。
- **分层架构**：每个服务内部遵循典型的三层架构（Controller - Service - Mapper）。
- **服务治理**：使用 Spring Cloud 和 Spring Cloud Alibaba 实现服务注册发现、配置中心等功能。

### 技术栈
#### 后端技术
- **框架**：Spring Boot 3.2.0 + Spring Cloud 2023.0.0 + Spring Cloud Alibaba 2023.0.1.0
- **ORM 框架**：MyBatis-Plus 3.5.5
- **数据库**：MySQL 8.3.0
- **搜索引擎**：Elasticsearch 8.17.5
- **其他工具**：
  - Fastjson2 2.0.53
  - Druid 1.2.19
  - Hutool 5.8.25
  - OkHttp 4.12.0

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

---

## ⚙️ 快速启动指南

### 1. 安装依赖
确保已安装以下工具：
- JDK 21
- Maven 3.8.1+
- MySQL 8.3.0+

### 2. 下载项目

```bash
git clone <repository-url>
cd study-online
```

### 3. 构建项目

```bash
mvn clean install
```


### 4. 启动服务
分别运行各个模块的主类以启动服务：
- [ContentApplication.java](file://K:\java_workspace\study-online\content-service\src\main\java\study\online\content\ContentApplication.java) → 启动课程服务
- [GatewayApplication.java](file://K:\java_workspace\study-online\gateway-service\src\main\java\study\online\gateway\GatewayApplication.java) → 启动网关服务
- [MediaApplication.java](file://K:\java_workspace\study-online\media-service\src\main\java\study\online\media\MediaApplication.java) → 启动媒体服务

### 5. 访问接口
默认访问地址为：`http://localhost:8080`

---

## 📦 数据库配置

本项目使用 Nacos 作为配置中心，数据库连接信息如下：

```yaml
spring:
  datasource:
    url: jdbc:mysql://${study.online.host:127.0.0.1}:3306/${study.online.database}?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai
    username: ${study.online.username}
    password: ${study.online.password}
    driver-class-name: com.mysql.cj.jdbc.Driver
```


> 注意：请确保在 Nacos 中正确配置数据库相关信息。
---

## 📞 联系方式

如有任何问题或需要技术支持，请联系：
- Email: cryingsky@icloud.com
- GitHub: [https://github.com/newmaster694/study-online](https://github.com/newmaster694/study-online)