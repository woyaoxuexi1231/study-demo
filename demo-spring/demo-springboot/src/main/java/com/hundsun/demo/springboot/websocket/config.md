`WebSocketMessageBrokerConfigurer`和`WebSocketConfigurer`是Spring框架中用于配置WebSocket的两个接口，它们在WebSocket配置中扮演着不同的角色，并且服务于Spring框架中不同层次的WebSocket支持。

### `WebSocketConfigurer`

- **层次**：更接近底层。
- **用途**：用于配置原生WebSocket的端点。如果你正在直接使用WebSocket API（例如使用`@ServerEndpoint`注解），并且希望通过Spring来配置WebSocket的端点，那么你可能会实现`WebSocketConfigurer`接口。
- **实现方式**：通过实现`WebSocketConfigurer`接口并重写`registerWebSocketHandlers`方法，开发者可以注册WebSocket的处理器，并且定义URL的映射路径。这允许直接使用WebSocket协议。

### `WebSocketMessageBrokerConfigurer`

- **层次**：更高级、更抽象。
- **用途**：用于配置基于消息代理的WebSocket通讯。`WebSocketMessageBrokerConfigurer`是为了支持更复杂的消息传递场景设计的，比如使用STOMP协议（Simple Text Oriented Messaging
  Protocol）来作为WebSocket的子协议，允许通过消息代理来进行广播或者与特定用户通讯。
- **实现方式**：通过实现`WebSocketMessageBrokerConfigurer`接口并重写相关的配置方法（例如`configureMessageBroker`、`registerStompEndpoints`
  等），开发者可以定义消息代理的行为、配置端点、设置消息的前缀等。这种方式允许使用更高级的消息传递模式，如发布/订阅。

### 关系与区别

- **关系**：它们都是Spring框架中用于配置WebSocket的接口，但是服务于不同的使用场景和需求。`WebSocketConfigurer`更多地面向底层的WebSocket配置，而`WebSocketMessageBrokerConfigurer`面向基于消息代理的高级特性。
- **区别**：主要在于它们配置的层次和复杂性不同。`WebSocketConfigurer`适合那些需要直接控制WebSocket连接和处理的场景，而`WebSocketMessageBrokerConfigurer`适合需要通过消息代理进行复杂消息传递模式的应用场景。

根据你的具体需求，你可能会选择实现其中的一个或者两者都不使用，直接利用Spring提供的注解和自动配置来简化WebSocket的开发。