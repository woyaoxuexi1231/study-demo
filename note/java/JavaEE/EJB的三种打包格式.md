在 Java EE（Jakarta EE）中，**EJB** 容器的应用可以打包成 **WAR**、**EAR** 或 **JAR** 文件，具体取决于你的应用架构和容器的要求。下面是对每种打包格式的解释，以及在什么情况下使用它们。

### 1. **WAR 包（Web Archive）**

- **用途**：用于 Web 应用程序，包含 Servlet、JSP、静态资源（如 HTML、CSS、JS）等，通常与 Web 服务器一起运行。

- 包含内容

  ：

  - `web.xml` 配置文件
  - Web 应用的所有 Java 类（如 Servlet、JSP）以及相关的资源文件
  - 如果 Web 应用中包含 EJB 服务，EJB 部分通常会打包到 WAR 文件的 **`WEB-INF/lib`** 目录下作为一个 JAR 包。

- 适用场景

  ：

  - 如果你的应用是一个 Web 应用，并且其中包含 EJB，那么你可以将 **EJB** 和 **Web 组件（Servlet、JSP 等）** 一起打包成 **WAR** 文件。
  - EJB 容器在部署时会识别 WAR 包中的 `WEB-INF/lib` 下的 EJB JAR 文件，并根据需要初始化 EJB 容器。

**示例**：如果你将 EJB 放在 WAR 文件中，你的 `pom.xml` 可以包括：

```xml
<dependencies>
    <dependency>
        <groupId>javax.ejb</groupId>
        <artifactId>javax.ejb-api</artifactId>
        <version>3.2</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

然后，你将 EJB 类放置在 `src/main/java` 中，打包成 `war` 文件，部署到支持 EJB 的应用服务器（如 TomEE）。

### 2. **EAR 包（Enterprise Archive）**

- **用途**：用于企业级应用，支持包含多种模块类型，如 Web 模块（WAR）、EJB 模块（JAR）、应用客户端等，适用于更复杂的企业级应用架构。
- **包含内容**：
  - **Web 模块**：通常是 WAR 文件
  - **EJB 模块**：通常是 JAR 文件，包含 EJB 类
  - **应用客户端模块**（如果有）
  - **`META-INF/application.xml`**：描述整个应用结构的配置文件，指定哪个 EJB 模块，哪个 WAR 模块等
- **适用场景**：
  - 如果你有一个包含多个模块的企业应用，其中包括 EJB、Web 应用、以及可能的应用客户端，应该使用 **EAR** 包。
  - **EJB JAR** 文件会作为 EAR 文件的一部分被打包和部署。

**示例**：EAR 包适用于更复杂的应用架构，通常与 Java EE 容器（如 WildFly、GlassFish）一起使用。EAR 包结构示例如下：

```
my-app.ear
├── my-web-app.war
├── my-ejb-app.jar
├── META-INF/
│   └── application.xml
```

`application.xml` 中会描述该 EAR 包中的各个模块：

```xml
<application xmlns="http://java.sun.com/xml/ns/javaee"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
                                 http://java.sun.com/xml/ns/javaee/application_5.xsd"
             version="5">
    <module>
        <web>
            <web-uri>my-web-app.war</web-uri>
            <context-root>/my-web-app</context-root>
        </web>
    </module>
    <module>
        <ejb>my-ejb-app.jar</ejb>
    </module>
</application>
```

### 3. **JAR 包（Java Archive）**

- **用途**：用于将 EJB 单独打包成一个 JAR 文件，通常只包含 EJB 类，没有 Web 模块。

- 包含内容

  ：

  - 只有 EJB 类和相关的依赖 JAR（如 `javax.ejb`）。
  - 需要与 **EJB 容器** 配合使用，部署到支持 EJB 的应用服务器或容器（如 TomEE、WildFly）。

- 适用场景

  ：

  - 如果你有一个 **纯粹的 EJB 模块**，不涉及 Web 层（Servlet/JSP），且需要将它部署到一个支持 EJB 的容器中（如 TomEE、WildFly、GlassFish），则可以将 EJB 类打包成 JAR 文件。
  - **EJB JAR** 文件不包含 Web 应用或客户端，只包含 EJB 实现和接口。

**示例**：如果你只打包 EJB 类到 JAR 文件，`pom.xml` 可以如下配置：

```xml
<packaging>jar</packaging>

<dependencies>
    <dependency>
        <groupId>javax.ejb</groupId>
        <artifactId>javax.ejb-api</artifactId>
        <version>3.2</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

在这种情况下，你的项目将是一个 **EJB JAR** 模块，部署到 **EJB 容器** 中。

### 4. **总结：何时使用哪种包格式？**

- **WAR 包**：
  - 如果你的应用是 **Web 应用**，并且包含 EJB 组件，可以将 EJB 和 Web 组件一起打包成 WAR 文件。
  - 使用 TomEE 或其他支持 EJB 的容器，WAR 文件会自动处理 EJB 服务。
- **EAR 包**：
  - 如果你的应用包含多个模块（如 EJB 模块、Web 模块、客户端模块等），则可以使用 EAR 包。它允许将不同类型的模块（如 WAR、EJB）打包在一个文件中，并一起部署到 Java EE 容器。
- **EJB JAR 包**：
  - 如果你的应用只是一个 **EJB 模块**（没有 Web 组件），则可以将 EJB 类打包成 JAR 文件，部署到支持 EJB 的容器中。

### 示例：如何打包 EJB 模块

假设你有一个简单的 EJB 模块，里面包含一个 `MyEJBService` 类。

#### 1. **EJB 类**

```java
import javax.ejb.Stateless;

@Stateless
public class MyEJBService {
    public String sayHello() {
        return "Hello from EJB!";
    }
}
```

#### 2. **`pom.xml` 配置**

```xml
<packaging>ejb</packaging>

<dependencies>
    <dependency>
        <groupId>javax.ejb</groupId>
        <artifactId>javax.ejb-api</artifactId>
        <version>3.2</version>
        <scope>provided</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <version>3.1.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>jar</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

#### 3. **生成 JAR 包**

```bash
mvn clean package
```

这将生成一个 **EJB JAR** 文件，你可以将其部署到支持 EJB 的容器中。

### 结论

- **WAR** 包适用于包含 Web 层和 EJB 组件的 Web 应用。
- **EAR** 包适用于更复杂的企业级应用，其中可能包含多个模块（EJB、Web、客户端等）。
- **EJB JAR** 包适用于纯粹的 EJB 模块，没有 Web 层，仅包含 EJB 组件。

选择哪种打包方式取决于你的应用需求和架构。如果你正在构建一个 Web 应用并且包含 EJB，`WAR` 包是最常用的选择。如果你只需要部署 EJB，则 `JAR` 包是最佳选择。