Spring MVC 是 Spring 框架的一个 Web 开发框架，用于构建 Web 应用程序和 RESTful 服务。它采用了经典的 MVC（模型-视图-控制器）设计模式，将一个 Web
应用程序分成三个核心部分：模型（Model）、视图（View）和控制器（Controller），以提高代码的模块化程度、可维护性和可测试性。
下面是 Spring MVC 的三个核心组件及其作用的简要介绍：

1. **模型（Model）**：
   模型代表应用程序的数据和业务逻辑。在 Spring MVC 中，模型通常是一个普通的 Java 对象（POJO），用于封装和处理数据。模型对象通常存储在请求作用域中，可以通过请求处理流程来获取和操作。模型与视图之间是独立的，视图不会直接修改模型数据。
2. **视图（View）**：
   视图负责将模型数据呈现给用户，并处理用户的交互。在 Spring MVC 中，视图通常是一个 JSP 页面、Thymeleaf 模板或者是 JSON/XML
   等数据格式。视图负责将模型数据渲染成用户可见的界面，以便用户进行交互操作。在处理完请求之后，控制器会将模型数据传递给适当的视图进行呈现。
3. **控制器（Controller）**：
   控制器负责处理用户请求，并协调模型和视图之间的交互。在 Spring MVC 中，控制器通常是一个带有 `@Controller` 注解的 Java 类，它使用 `@RequestMapping` 注解来映射请求 URL
   和请求方法。控制器接收用户请求后，会调用适当的业务逻辑处理方法，然后将模型数据传递给视图进行呈现。

Spring MVC 框架提供了一套灵活的处理器映射机制、视图解析机制和异常处理机制，使得开发者能够更加方便地构建 Web 应用程序。它还提供了丰富的标签库和注解，用于简化表单处理、数据绑定、数据验证等操作。另外，Spring MVC 框架还支持 RESTful 风格的 Web
服务开发，通过 `@RestController` 注解可以很方便地创建 RESTful 服务。总的来说，Spring MVC 是一个功能强大、灵活性高、易于使用的 Web 开发框架，广泛应用于构建各种类型的 Web 应用程序和 RESTful 服务。

1. **DispatcherServlet（调度器 Servlet）**：Spring MVC 的核心组件，负责接收 HTTP 请求并将其分派到相应的处理器（Controller）上进行处理。
   `DispatcherServlet`（调度器 Servlet）是 Spring MVC 框架的核心组件之一，它是一个特殊的 Servlet，负责接收客户端的 HTTP 请求，并将请求分派到相应的处理器（Controller）进行处理。DispatcherServlet 在整个
   Spring MVC 请求处理流程中起着非常重要的作用。  
   以下是 DispatcherServlet 的主要功能和特点：  
   **接收请求**：
   DispatcherServlet 作为应用程序的前端控制器（Front Controller），负责接收所有客户端的 HTTP 请求。它监听一个或多个 URL 地址，并拦截所有与这些 URL 地址匹配的请求。  
   **请求处理流程**：
   一旦接收到请求，DispatcherServlet 将根据配置的处理器映射（Handler Mapping）来确定该请求应该由哪个处理器来处理。然后，它将请求委派给相应的处理器（Controller）进行处理，并等待处理器的执行结果。  
   **处理器执行**：
   处理器（Controller）是实际处理请求的组件，它执行业务逻辑并生成模型数据。DispatcherServlet 调用处理器的方法，并将请求的上下文信息传递给处理器。处理器执行完毕后，通常会返回一个逻辑视图名或者一个视图对象。  
   **视图解析**：
   DispatcherServlet 将处理器返回的逻辑视图名（或者视图对象）解析成实际的视图对象。它使用视图解析器（View Resolver）来完成这个工作，视图解析器会根据配置的规则来将逻辑视图名解析成实际的视图对象（如 JSP 页面、Thymeleaf 模板等）。  
   **视图呈现**：
   一旦确定了视图对象，DispatcherServlet 将调用视图对象的渲染方法来生成最终的响应内容。视图对象通常负责将模型数据填充到视图中，并将渲染结果发送给客户端。  
   **异常处理**：
   在整个请求处理流程中，如果出现异常，DispatcherServlet 会调用配置的异常处理器（Exception Resolver）来处理异常。异常处理器负责捕获和处理异常，并生成合适的错误响应。  
   **拦截器支持**：
   DispatcherServlet 支持拦截器（Interceptor）机制，允许开发者在请求处理的不同阶段进行预处理或后处理操作。拦截器可以在 DispatcherServlet 的请求处理流程中插入自定义的逻辑。  
   总的来说，DispatcherServlet 在 Spring MVC 框架中起着非常重要的作用，它负责整个请求处理流程的协调和控制。通过适当的配置，开发者可以定制 DispatcherServlet 的行为，以满足不同的需求和场景。
2. **HandlerMapping（处理器映射器）**：负责将请求映射到相应的处理器上。
   `HandlerMapping`（处理器映射器）是 Spring MVC 框架中的一个重要组件，它负责将请求映射到对应的处理器（Controller）上进行处理。在 Spring MVC 中，处理器映射器起着路由的作用，根据请求的 URL 和其他条件来确定哪个处理器将处理该请求。
   Spring MVC 提供了多种实现了 HandlerMapping 接口的默认处理器映射器，每种映射器都有不同的映射规则。以下是一些常见的 HandlerMapping 实现：  
   **BeanNameUrlHandlerMapping**：
   这是一个简单的映射器，根据请求 URL 的路径查找相应的处理器 Bean。例如，如果请求的 URL 路径是 `/user`，则会查找名为 `userController` 的处理器 Bean。  
   **DefaultAnnotationHandlerMapping**：
   这个映射器会扫描 Spring 应用程序上下文中所有标记了 `@Controller` 注解的 Bean，并根据 `@RequestMapping` 注解的配置来确定请求路径与处理器之间的映射关系。  
   **RequestMappingHandlerMapping**：
   这是 Spring MVC 中最常用的映射器，它支持多种注解（如 `@RequestMapping`、`@GetMapping`、`@PostMapping` 等）来定义请求路径与处理器的映射关系。它会根据请求的 HTTP 方法和路径来匹配对应的处理器。  
   **SimpleUrlHandlerMapping**：
   这个映射器允许开发者通过配置显式地指定 URL 与处理器之间的映射关系。可以根据请求的 URL 路径或者请求的请求头信息等来匹配处理器。  
   **RequestMappingInfoHandlerMapping**：
   这是 Spring MVC 5.0 引入的新的映射器，它支持更灵活的请求映射规则，可以基于请求路径、请求参数、请求头、请求方法等多个条件来匹配处理器。
   这些映射器的选择取决于应用程序的需求和开发者的偏好。通常情况下，开发者不需要自己选择映射器，Spring MVC 框架会根据配置自动选择合适的映射器来处理请求。 HandlerMapping 的工作是在 Spring MVC
   的请求处理流程中的第一个步骤，它确定了请求将由哪个处理器来处理。
3. **Controller（控制器）**：处理 HTTP 请求，执行业务逻辑，并返回相应的视图。
   在 Spring MVC 框架中，Controller（控制器）是用于处理客户端请求并生成响应的组件。Controller 负责接收请求、执行业务逻辑，并返回相应的视图或数据给客户端。Controller 在 MVC（模型-视图-控制器）架构中扮演着控制请求和响应的角色。  
   以下是 Controller 的主要特点和功能：
   **请求映射**：
   Controller 使用 `@RequestMapping` 注解或其衍生注解（如 `@GetMapping`、`@PostMapping` 等）来指定处理特定 URL 地址的请求。通过这些注解，开发者可以将请求映射到特定的处理方法上。
   ```java
   @Controller
   public class MyController {
   
       @GetMapping("/hello")
       public String hello() {
           return "hello";
       }
   }
   ```
   **处理请求**：
   Controller 中的处理方法（handler method）负责执行实际的业务逻辑，并生成模型数据。处理方法通常是一个公开的方法，用于处理特定类型的请求，可以接收请求参数、执行业务逻辑，并返回响应数据。
   ```java
   @Controller
   public class MyController {
   
       @GetMapping("/hello")
       public String hello(Model model) {
           model.addAttribute("message", "Hello, world!");
           return "hello";
       }
   }
   ```
   **数据绑定**：
   Controller 可以使用 `@RequestParam`、`@PathVariable` 等注解来绑定请求参数或路径变量到处理方法的参数上。这样，开发者可以方便地获取客户端发送的数据，并在处理方法中进行处理。
   ```java
   @Controller
   public class MyController {
   
       @GetMapping("/hello")
       public String hello(@RequestParam("name") String name, Model model) {
           model.addAttribute("message", "Hello, " + name + "!");
           return "hello";
       }
   }
   ```
   **返回视图**：
   处理方法通常会返回一个逻辑视图名（Logical View Name），DispatcherServlet 会根据视图解析器（View Resolver）将逻辑视图名解析成实际的视图对象，并将视图渲染成最终的响应结果返回给客户端。
   ```java
   @Controller
   public class MyController {
   
       @GetMapping("/hello")
       public String hello() {
           return "hello";
       }
   }
   ```
   **返回数据**：
   除了返回视图外，处理方法还可以直接返回数据对象（如 POJO、Map、List 等），Spring MVC 框架会自动将这些数据转换成合适的响应格式（如 JSON、XML）并发送给客户端。
   ```java
   @Controller
   public class MyController {
   
       @GetMapping("/data")
       @ResponseBody
       public Map<String, String> data() {
           Map<String, String> data = new HashMap<>();
           data.put("message", "Hello, world!");
           return data;
       }
   }
   ```

总的来说，Controller 在 Spring MVC 框架中扮演着非常重要的角色，它负责处理客户端的请求、执行业务逻辑，并生成相应的视图或数据。通过合适的配置和编码，开发者可以实现灵活、可维护的 Web 应用程序。

4. **ViewResolver（视图解析器）**：将逻辑视图名称解析为实际的视图对象
   ViewResolver（视图解析器）是 Spring MVC 框架中的一个关键组件，它负责将逻辑视图名称（Logical View Name）解析为实际的视图对象（View）。在处理完请求后，Controller 方法通常会返回一个逻辑视图名称，例如 `"hello"`
   ，而不是直接返回一个具体的视图对象。ViewResolver 就是负责将这个逻辑视图名称解析成实际的视图对象的组件。
   下面是 ViewResolver 的主要工作流程：
   **获取逻辑视图名称**：
   当处理器方法执行完成后，它会返回一个逻辑视图名称，例如 `"hello"`。
   **视图解析**：
   DispatcherServlet 将这个逻辑视图名称传递给 ViewResolver，ViewResolver 会根据配置的规则和策略来将逻辑视图名称解析成实际的视图对象。
   **视图渲染**：
   一旦获取到实际的视图对象，DispatcherServlet 就会调用视图对象的 `render` 方法来渲染视图，并生成最终的响应结果。渲染的结果通常是一个 HTML 页面、一个 JSON 字符串或者一个 XML 文档，这取决于具体的视图对象。
   Spring MVC 框架提供了多种不同类型的 ViewResolver 实现，常见的 ViewResolver 包括：

- **InternalResourceViewResolver**：
  这是 Spring MVC 默认的视图解析器，它将逻辑视图名称解析为一个 JSP 文件路径，并返回一个 InternalResourceView（内部资源视图）对象。通常用于将逻辑视图名称映射到 JSP 视图。

```xml

<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/views/"/>
    <property name="suffix" value=".jsp"/>
</bean>
```

- **XmlViewResolver**：
  这个视图解析器从 XML 文件中读取视图配置，将逻辑视图名称解析为一个 AbstractUrlBasedView（基于 URL 的抽象视图）对象。

```xml

<bean class="org.springframework.web.servlet.view.XmlViewResolver">
    <property name="location" value="/WEB-INF/views/views.xml"/>
</bean>
```

- **FreeMarkerViewResolver**、**VelocityViewResolver** 等：
  这些视图解析器用于将逻辑视图名称解析为 FreeMarker、Velocity 等模板引擎的视图对象。

通过合适配置 ViewResolver，开发者可以根据项目的需求将逻辑视图名称映射到各种类型的实际视图对象上，从而实现灵活、可定制的视图解析策略。

