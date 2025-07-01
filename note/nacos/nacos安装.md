# windows安装

参考文章：[Nacos详细教程_nacos 教程-CSDN博客](https://blog.csdn.net/Top_L398/article/details/111352983)

源码解压后：

```shell
./startup.cmd -m standalone
```



# Docker安装

[Docker安装nacos（图文并茂，避免踩坑，一步到位）_docker 安装nacos并且配置文件挂载-CSDN博客](https://blog.csdn.net/web2u/article/details/145107436)

[一文教你使用 Docker 启动并安装 Nacos-阿里云开发者社区](https://developer.aliyun.com/article/1274726)

[版本说明 · alibaba/spring-cloud-alibaba Wiki](https://github.com/alibaba/spring-cloud-alibaba/wiki/版本说明)



[Nacos 2.2.1版本 docker启动 缺少NACOS_AUTH_TOKEN等配置获取最新镜像 启动容器 此时容器 - 掘金](https://juejin.cn/post/7213677398720757821)

单机版

```shell
# 拉取镜像
docker pull nacos/nacos-server:v2.2.1
# https://hub.docker.com/r/nacos/nacos-server 这个网站有介绍使用

docker network create nacos_network

# 新版本从 1.4.1 版本开始支持鉴权功能
# 方法 1：使用默认的 SecretKey（Nacos 默认鉴权密钥）
# Nacos 默认的 SecretKey 是：SecretKey012345678901234567890123456789012345678901234567890123456789

echo -n "SecretKey012345678901234567890123456789012345678901234567890123456789" | base64

# docker run --name nacos-quick -e MODE=standalone -p 8849:8848 -d nacos/nacos-server:2.0.2
# 我们这里不带版本号，也不改端口，并且设置自动重启
docker run -d \
--name nacos-standalone \
-p 8848:8848 \
-e MODE=standalone \
-e NACOS_AUTH_ENABLE=false \
nacos/nacos-server:v2.2.1
```



# NACOS_AUTH_ENABLE、NACOS_AUTH_IDENTITY_KEY 参数

好的，我来给你详细讲讲你提到的 `NACOS_AUTH_ENABLE`、`NACOS_AUTH_IDENTITY_KEY` 等相关参数，以及它们背后的原理、设计目的，以及你遇到的这个报错怎么解决。

------

## 一、先说下 **Nacos 认证** 的背景

从 **Nacos 1.2.x** 开始，Nacos 就引入了 **权限控制（Auth）** 的功能，目的是：

- 保障配置中心、注册中心的安全性；
- 防止未经授权的用户随意读写服务列表和配置文件。

默认情况下：

- `NACOS_AUTH_ENABLE` 可以控制是否启用认证。
- 如果启用了认证，需要提供一些身份校验相关的配置，比如：
  - `NACOS_AUTH_IDENTITY_KEY`
  - `NACOS_AUTH_IDENTITY_VALUE`
  - `NACOS_AUTH_TOKEN` 等

------

## 二、这几个参数的含义

| 参数                        | 含义                   | 作用                                            |
| --------------------------- | ---------------------- | ----------------------------------------------- |
| `NACOS_AUTH_ENABLE`         | 是否开启认证           | `true` 开启，`false` 关闭（默认通常是 `false`） |
| `NACOS_AUTH_IDENTITY_KEY`   | 身份验证关键字         | 用于集群节点间通信时标识身份                    |
| `NACOS_AUTH_IDENTITY_VALUE` | 身份验证关键字的值     | 用于配合 `NACOS_AUTH_IDENTITY_KEY` 验证         |
| `NACOS_AUTH_TOKEN`          | 用于节点间通信的 Token | Nacos 节点之间的 RPC 调用需要认证               |

------

## 三、为什么会报 `Could not resolve placeholder 'NACOS_AUTH_IDENTITY_KEY'`

这是因为：

- 你把 `NACOS_AUTH_ENABLE` 设置成了 `false`，按理说就 **不需要** 再配置身份验证了。
- 但是在 Nacos 的某些版本里（尤其是 2.x），如果没有显式设置 `NACOS_AUTH_IDENTITY_KEY` 和 `NACOS_AUTH_IDENTITY_VALUE`，有时它还是会去解析它们，导致找不到就报错。

**本质原因**：

- Nacos 的启动脚本里有些地方写死了必须解析这些变量，即使你没开认证也会去解析。

------

## 四、如何解决

✅ **推荐做法**：
 在 `docker run` 的时候显式传入这两个参数，即使你没用到它。

举个例子：

```bash
docker run -d \
--name nacos-standalone \
--restart=unless-stopped \
-p 8848:8848 \
-e MODE=standalone \
-e NACOS_AUTH_ENABLE=true \
-e NACOS_AUTH_IDENTITY_KEY=serverIdentity \
-e NACOS_AUTH_IDENTITY_VALUE=secret \
-e NACOS_AUTH_TOKEN="U2VjcmV0S2V5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5" \
nacos/nacos-server:v2.2.1
```

这里：

- `NACOS_AUTH_IDENTITY_KEY` 设置为任意非空字符串，比如 `serverIdentity`。

- `NACOS_AUTH_IDENTITY_VALUE` 也随便填，比如 `secret`。

- `NACOS_AUTH_TOKEN` 要求长度必须要大于 32 位，而且必须使用 base64 编码。

  比如 echo -n "SecretKey012345678901234567890123456789012345678901234567890123456789" | base64

只要有值，它就不会报错了。

------

## 五、如果你要开启认证呢？

如果你想要开启认证（`NACOS_AUTH_ENABLE=true`），那么：

- 必须设置这两个身份参数；
- 并且要配置用户和权限（比如用控制台建用户、给用户分配角色）。



这对 Key/Value 并没有硬性要求，只要在所有节点里保持一致就行。

常见做法：

- `NACOS_AUTH_IDENTITY_KEY`：随便取个名字，比如 `serverIdentity`。
- `NACOS_AUTH_IDENTITY_VALUE`：可以是一个随机字符串，最好复杂点，起到类似密钥的作用。

**推荐做法**：

```bash
# 生成一个随机字符串（Linux/Mac）
openssl rand -hex 16
```

比如生成：

```bash
e3b0c44298fc1c14af0c
```

那么就可以这样配置：

```bash
-e NACOS_AUTH_IDENTITY_KEY=serverIdentity \
-e NACOS_AUTH_IDENTITY_VALUE=e3b0c44298fc1c14af0c
```

------

## 六、总结

- ✅ **NACOS_AUTH_ENABLE**：控制是否启用权限认证。
- ✅ **NACOS_AUTH_IDENTITY_KEY / VALUE**：用于集群节点间身份校验，单机模式也要传（有些版本里是硬编码的坑）。
- ✅ **为什么会报错**：解析不到环境变量占位符，必须有默认值。

------

**实战经验**：

> 很多用户在用 `standalone` 单机模式时，推荐直接 `NACOS_AUTH_ENABLE=false` 并显式传入这两个身份变量，避免坑。

------

# 使用

Nacos默认账户密码是  nacos / nacos



---

### 1. 核心访问路径**
#### **控制台首页**
- **路径**: `http://192.168.3.102:8848/nacos`
  - 作用：Nacos 的 Web 控制台，用于管理服务发现、配置中心、命名空间等。
  - 如果访问此路径仍返回 404，可能是：
    - Nacos 未成功启动（检查日志）。
    - 上下文路径被自定义修改（检查 `application.properties` 中的 `server.servlet.contextPath`）。

#### **OpenAPI 入口**
- **路径**: `http://192.168.3.102:8848/nacos/v1/`
  - 作用：Nacos 的 RESTful API 根路径，用于通过 HTTP 接口管理服务、配置等。
  - 示例：
    - 查询所有服务：`/nacos/v1/ns/service/list`
    - 获取配置：`/nacos/v1/cs/configs?dataId=xxx&group=xxx`

---

### **2. 其他常见路径**
#### **健康检查**
- **路径**: `http://192.168.3.102:8848/nacos/actuator/health`
  - 作用：检查 Nacos 服务健康状态（需开启 Spring Boot Actuator）。

#### **Prometheus 监控**
- **路径**: `http://192.168.3.102:8848/nacos/actuator/prometheus`
  - 作用：暴露 Prometheus 格式的监控指标（需配置监控）。

#### **集群状态**
- **路径**: `http://192.168.3.102:8848/nacos/v1/ns/operator/raft/peer/list`
  - 作用：查看 Nacos 集群节点状态（需集群模式）。

#### **配置管理**
- **路径**: `http://192.168.3.102:8848/nacos/v1/cs/configs`
  - 作用：通过 API 获取或发布配置。

---

### **3. 可能的问题排查**
1. **检查 Nacos 是否启动成功**：
   - 查看日志：`logs/start.out` 或 `logs/nacos.log`，确认无报错。
   - 检查端口占用：`netstat -tlnp | grep 8848`。

2. **检查上下文路径**：
   - 配置文件 `conf/application.properties` 中是否修改了：
     ```properties
     server.servlet.contextPath=/nacos
     ```
   - 如果自定义了路径（如 `/my-nacos`），需访问 `http://192.168.3.102:8848/my-nacos`。

3. **防火墙/网络问题**：
   - 确保服务器防火墙放行了 8848 端口：
     ```bash
     sudo ufw allow 8848
     ```

4. **版本差异**：
   - Nacos 2.x 默认启用了 gRPC 端口（9848），但 Web 端口（8848）仍为主入口。

---





# 作为配置中心

[nacos 配置中心详解（有这一篇就够啦）-CSDN博客](https://blog.csdn.net/qing_zhi_feng/article/details/136363273)

