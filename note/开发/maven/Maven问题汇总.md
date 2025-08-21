Maven拉取依赖出现报错：Since Maven 3.8.1 http repositories are blocked.

问题解决：

- 参考文章：https://www.cnblogs.com/davidhhuan/p/17004656.html

- 修改maven全局配置里的配置，把配置里的下面的内容注释。

  idea在的安装目录下: C:\Program Files\JetBrains\IntelliJ IDEA 2022.1\plugins\maven\lib\maven3\conf

  ```
  <mirror>
    <id>maven-default-http-blocker</id>
    <mirrorOf>external:http:*</mirrorOf>
    <name>Pseudo repository to mirror external repositories initially using HTTP.</name>
    <url>http://0.0.0.0/</url>
    <blocked>true</blocked>
  </mirror>
  ```

  



## 配置代理

在 maven 的配置文件的 proxies 标签下添加 

```
    <proxy>
      <id>optional</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>192.168.3.2</host>
      <port>21231</port>
    </proxy>
```

通过 `mvn -version` 找到对应的位置
