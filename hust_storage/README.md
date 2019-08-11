### SpringBoot + SpringSecurity + JWT + Swagger2的企业级系统管理平台基础版本。

#### 1. 实现功能：

| 功能点 | 详情 | 完成度 |
| ------ | ------ | ------ |
| 用户管理 |  | 100% |
| 角色管理 |  | 100% |
| 权限管理 |  | 100% |
| jwt | 包含登录注册，用户访问token过滤，结合SpringSecurity完成数据库权限 | 100% |
| 登录 | 使用form登录成功后返回jwt token | 100% |
| 注册 |  | 100% |
| Swagger2 |  | 100% |

#### 2. 使用的框架及版本：

| JAR | 版本 | 结构 |
| ------ | ------ | ------ |
| SpringBoot | 2.0.3 | - |
| SpringSecurity | - | - |
| StringCache | - | - |
| JWT | 0.9.0 | io.jsonwebtoken:jjwt |
| Swagger2 | 2.9.2 | io.springfox |

#### 3. 获取登录用户
Contorller 参数增加
```java
@AuthenticationPrincipal User user
```