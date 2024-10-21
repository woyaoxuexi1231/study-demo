spring默认支持配置单个数据源和多个数据源

- 单数据源形式通过 spring.datasource.xxx 进行配置
- 多数据源需要手动实现,spring多数据源的实现基于ThreadLocal来实现,spring通过在线程内设置线程的数据源标记来确定当前线程在获取数据库连接的时候具体使用哪个数据源

要使用spring多数据源需要准备如下东西:

1. 多个数据源需要进行真实的配置
2. 多个数据源如何进行区分和切换

实现的方式也有两种

1. 数据库的切换基于代码内
2. 数据库的切换基于注解形式

主要的流程如下

1. 准备工作:
    - 一个枚举类,用于标识各个数据源,这个标记也就是ThreadLocal的类型参数
    - 一个用于切换线程标记的工具类,这个工具类的用途可以是在程序内部任何需要进行切换的地方使用,也可以是基于注解的形式时作切换使用
    - 一个实现了spring提供的实现多数据源的类 **AbstractRoutingDataSource** 类, 这个类将作为spring名为dataSource的bean的替换,
      这个类的内部提供一个 targetDataSource保存真实的多个数据源,targetDataSource是一个Map,key为我们定义的枚举类,value则为真实的数据源配置.
      这个类需要实现 determineCurrentLookupKey 这个方法,在spring执行数据库操作时,获取连接时会调用这个方法来获取当前线程的数据源标记
      还需要设法在在这个实现类的内部维护一个线程标记与真实数据库的对应关系的map,这个map则是 AbstractRoutingDataSource 的
      targetDataSource,这个可以在afterPropertiesSet方法内实现,设置完成之后调用一下父类的afterPropertiesSet方法来验证一下map是否合法,以及targetDataSource和resolvedDataSources的转换
    - 如果使用注解形式来进行切换数据库的操作,那么还需要准备一个基于注解的切面,然后注解的属性设置为枚举类的值,切面的逻辑则为捕获相关需要进行切换的方法,然后执行切换线程标记的工具的方法
2. 整体流程:
    1. 一个需要db的请求到达后端
    2. 后端接受请求后,在访问db前,首先进行数据库的线程标记切换
    3. spring获取数据库连接依次会调用 getConnect() -> determineTargetDataSource() -> determineCurrentLookupKey()
       方法来获取当前线程的数据库标记,
       determineTargetDataSource方法会根据determineCurrentLookupKey返回的key在resolvedDataSources这个map内获取真实的数据库连接,resolvedDataSources这个map是由我们设置的targetDataSource得来的
    4. spring通过这个线程标记获取真实的数据库连接,然后执行db请求,最终返回
