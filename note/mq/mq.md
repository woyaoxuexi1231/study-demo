### RabbitMQ 中的 broker 是指什么?cluster 又是指什么?

在 RabbitMQ 中，"broker" 是指 RabbitMQ 服务器的实例，负责处理消息的传输和路由。它是消息代理的核心组件，负责接收、存储、传递和传输消息，同时提供了多种消息传输协议和可靠的消息传递机制。

RabbitMQ 的 "cluster" 则指的是多个 RabbitMQ broker 节点组成的集群。通过在多个节点之间共享队列、交换机和消息，RabbitMQ 集群可以提高消息的可靠性、可用性和伸缩性。集群中的各个节点可以相互通信，共同管理队列和消息，使得整个系统具有更强大的性能和可靠性。

在 RabbitMQ 集群中，每个节点都有自己的独立配置和存储，但它们共享相同的虚拟主机（Virtual Host）、用户和权限信息。通过将多个节点组成集群，可以实现消息的负载均衡和故障转移，从而提高系统的稳定性和可靠性。

### RabbitMQ集群

RabbitMQ集群具有以下特点：

1. **高可用和负载均衡**：当单台RabbitMQ服务处理消息的能力到达瓶颈时，可以通过集群来实现高可用和负载均衡¹。

2. **节点类型**：在RabbitMQ集群中，节点类型可以分为两种：内存节点和磁盘节点¹。

3. **集群模式**：RabbitMQ中的集群主要有两种模式：普通集群模式和镜像队列模式¹。

4. **数据同步**：在普通集群模式下，集群中各个节点之间只会相互同步元数据，也就是说，消息数据不会被同步¹。而在镜像队列模式下，节点之间不仅仅会同步元数据，消息内容也会在镜像节点间同步，可用性更高¹。

5. **高可用集群**：可以通过Keepalived和HAProxy两个组件实现了集群的高可用性和负载均衡功能²。

6. **多活模式**：这种模式需要依赖rabbitmq的federation插件，可以实现继续的可靠AMQP数据通信²。

7. **元数据共享**：集群节点之间共享元数据，不共享队列数据 (默认)⁴。

8. **数据互相转发**：RabbitMQ节点数据互相转发，客户端通过单一节点可以访问所有数据⁴。

RabbitMQ集群并非能保证消息万无一失,即使消息,队列,交换机等都设置为可持久化,客户端和服务端也都正确的使用了确认方式.当集群中的一个rabbitMQ节点崩溃时,该节点上所有的消息也会丢失.rabbitMQ集群中的所有节点会同步和备份所有的元数据信息.

在RabbitMQ中，"元数据"主要指的是以下几种信息¹³：

1. **队列元数据**：包括队列名称和它的属性³。
2. **交换器元数据**：包括交换器名称、类型和属性³。
3. **绑定元数据**：一张简单的表格展示了如何将消息路由到队列³。
4. **vhost元数据**：为vhost内的队列、交换器和绑定提供命名空间和安全属性³。

因此，当用户访问其中任何一个RabbitMQ节点时，通过rabbitmqctl查询到的queue／user／exchange/vhost等信息都是相同的³。这就是RabbitMQ在普通模式下同步元数据的含义。

### 普通集群模式

- **在普通集群下,同一个队列只要一个节点创建之后,其他节点就不会重复创建了吗?**  
  在RabbitMQ集群中，当一个队列在某个节点（例如node1）上被创建后，这个队列的元数据（包括队列名称、属性等）会被同步到集群中的所有其他节点（例如node2）¹²。因此，即使你在node2上尝试创建同名的队列，RabbitMQ会识别出这个队列已经存在，因此不会重复创建¹²。
  然而，需要注意的是，虽然队列的元数据在集群中的所有节点上都是可用的，但在普通模式下，队列中的消息数据是不会在节点之间同步的¹²。也就是说，如果一个消息被发布到node1上的队列，那么这个消息默认情况下只存在于node1上，除非有消费者从node2上消费这个队列，这时node2会从node1拉取消息¹²。
- **我现在有一个node1和node2的rabbitmq集群. 有一个test交换机, 我在node2上面创建了一个test的queue的队列, 我在node1往test上面发消息,node2的queue队列上的消费者能收到消息吗?**  
  是的，node2上的消费者可以收到消息。在RabbitMQ集群中，交换器（Exchange）的元数据是在所有节点上同步的¹。当你在node1上向test交换器发送消息时，RabbitMQ会根据交换器的路由规则将消息路由到相应的队列¹。即使队列是在node2上创建的，消息仍然可以被路由到那里¹。
  然后，node2上的消费者可以从该队列中获取并消费这些消息¹。这是因为消费者和队列之间的交互是在队列所在的节点上进行的¹。
  所以，即使你在node1上发布消息，只要消息被路由到node2上的队列，node2上的消费者就可以收到这些消息¹。
    - 如果此时node2发生宕机:
        1. **队列将不可用,队列的状态会变成down**
        2. **新的服务会因为队列的状态而无法启动**,服务会报错channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - home node '
           rabbit@node-129' of durable queue 'topic-queue-slave' in vhost '/' is down or inaccessible, class-id=50, method-id=10)
        3. **消息生产者的消息将变得不可达,在到达交换机之后将被丢弃**
    - node2重启后,队列恢复可用
    - 如果node2是内存节点
        1. 那么这个节点上的所有信息都将丢失,如果信息是持久化的,那么节点重启后是可以恢复的(包括队列内的消息)
        2. 如果队列信息是非持久化的,那么node宕机后,数据都将丢失,但是其他节点就可以重新声明这个队列了

### RabbitMQ 概念里的 channel、exchange 和 queue 这些是逻辑概念，还是对应着进程实体？这些别起什么作用？

在 RabbitMQ 中，channel、exchange 和 queue 是逻辑概念，它们各自扮演不同的角色¹²³⁴⁵：

- **Channel（通道）**：Channel 是在客户端和 RabbitMQ 之间建立的虚拟连接，用于建立和管理 Exchange 和 Queue，以及在它们之间传递消息。每个 Channel 都拥有自己的资源和缓存，因此使用多个 Channel 可以提高 RabbitMQ
  的并发性能³。Channel 是实际进行路由工作的实体，即负责按照 routing_key 将 message 投递给 queue⁵。

- **Exchange（交换机）**：Exchange 是消息交换机，接收生产者发送的消息并根据路由规则将消息分发到一个或多个队列。RabbitMQ 提供了四种类型的 Exchange：Direct、Topic、Fanout 和 Headers³。Exchange 根据什么规则把消息投递到哪些
  queue 中？这取决于 Exchange 的类型²。

- **Queue（队列）**：Queue 是消息在 RabbitMQ 中的存储区，它接收 Exchange 路由过来的消息，并将其存储起来，等待消费者来消费。Queue 可以绑定多个 Exchange，也可以由多个消费者来消费。在 Queue 中，消息遵循先进先出（FIFO）的原则³。Queue
  具有自己的 erlang 进程⁵。

这些概念都是 RabbitMQ 中的核心组件，它们共同协调以实现消息的生产、路由和消费⁴。

### 能够在地理上分开的不同数据中心使用 RabbitMQ cluster 么？ TODO

### 为什么 heavy RPC 的使用场景下不建议采用 disk node ？

"heavy RPC"是指在业务逻辑中高频调用 RabbitMQ 提供的 RPC 机制，这会导致不断创建、销毁 reply queue，从而对 disk node 产生性能问题。这是因为这种操作会导致针对元数据的频繁磁盘写入¹²。因此，在使用 RPC 机制时，需要根据自身的业务场景进行考虑¹²。

在重型 RPC 场景下，频繁地进行磁盘 I/O 操作可能会对性能产生较大影响，因此不建议采用磁盘节点（disk node）。

在 RabbitMQ 中，磁盘节点会将消息持久化到磁盘上，以确保消息的持久性和可靠性。这意味着每次发送和接收消息时都需要将消息写入磁盘，或者从磁盘中读取消息，这会导致大量的磁盘 I/O 操作。而在重型 RPC 场景下，消息的发送和接收频率可能会非常高，如果频繁进行磁盘 I/O
操作，会严重影响系统的响应速度和吞吐量。

相比之下，内存节点（memory node）通常会将消息存储在内存中，能够提供更高的性能和吞吐量。在 RPC 场景下，如果消息的持久性要求不高，可以考虑使用内存节点来避免频繁的磁盘 I/O 操作，从而提高系统的性能和响应速度。

### 什么是 Blackholed,什么情况下会发生,怎么避免?

"Blackholed"在RabbitMQ中是指，向exchange投递了message，但由于某些原因导致该message丢失，而发送者却不知道²⁶。以下是可能导致"blackholed"的情况：

1. 向未绑定queue的exchange发送message²⁶。
2. exchange以binding_key key_A绑定了queue queue_A，但向该exchange发送message使用的routing_key却是key_B²⁶。

避免"blackholed"问题的方法主要有：

1. 在具体实践中通过各种方式保证相关fabric的存在²。
2. 如果在执行Basic.Publish时设置mandatory=true，则在遇到可能出现"blackholed"情况时，服务器会通过返回Basic.Return告之当前message无法被正确投递（内含原因312
   NO_ROUTE）²。这样可以让发送者知道message没有被正确投递，从而采取相应的措施。

### 


### 关于spring配置集群的问题

像这样配置多个地址
spring.rabbitmq.addresses=192.168.80.128:5672,192.168.80.129:5672

提供了多个地址，那么 Spring Boot 会尝试按照你提供的顺序去连接这些地址。也就是说，如果第一个地址的 RabbitMQ 服务器无法连接，那么 Spring Boot 会尝试连接第二个地址，以此类推。

但是，需要注意的是，这并不意味着 Spring Boot 会与所有的 RabbitMQ 服务器建立连接。实际上，Spring Boot 只会与能够成功连接的第一个 RabbitMQ 服务器建立连接。如果这个连接断开，Spring Boot 会再次尝试按照你提供的顺序去连接这些地址。

### 消息重复消费的解决方案

### 消息幂等性(需要业务代码具备这种幂等性操作)

### 消息的有序性(单一消费者模式,kafka分区顺序,RocketMQ顺序消息队列)