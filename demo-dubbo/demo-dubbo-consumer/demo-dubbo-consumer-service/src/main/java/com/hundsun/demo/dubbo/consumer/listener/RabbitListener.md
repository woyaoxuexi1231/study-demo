`@RabbitListener` 是 Spring AMQP 提供的注解，用于标识一个方法作为消息监听器。该注解提供了多个参数，用于配置消息监听器的行为和属性。以下是一些常用的参数及其详细解释：

1. **queues**：指定监听的队列名，可以是一个字符串数组，用于指定多个队列。例如：`@RabbitListener(queues = "myQueue")` 或 `@RabbitListener(queues = {"queue1", "queue2"})`。

2. **containerFactory**：指定 `MessageListenerContainer` 的工厂类，用于创建监听器容器。可以通过 `@RabbitListenerContainerFactory` 注解定义自定义的容器工厂，然后在 `@RabbitListener` 注解中使用该工厂。例如：`@RabbitListener(queues = "myQueue", containerFactory = "myContainerFactory")`。

3. **id**：指定监听容器的唯一标识符，用于区分不同的监听容器。

4. **autoStartup**：指定是否在启动时自动启动监听容器，默认为 `true`。如果设置为 `false`，则需要手动调用 `start()` 方法来启动监听容器。

5. **concurrency**：指定并发消费者的数量。可以是一个固定的数量，也可以是一个范围（例如 "3-5" 表示在3到5个并发消费者之间动态调整）。默认为 "1"。

6. **exclusive**：指定是否为独占模式，即是否只允许一个消费者监听该队列。默认为 `false`。

7. **priority**：指定消费者的优先级。高优先级的消费者将在低优先级的消费者之前接收消息。默认为 `0`。

8. **admin**：指定 `RabbitAdmin` 实例，用于在需要创建队列时自动声明队列。

9. **bindings**：指定队列和交换机之间的绑定关系。可以定义多个 `@QueueBinding` 注解，每个注解表示一个绑定关系。

10. **exclude**：指定不包括的异常类型数组。当抛出指定的异常时，消息将被丢弃而不会重试。例如：`@RabbitListener(queues = "myQueue", exclude = {SomeException.class, AnotherException.class})`。

11. **returnExceptions**：指定在消息无法被路由到队列时，应该抛出的异常类型数组。默认为 `AmqpRejectAndDontRequeueException.class`。

12. **defaultRequeueRejected**：指定是否默认情况下重新排队被拒绝的消息。默认为 `true`。

13. **errorHandler**：指定消息处理异常时的错误处理器，可以是一个实现了 `MessageListenerErrorHandler` 接口的类。

14. **group**：用于设置消费者组的名称。具有相同组名的消费者将共享处理消息的工作。

15. **useDefaultContainerFactory**：指定是否使用默认的容器工厂。如果设置为 `true`，则使用默认的容器工厂；如果设置为 `false`，则需要通过 `containerFactory` 属性指定自定义的容器工厂。

这些参数提供了丰富的配置选项，允许开发人员根据实际需求来配置消息监听器的行为。