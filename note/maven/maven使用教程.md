参考书籍：《Maven实战》

## 仓库篇

### 仓库的分类

1. 本地仓库
2. 远程仓库
   - 中央仓库，http://repo1.maven.org/maven2
   - 私服，例如：公司内部使用私服工具搭建的maven私服库，Nexus；JFrog；Apache Archiva。
   - 其他公共库，例如：阿里云



### 仓库的配置

可以配置pom，可以在全局setting中配置。

```xml
    <repositories>
        <repository>
            <!-- 其实这个id是随便起的,但是要保证唯一,而且如果名字是central,那么会覆盖默认的中央仓库 -->
            <id>my-snap-repository</id>
            <name>nexus发布版仓库</name>
            <url>http://192.168.80.128:8081/repository/my-snap-repository/</url>
            <!-- 这两个用来控制发布版构建和快照版构建的下载 -->
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <!-- 控制从远程仓库更新的频率 -->
                <updatePolicy>daily</updatePolicy>
                <!-- 配置maven检查检验和文件的策略 -->
                <!-- 当配置为warn的时候,如果检验校验和文件失败那么会输出警告日志 -->
                <checksumPolicy>ignore</checksumPolicy>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
```

仓库可能涉及认证

```xml
    <!-- 仓库可能存在认证信息,如果需要,那么就要在setting文件中进行配置 -->
    <!-- 配置在setting文件中的考虑是因为pom文件往往都能下载,认证信息如果放在这里就不安全了,所以一般存在本机的setting文件中是比较安全的 -->
    <!-- 这里是一个私服配置 -->
    <servers>
      <server>
        <id>my-snap-repository</id>
        <username>admin</username>
        <password>admin</password>
      </server>
    </servers>
```



### 快照版本和稳定版本

快照版本依赖以 -SNAPSHOT 结尾, 快照版本的出现主要是为了快速构建



### 镜像

定义: 如果仓库X可以提供仓库Y存储的所有内容，那么就可以认为仓库X是仓库Y的一个镜像。





## 插件篇

### 生命周期

Maven的声明周期就是为了对所有的构建过程进行抽象和统一。而maven的生命周期是抽象的，实际的任务都是由插件来完成的。

#### clean声明周期

clean声明周期一共包含三个阶段，目的是为了清理项目：

1. pre-clean 执行清理前需要完成的工作
2. clean 清理上一次构建生成的文件
3. post-clean 执行一些清理后需要完成的工作



#### default声明周期

default生命周期定义了真正构建时所需要执行的所有步骤。包含：

1. validate
2. initialize
3. generate-sources
4. process-sources 处理项目的主资源文件，一般来说就是对 src/main/resources 目录的内容进行变量替换的工作后，复制到项目的输出的主classpath目录中
5. generate-resources
6. process-resources
7. compile 编译项目的主源码，一般来说就是编译 src/main/java 目录下的Java文件在项目输出的主classpath目录下
8. process-classes
9. generate-test-sources
10. process-test-sources 处理项目测试资源文件，一般来说就是 /src/test/resources 目录中的内容进行变量替换。随后输出到主目录
11. generate-test-resources
12. process-test-resources
13. test-compile 编译项目的测试代码，也就是 src/test/java目录下的Java文件到主目录
14. process-test-classes
15. test 运行单元测试框架进行测试，测试代码不会被打包和部署
16. prepare-package
17. package
18. pre-integration-test
19. integration-test
20. post-integration-test
21. verify
22. install 将包安装到maven的本地仓库，供其他本地Maven项目使用。
23. deploy 将最终的包复制到远程仓库，供其他开发人员和Maven项目使用。

default声明周期较多，官方文档：[Maven – Introduction to the Build Lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)



#### site声明周期

site生命周期的目的是为了建立和发布项目站点。Maven能够基于POM所包含的信息，自动生成一个友好的站点，方便团队交流和发布项目信息。

1. pre-site 执行一些在生成项目站点之前需要完成的工作
2. site 生成项目站点文档
3. post-site 执行一些在生成项目站点之后需要完成的工作
4. site-deploy 将生成的项目站点发布到服务器上

#### 命令行与声明周期

maven各个生命周期虽然是独立的,但是一个生命周期的阶段是有前后依赖关系的。

$mvn clean 执行clean生命周期，但是会先执行pre-clean和clean两个阶段

$mvn clean install 先执行pre-clean到clean的生命周期，在执行从validate到install的所有声明周期

其他的类似

### 插件

#### 常用插件

插件和生命周期是相互绑定的，maven有一些内置的绑定关系：

```bash
Clean Lifecycle
┌─────────────────────────┐
│  pre-clean              │  (无默认绑定)
│                         │
│  clean                  │  maven-clean-plugin:clean
│                         │
│  post-clean             │  (无默认绑定)
└─────────────────────────┘

Site Lifecycle
┌─────────────────────────┐
│  pre-site               │  (无默认绑定)
│                         │
│  site                   │  maven-site-plugin:site  
│                         │
│  post-site              │  (无默认绑定)
|  site-deploy            |  maven-site-plugin:deploy
└─────────────────────────┘

Default Lifecycle
┌─────────────────────────┐
│  process-resources      |  maven-resources-plugin:resources 复制主资源文件到输出目录
│                         │
│  compile                │  maven-compiler-plugin:compile   编译主代码至输出目录
│					      |  
│  process-test-resources |  maven-resource-plugin:testResources 复制测试资源文件至输出目录
│  test-compile           |  maven-compiler-plugin:testComplie 编译测试代码至输出目录
|  test                   │  maven-surefire-plugin:test 执行测试用例
│                         │
│  package                │  maven-jar-plugin:jar 打包项目，可能是jar，可能是war
│                         │
│  install                │  maven-install-plugin:install 将项目输出构建安装到本地仓库
│                         │
│  deploy                 │  maven-deploy-plugin:deploy 将项目输出构建安装到远程仓库
└─────────────────────────┘
```

至于无默认绑定的生命周期，那么在实际运行时也就没有任何实际行动。

除了内置绑定之外，也可以选择绑定插件到目标生命周期上。

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <version>2.0.2.RELEASE</version>
    <configuration>
        <!--
         JAR: regular executable JAR layout.
         WAR: executable WAR layout. provided dependencies are placed in WEB-INF/lib-provided to avoid any clash when the war is deployed in a servlet container.
         ZIP (alias to DIR): similar to the JAR layout using PropertiesLauncher.
         NONE: Bundle all dependencies and project resources. Does not bundle a bootstrap loader.
         -->
        <!-- 指定打包布局为 ZIP 格式。Spring Boot 支持多种布局，如 JAR、WAR 和 ZIP。这里选择了 ZIP 以生成一个 ZIP 包。 -->
        <layout>ZIP</layout>
    </configuration>
    <executions>
        <execution>
            <!-- 定义了要执行的目标。在这个例子中，目标是 repackage，即重新打包应用程序，把所有依赖和项目打包成一个可执行的 JAR 或 ZIP 文件。 -->
            <goals>
                <goal>repackage</goal>
            </goals>
            <!-- 指定在 package 阶段执行，即在 Maven 的 package 阶段自动执行重新打包操作。 -->
            <phase>package</phase>
        </execution>
    </executions>
</plugin>
```

#### 插件信息查询

1. 网上找，maven官方可以找到几乎所有官方插件的信息
2.  mvn help: describe-Dplugin = org.apache.maven.Plugins: maven-compliler-plugins:2.1



#### 快照版本和稳定版本的打包

 快照版本和稳定版本的打包主要有以下几种方式：
 1. 手动修改pom文件的版本，快照时添加-SNAPSHOT后缀，稳定版本时去掉
 2. 使用 maven-release-plugin 管理版本发布时可以更为自动化。
    该插件自动更新 pom.xml 中的版本号。
    执行 mvn release:prepare 会自动去掉 -SNAPSHOT，提交修改，然后标记版本。
    执行 mvn release:perform 来进行实际的发布。
 3. 使用 CI/CD 工具
    Jenkins、GitLab CI、GitHub Actions 等在流水线中将 SNAPSHOT 版本用于开发分支，将去掉 -SNAPSHOT 的版本用于发布分支。

#### 使用 maven-release-plugin 进行版本发布

参考文章：[[Maven版本管理插件\] - maven-release-plugin介绍与使用-CSDN博客](https://blog.csdn.net/weixin_43600770/article/details/130709318)

配置scm

```xml
<scm>
    <!--      git https  -->
    <connection>scm:git:https://github.com/woyaoxuexi1231/study-demo.git</connection>
    <!--      git https  -->
    <developerConnection>scm:git:https://github.com/woyaoxuexi1231/study-demo.git</developerConnection>
    <!--      git url  -->
    <url>https://github.com/woyaoxuexi1231/study-demo.git</url>
    <tag>HEAD</tag>
</scm>
```

配置私服信息

```xml
<!-- 配置部署到私服 -->
<distributionManagement>
    <!-- 快照仓库的目的就是为了解决未发布模块的快速构建-->
    <snapshotRepository>
        <id>my-snap-repository</id>
        <name>Nexus自定义的快照私服</name>
        <url>http://192.168.80.128:8081/repository/my-snap-repository/</url>
    </snapshotRepository>
    <!-- 发布版构建仓库 -->
    <repository>
        <id>my-repository</id>
        <name>Nexus自定义的稳定私服</name>
        <url>http://192.168.80.128:8081/repository/my-repository/</url>
    </repository>
</distributionManagement>
```

配置maven-release-plugin插件

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-release-plugin</artifactId>
    <version>3.0.1</version>
    <configuration>
        <!--  prepare发布之前执行的mvn命令   -->
        <preparationGoals>clean verify</preparationGoals>
        <!--  生成的tag格式  这里 @{} 而不是 ${} 可以防止project.version被其他方式覆盖 -->
        <tagNameFormat>release-@{project.version}</tagNameFormat>
        <!--   手动push -->
        <pushChanges>false</pushChanges>
    </configuration>
</plugin>
```

**先执行 mvn release:prepare**

**这个命令默认情况下会做以下几个事情**

- 检查是否存在为commit的代码
- 检查是否存在SNAPSHOT的依赖
- 修改POM的project.version 成为一个不带SNAPSHOT的release版本(默认有三种version生成策略[see 其他])
- 修改SCM中对应的TAG
- 运行单元测试
- 提交被修改的pom
- 根据SCM给commit log 打一个对应的 tag
- 更换本地pom文件为一个新的version(根据策略,默认是在最后一个版本号+1 并且加上SNAPSHOT)
  最后根据配置执行 completionGoals

**此时**

- 此时我们的 1.0.0-release 版本已经被commit到本地并且打上了对应的tag
- 同时，我们的 project.version 递增了一个SNAPSHOT版本
- 项目更目录会生成两个文件 pom.xml.releaseBackup
- release.properties 用于后续的操作

**发布**

发布前要检查，如果在插件中配置了 pushChanges 为false，那么prepare的时候不会自动push任何代码，只是commit到了本地，然而perform做的事情就是拉代码和deploy，所以会导致拉不到代码而失败，所以需要手动push代码和tag。
(git push origin --tags/git push origin <tagName>)
(在idea中在push的时候选择左下角的 ALL TAG 即可)

**执行 mvn release:perform**

**做了以下事情**

- 根据SCM的URL和TAG 拉取最新的tag代码到 workingDirectory(默认: target/checkout)
- 对拉下来的代码执行 deploy / site-deploy 发布到仓库
- 构建成功的话就会删除根目录的 pom.xml.releaseBackup / release.properties 文件

**此时**

- 一个 1.0.0-SNAPSHOT 发布到仓库并且打一个tag为 1.0.0
- 修改version为 1.0.1-SNAPSHOT 并将 1.0.0 deploy到私有仓库的过程就完成了



**遇到的问题：**

mvn release:prepare 遇到报错

```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-release-plugin:3.0.1:prepare (default-cli) on project dependencies-parent: Unable to tag SCM
[ERROR] Provider message:
[ERROR] The git-tag command failed.
[ERROR] Command output:
[ERROR] fatal: tag 'release-1.0.0.3' already exists
```

git标签已经存在，直接删了git tag -d release-1.0.0.3



发布时报错，问题：

1. 这里不知道为什么这个插件非要去这个分支拉取代码，暂时还不知道怎么配置。现在的做法是先创建这个分支，进行 perform操作
2. 上述操作完成后，打包成功了，但是稳定版没有上传到nexus私服，只有快照版本

```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-release-plugin:3.0.1:perform (default-cli) on project dependencies-parent: Unable to checkout from SCM
[ERROR] Provider message:
[ERROR] The git-clone command failed.
[ERROR] Command output:
[ERROR] Cloning into 'checkout'...
[ERROR] warning: Could not find remote branch release-1.0.0.1 to clone.
[ERROR] fatal: Remote branch release-1.0.0.1 not found in upstream origin
```

