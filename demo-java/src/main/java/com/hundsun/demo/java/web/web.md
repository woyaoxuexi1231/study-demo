> 1. web应用由web服务器来发布和运行
> 2. Tomcat启动时，出现了一系列中文乱码这里需要设置一下Tomcat服务器的输出文字编码找到conf文件夹下的 logging.properties 文件找到这一行： java.util.logging.ConsoleHandler.encoding = UTF-8将UTF-8修改为GBK


Web应用程序的内部目录结构可以因框架、技术栈和项目需求而异，但通常包括一些常见的目录和文件。以下是一个典型的Web应用程序内部目录结构的示例：

```
myapp/
|-- WEB-INF/
|   |-- classes/          # 编译后的Java类文件
|   |-- lib/              # 依赖的JAR文件
|   |-- web.xml           # Web应用程序的配置文件
|-- META-INF/             # 应用程序元数据目录
|   |-- MANIFEST.MF       # JAR文件的元数据
|   |-- context.xml       # 上下文配置文件
|   |-- persistence.xml   # JPA持久化配置文件
|-- static/               # 静态资源文件夹（CSS、JavaScript等）
|-- templates/            # 模板文件（如果使用模板引擎）
|-- WEB-INF/views/        # 视图文件（JSP、Thymeleaf等）
|-- src/                  # 源代码目录
|   |-- main/
|       |-- java/         # Java源代码
|       |-- resources/    # 资源文件，比如配置文件
|       |-- webapp/       # Web资源文件夹（HTML、CSS、JavaScript等）
|-- test/                 # 测试代码目录
|   |-- java/             # 测试用的Java源代码
|   |-- resources/        # 测试用的资源文件
```

在上述示例中：

- `WEB-INF` 目录是Java EE Web应用程序的标准目录，包含编译后的类文件、依赖的JAR文件以及配置文件等。
- `META-INF` 目录包含了应用程序的元数据信息，如MANIFEST.MF、context.xml、persistence.xml等。
- `static` 目录通常用于存放不经过处理的静态资源文件，如CSS、JavaScript、图片等。
- `templates` 目录是一些Web框架使用的，用于存放视图模板文件，比如Thymeleaf或FreeMarker模板。
- `WEB-INF/views` 目录可能包含应用程序的视图文件，如JSP文件或其他模板文件。
- `src` 目录包含应用程序的源代码，按照Maven/Gradle等构建工具的约定，通常包括`main`和`test`两个子目录。
- `test` 目录包含应用程序的测试代码，分为`java`和`resources`两个子目录。

请注意，这只是一个示例，实际的目录结构可能根据具体的项目需求、技术栈和团队约定而有所不同。