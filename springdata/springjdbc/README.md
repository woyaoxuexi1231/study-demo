## spring JDBC

`spring-boot-starter-jdbc` 是 Spring Boot 提供的一个启动器模块，用于简化 JDBC （Java Database Connectivity）开发。
它包含了一些常用的依赖和自动配置，可以帮助开发者快速搭建基于 JDBC 的数据访问层。

### 主要特性

1. **自动配置**：
    - 自动配置 `DataSource`，如果项目中存在相应的数据库驱动。也正是因为这个原因,这个将作为spring访问数据库的最基本的依赖,mybatis也会引用这个依赖,但是springredis是不会引入这个依赖的
    - 自动配置 `JdbcTemplate`，用于简化 JDBC 操作。

2. **默认依赖**：
    - 包含 Spring JDBC 和其他必要的库，无需手动配置。
    - 自动包含连接池（例如 HikariCP），作为默认的数据源连接池实现。

3. **简化的 JDBC 访问**：
    - 提供 `JdbcTemplate` 和 `NamedParameterJdbcTemplate`，简化常见的数据库操作。
    - 支持声明式事务管理。

### 典型结构

在一个典型的 Spring Boot 项目中，使用 `spring-boot-starter-jdbc` 可以极大简化 JDBC 的使用。一般步骤包括：

1. **添加依赖**：
   在 `pom.xml` 中添加 `spring-boot-starter-jdbc` 依赖：

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-jdbc</artifactId>
   </dependency>
   ```

2. **配置数据源**：
   在 `application.properties` 或 `application.yml` 文件中配置数据源信息：

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/mydb
   spring.datasource.username=root
   spring.datasource.password=secret
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   ```

   以上例子以 MySQL 为例，连接信息会根据使用的数据库类型和实际情况调整。

3. **使用 JdbcTemplate**：
   通过自动注入 `JdbcTemplate` 或 `NamedParameterJdbcTemplate`，进行数据库操作。

   ```java
   @Service
   public class UserService {

       private final JdbcTemplate jdbcTemplate;

       public UserService(JdbcTemplate jdbcTemplate) {
           this.jdbcTemplate = jdbcTemplate;
       }

       public List<User> findAll() {
           String sql = "SELECT * FROM users";
           return jdbcTemplate.query(sql, (rs, rowNum) ->
               new User(rs.getLong("id"), rs.getString("name"), rs.getString("email")));
       }

       public void save(User user) {
           String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
           jdbcTemplate.update(sql, user.getName(), user.getEmail());
       }
   }
   ```

### 优势

- **减少样板代码**：不需要手动管理数据库连接和关闭，`JdbcTemplate` 简化了查询、更新等操作。
- **自动化配置**：简便的配置，Spring Boot 会自动配置合适的 `DataSource` 并管理数据库连接池。
- **集成性强**：易于与其他 Spring 组件和模块集成，如 Spring Boot Test。

### 注意事项

- 默认的连接池是 HikariCP，尽管可以通过配置更改为其他连接池。
- 在生产环境中，建议仔细调整连接池参数以适应应用程序的负载。
- 尽管 `spring-boot-starter-jdbc` 提供了 JDBC 操作的简化方案，但当涉及复杂查询和高级功能时，可能需要考虑使用 JPA 或其他
  ORM 框架。

通过 `spring-boot-starter-jdbc`，开发者能够快速搭建一个基于 JDBC 的数据访问层，从而更专注于业务逻辑的实现。