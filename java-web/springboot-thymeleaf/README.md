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