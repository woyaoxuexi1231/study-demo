# JPA和JDBC

JDBC（Java Database Connectivity）和JPA（Java Persistence API）都是用于Java应用程序中与数据库交互的技术，但它们在设计目标、使用方式和灵活性上有一些明显的区别。

1. **抽象层次**：
    - **JDBC**：提供一种底层的API来直接与数据库通信。你需要编写SQL查询来进行数据库操作。因此，它提供了对数据库的细粒度控制，但同时也需要更多的代码。
    - **JPA**：是一个ORM（对象关系映射）规范，旨在简化数据库操作，允许开发者使用面向对象的方式来与数据库交互。JPA将数据库表映射到Java对象，减少直接的SQL操作。

2. **开发效率**：
    - **JDBC**：由于需要手动编写SQL以及处理结果集，开发通常比JPA更耗时，容易出错，特别是在复杂查询时。
    - **JPA**：通过注解或XML配置文件来进行对象与数据库之间的映射，自动生成SQL，大大提高了开发效率，特别适合CRUD操作。

3. **维护性**：
    - **JDBC**：由于SQL语句散布在代码中，维护起来比较麻烦，特别是在业务逻辑复杂的情况下。
    - **JPA**：将业务逻辑与数据访问逻辑分离，使用实体模型来操作数据，更容易维护和修改代码。

4. **性能**：
    - **JDBC**：因直接使用SQL，可能在性能调优上更具优势（如：索引优化、查询优化）。
    - **JPA**：提供了性能优化的机制（如缓存、懒加载），但在涉及复杂查询时，有时可能需要自定义SQL以获得更好的性能。

5. **事务处理**：
    - **JDBC**：需要手动管理事务，通过`Connection`对象来处理事务的提交和回滚。
    - **JPA**：通常由容器管理，特别是在Java EE环境中，处理事务更加简单，通过注解即可管理事务的边界。

总的来说，JDBC适合需要精细控制和优化性能的场景，而JPA适合希望提高开发效率和可维护性的项目。选择使用哪种技术，需要根据项目需求综合考虑。

- jdbc由各个关系型数据库实现,使用SQL与数据库通信
- jpa由ORM(Object-Relational Mapping)框架实现,用面向对象的方式,通过ORM框架生成sql进行操作,JPA在jdbc之上,底层依赖jdbc来操作数据库

# JPA规范的介绍

jpa是在sun公司在jdk1.5的时候提出的一种ORM规范:

1. 简化持久化操作的开发工作.
2. sun公司希望持久化技术能够统一,实现天下归一:如果你基于JPA进行持久话,那么你可以随意的切换数据库

JPA提供了:

1. ORM映射元数据. jpa支持xml和注解两种元数据的形式,比如@Table,@Column这些注解
2. JPA的API. 用来操作实体数据,执行CRUD操作,框架在后台生成sql,自动执行. 提供了一些自带的api, findById这些类似的api
3. JPQL查询语句. 通过面向对象而非面向数据库的查询语言查询数据

# 关于jpa依赖的名称问题

`javax.persistence` 和 Jakarta Persistence API 都是与持久化技术相关的API，但它们有一些关键的背景区别：

1. **起源与演变**：
    - **javax.persistence**：这是最初由Java EE（Java Platform, Enterprise Edition）定义的持久化API，通常被简单地称作JPA（Java
      Persistence API）。它是在Java Community Process (JCP) 下由多个行业领导者共同开发的。

    - **Jakarta Persistence**：这是EJB（Enterprise JavaBeans）之后的升级和转变版本，归属于Eclipse基金会旗下的Jakarta
      EE（原Java EE）。名称上的改变主要来源于Oracle与Eclipse基金会关于商标使用的限制问题。自Jakarta EE 9开始，所有与EE相关的包都从
      `javax.*`迁移到了`jakarta.*`，包括持久化API。

2. **包名变化**：
    - 在Java EE 8和更早的版本中使用的是`javax.persistence`包名。
    - 在Jakarta EE 9及后续版本中，包名改为`jakarta.persistence`。

3. **功能和规范**：
    - 虽然名称和包名发生了变化，但基本功能特性保持一致。Jakarta Persistence API延续了JPA的标准，实现对象-关系映射（ORM），以简化数据库访问和操作。

4. **兼容性与迁移**：
    - 由于包名的变化，迁移到Jakarta EE 9及以上版本时，开发人员需要调整代码中的包名。例如，从`javax.persistence.Entity`改为
      `jakarta.persistence.Entity`。

总的来说，`javax.persistence`和Jakarta Persistence
API实际上代表的是同一套概念和技术规范，不同之处在于品牌的转移和包名的变化。这种变化反映了从Oracle到Eclipse基金会的管理和命名空间的演化。

# Hibernate 和 Spring Data JPA

Hibernate 和 Spring Data JPA 都是与 Java 应用程序中的持久化和数据库访问相关的技术，但它们在功能和用途上有不同之处：

1. **Hibernate**：
    - Hibernate 是一个成熟的 ORM（对象关系映射）框架，它帮助开发者将 Java 对象与数据库表映射起来。它处理从 Java
      类到数据库表行的映射关系。
    - 提供功能：包括缓存管理、延迟加载、批量操作等。它直接与数据库进行交互，管理实体的生命周期。
    - Hibernate 还支持 HQL（Hibernate Query Language）和 Criteria API 进行复杂查询。

2. **Spring Data JPA**：
    - Spring Data JPA 是 Spring Data 家族的一部分，它基于 JPA（Java Persistence API）规范进行开发，旨在简化数据持久层的实现。
    - 它本身不是一个 ORM 框架，而是对于 JPA 的抽象和简化。它提供了一个更高层次的抽象，让开发者无需编写大量样板代码即可实现数据访问。
    - Spring Data JPA 可以与任何实现 JPA 规范的提供程序一起使用，Hibernate 就是最常用的一个实现。也就是说，Spring Data JPA
      通常是在底层使用 Hibernate 作为 JPA 的实现。

简单来说，Hibernate 是一个具体的 ORM 框架，而 Spring Data JPA 是一个更高层次的抽象库，方便开发者使用 JPA 的一些特性。Spring
Data JPA 常常在内部使用 Hibernate 来实际执行 ORM 的任务，从而让开发者能够专注于业务逻辑的实现，而无需关注复杂的持久化层细节。