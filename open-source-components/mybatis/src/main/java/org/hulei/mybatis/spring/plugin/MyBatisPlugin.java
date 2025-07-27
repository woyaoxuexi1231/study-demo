package org.hulei.mybatis.spring.plugin;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.Properties;

/*
MyBatis 允许拦截的核心对象有四个，它们是 MyBatis 执行过程中的关键组件：
    - Executor：MyBatis 的核心执行器，负责 SQL 的生成和查询缓存的维护
      可拦截的方法：update、query、flushStatements、commit、rollback、getTransaction、close、isClosed
    - ParameterHandler：处理 SQL 参数的对象
      可拦截的方法：getParameterObject、setParameters
    - ResultSetHandler：处理结果集的对象
      可拦截的方法：handleResultSets、handleOutputParameters
    - StatementHandler：封装 JDBC Statement 操作的对象
      可拦截的方法：prepare、parameterize、batch、update、query

创建一个插件只需要几个步骤就行
 1. 创建拦截器类：实现 org.apache.ibatis.plugin.Interceptor 接口
 2. 添加@Intercepts 注解：声明要拦截的对象和方法
 3. 实现 intercept 方法：添加自定义逻辑
 4. 实现 plugin 方法：创建代理对象
 5. 实现 setProperties 方法：获取配置参数
 6. 在 MyBatis 配置文件中注册插件（springboot就不需要，直接声明成bean就行）


这四个关键组件执行一条sql时 UserMapper.selectUserById(id)，流程如下：
  1. MapperProxy 调用 Executor.query()
  2. Executor 查缓存 -> 无缓存 -> 调用 StatementHandler
  3. StatementHandler.prepare(Connection) 创建 PreparedStatement
  4. 调用 ParameterHandler.setParameters(PreparedStatement) 设置参数
  5. StatementHandler.query(PreparedStatement) 执行 SQL
  6. ResultSetHandler.handleResultSets(ResultSet) 处理返回数据
  7. Executor 返回结果给 MapperProxy
配置插件后，会由 public Object plugin(Object target) 这个方法来生成拦截的对应的接口的代理对象
后续在第一步执行时，会在代理对象内执行操作，判断是否有拦截此方法的插件，然后通过插件来增强执行

源码中 org.apache.ibatis.session.Configuration.newExecutor(org.apache.ibatis.transaction.Transaction, org.apache.ibatis.session.ExecutorType)
这个方法可以看到，executor 和 插件代理对象在每次请求都会重新创建
* 由于 SqlSession 是线程不安全的，所以不管使用哪种方式，手动开启 SqlSession 或是直接使用 mapper 查询
  两种方式都会基于当前的请求(或者说是当前线程)重新创建一个 sqlSession
  SqlSession 内部维护了“状态”，而状态是线程不隔离的。
    - Executor 持有一级缓存、事务对象、执行上下文
    - LocalCache 一级缓存（作用域：SqlSession）
    - Transaction 当前事务对象，和连接绑定
    - JDBC Connection 持久化连接，如果多个线程同时用，会出错
  所以如果共享 sqlSession，会导致内部状态互相污染，造成并发问题
  * 所以这也同样意味着：代理对象的创建也是每次都会重新创建
 */

// @Component
@Intercepts({
        @Signature(
                type = Executor.class,          // 拦截 Executor 接口
                method = "query",              // 拦截 query 方法
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        ),
        @Signature(
                type = Executor.class,          // 拦截 Executor 接口
                method = "query",              // 拦截 query 方法
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
        )
})
public class MyBatisPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 1. 获取拦截方法的参数
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];

        // 2. 执行前的逻辑（如打印 SQL、修改参数等）
        System.out.println("拦截的 SQL ID: " + mappedStatement.getId());
        System.out.println("参数: " + parameter);

        // 3. 执行原方法（继续执行 SQL）
        Object result = invocation.proceed();

        // 4. 执行后的逻辑（如修改返回结果）
        System.out.println("执行结果: " + result);

        return result;
    }

    @Override
    public Object plugin(Object target) {
        /*
        使用 Plugin.wrap 生成代理对象，这是 mybatis 默认提供的代理对象的生成方式
        使用 Proxy.newProxyInstance jdk的代理方式生成代理对象
        Proxy.newProxyInstance(类加载器，目标接口，代理增强类)
         */
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 从 mybatis-config.xml 中读取插件配置
        System.out.println("插件配置: " + properties);
    }
}