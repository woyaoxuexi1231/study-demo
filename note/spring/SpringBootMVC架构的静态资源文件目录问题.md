# Spring Thymeleaf 项目

## Spring Boot 静态资源目录

原生静态文件存放目录:

- classpath:/META-INF/resources/
- classpath:/resources/
- classpath:/static/
- classpath:/public/

模板框架静态文件:

- classpath:/templates

## thymeleaf 目录

ThymeleafProperties配置类可以看到 thymeleaf 的默认目录:

```java
public static final String DEFAULT_PREFIX = "classpath:/templates/";
public static final String DEFAULT_SUFFIX = ".html";
```

只要引入了thymeleaf之后, view 的转发最后都会转到 thymeleaf   
所以尽管在 static 和 templates 下都有 home.html, 但是 static 下面的永远不会被使用  
如果更改 templates 为 templates2, 而 thymeleaf 的默认目录不变, 那么尽管在 static 下面有 home.html, 依旧会报错

```
Error resolving template [home], template might not exist or might not be accessible by any of the configured Template Resolvers
```

# Spring JS 项目

## Spring Boot 静态文件目录

原生静态文件存放目录:

- classpath:/META-INF/resources/
- classpath:/resources/
- classpath:/static/
- classpath:/public/

模板框架静态文件:

- classpath:/templates

## 静态文件处理方式

Spring Boot中的默认配置规定了静态资源的处理方式。

1. 默认的静态资源访问目录
   没有使用Thymeleaf或其他模板引擎时，Spring Boot会默认将静态资源放在**src/main/resources/static**目录下，并且会优先查找这些静态资源。
   因此，当访问根路径时（例如http://localhost:8080/），SpringBoot会自动查找**index.html**
   文件并返回给客户端，这是因为index.html通常被用作Web应用的默认首页。
   这种默认行为是出于方便考虑的，因为在许多Web应用中，index.html是默认的起始页面。
   如果需要自定义这个行为，可以修改Spring Boot的配置以更改默认的静态资源目录或修改默认的首页文件。
2. **WebMvcAutoConfiguration** 配置类
   org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.addResourceHandlers
   这个方法会配置默认的静态资源处理器，指定了静态资源的查找位置，其中包括了 classpath:/static/