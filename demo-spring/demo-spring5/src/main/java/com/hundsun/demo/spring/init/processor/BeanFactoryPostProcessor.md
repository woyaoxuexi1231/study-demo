BeanFactoryPostProcessor - spring的后置处理器
BeanFactoryPostProcessor 是 Spring 的一种扩容机制, 该机制允许我们在容器实例化对象之前, 对注册到容器的 BeanDefinition 所保存的信息做相应的修改

BeanPostProcessor 会处理容器内所有符合条件的实例化后的实例对象, 通常的场景是处理标记接口实现类, 或者为当前对象提供 *代理对象
ApplicationContext 对应的那些 Aware 接口实际上就是通过 BeanPostProcessor 的方式进行处理的

当 ApplicationContext 中每个对象的实例化过程走到 BeanPostProcessor 前置处理这一步时,
ApplicationContext 容器会检测到之前注册到容器的 ApplicationContextAwareProcessor 这个 BeanPostProcessor 的实现类,
然后就会调用其 postProcessBeforeInitialization()方法, 检查并设置 Aware 相关依赖

---

`BeanFactoryPostProcessor` 是 Spring 框架中的一个接口，它允许开发者编写自己的代码去修改应用程序上下文的 bean 定义，这个修改是在 Spring 容器实例化任何 bean 之前执行的。

通过实现 `BeanFactoryPostProcessor` 接口，开发者可以读取配置元数据（Bean 定义），并可以按需修改它。这使得开发者能够自定义和修改 Spring 容器的配置，比如更改属性值、修改 bean 的作用域，或者增加和删除某些 bean 定义。

`BeanFactoryPostProcessor` 在 Spring 容器的启动过程中被调用，在所有的 bean 定义将要被加载，但在 bean 被实例化之前。这给了开发者一个修改应用程序配置的强大工具，但也需要谨慎使用，因为它会影响到整个容器的配置。

总之，`BeanFactoryPostProcessor` 提供了一种方式来编程修改 bean 定义，在容器实例化 beans 之前。这可以用于多种需求，如调整配置、开启或关闭特定的功能等。

---

`configurableListableBeanFactory.ignoreDependencyInterface(IgnoreAware.class);`
这行代码是在使用Spring框架中的高级功能，具体来说，它是用于告诉Spring容器在自动装配（autowire）过程中忽略某些接口类型的依赖。

在Spring框架中，`ConfigurableListableBeanFactory` 是一个强大的工厂类，用于管理Bean的定义、创建、装配、获取等。它提供了细粒度的控制，包括能够修改Bean定义以及初始化过程中忽略某些自动装配的依赖。

当你调用 `ignoreDependencyInterface` 方法时，你可以指定一个接口（此处为 `IgnoreAware`
），之后Spring容器在自动装配Bean时，如果遇到了实现了这个接口的类，它将不会考虑这个接口的自动装配依赖。这通常用于某些特殊场景，比如当你想要控制某些自动装配的行为，或者避免由于自动装配导致的循环依赖。

简而言之，这段代码是Spring的高级应用，用于自定义Spring容器的自动装配行为，避免在某些情况下的依赖性注入问题。

---

`configurableListableBeanFactory.registerResolvableDependency` 方法是Spring框架中的一个功能，它允许开发者在Spring容器中注册自定义的依赖解析。这意味着可以动态地添加可以自动装配的依赖，而无需在容器的Bean定义中显式配置它们。

具体来说，这个方法允许你为特定类型的依赖注入自定义的解析策略。这是通过注册一个与依赖类型相匹配的对象来完成的。当Spring容器需要自动装配这个类型的依赖时，它将使用你提供的对象作为依赖的值。

这个功能特别有用在以下情况：

- 当你想要为特定类型提供一个共享实例，而不是每次注入时都创建一个新实例。
- 当需要注入的依赖并不是简单的Bean，可能是需要通过某种特定方式计算或获取的对象，例如，从一个外部服务获取的数据，或是需要根据环境不同而变化的配置值。
- 当你希望Spring管理的Bean之外的对象能够被注入到Spring管理的Bean中，而这些对象的创建不受Spring控制时。

使用 `registerResolvableDependency` 方法可以增加应用的灵活性和动态性，使得Spring容器更加强大和可配置。然而，这也要求开发者对Spring的工作原理有深入的理解，以确保正确使用这一高级功能，避免潜在的问题。

