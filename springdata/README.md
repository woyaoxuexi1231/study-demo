## spring DATA common

Spring Data Commons 是 Spring Data 项目中的一个核心模块，它为 Spring Data 生态系统中的各个子模块提供了一致的基础设施和支持。无论是操作关系型数据库（如
Spring Data JPA），还是非关系型数据库（如 Spring Data MongoDB），Spring Data Commons 都为它们提供了一套统一的编程模型和公共功能。

### Spring Data Commons 提供的功能

1. **Repository 抽象**：
    - 提供 `CrudRepository`、`PagingAndSortingRepository` 等基础接口，统一了数据访问层的设计模式，简化了 CRUD 操作。
    - 支持方法名解析查询，通过接口方法定义，自动生成相应的查询。

2. **流式 API**：
    - 自 Spring Data 2.x 版本起，引入流式操作，支持对查询结果的处理采用 Java 8 的 `Stream` API。

3. **分页和排序**：
    - 定义了分页和排序相关的通用接口和实现，如 `Pageable` 和 `Sort` 等，能轻松实现对数据库查询的分页和排序功能。

4. **Auditing**：
    - 提供审计功能，用于追踪实体的创建和修改信息，例如创建时间、创建者、修改时间、修改者等。

5. **Specification 和 QueryDSL 支持**：
    - 提供了基于 JPA 的 `Specification` 接口，支持通过组合不同条件构建动态查询。
    - 为子项目支持 QueryDSL，以便用类型安全且强大的 DSL 构建查询。

6. **Converter & Domain Events**：
    - 提供类型转换框架和事件机制，允许在实体状态变化时进行事件的发布和处理。

7. **基于元数据的配置**：
    - 允许通过注解和配置元数据来简化数据库操作的实现，尤其是在需要跨越多个数据源时。

### 使用场景

Spring Data Commons 并不直接用于数据库操作，而是为具体的数据访问模块提供基础支持。因此，它是一个后端基础模块，不同的数据访问模块（如
Spring Data JPA、Spring Data MongoDB 等）都会依赖于它来实现各自的功能。

### 示例应用

使用 Spring Data JPA 时，以下内容直接得益于 Spring Data Commons 的功能：

```java
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByName(String name);
}
```

### 总结

Spring Data Commons 是 Spring
数据访问模块的基础，它提供了通用的数据访问和操作模板，极大地简化了由不同数据库特性带来的复杂性。通过统一的编程模型和多种灵活的实现机制，开发者可以更加专注于应用逻辑的实现。无论是传统的关系型数据库，还是新兴的
NoSQL 数据库，Spring Data Commons 都提供了良好的支持。