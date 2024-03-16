# dubbo 通信协议 dubbo 协议为什么要消费者比提供者个数多?
dubbo 使用单一长连接, 当服务上线或者下线,会订阅或者取消订阅这个消费者.通过日志也可以看到  
`` INFO [main-EventThread] o.a.d.r.z.ZookeeperRegistry.notify(407) :  [DUBBO] Notify urls for subscribe url consumer://192.168.80.1/com.hundsun.demo.dubbo.provider.api.service.ProviderService?application=demo-dubbo-consumer&category=providers,configurators,routers&check=false&cluster=failsafe&dubbo=2.0.2&init=false&interface=com.hundsun.demo.dubbo.provider.api.service.ProviderService&loadbalance=roundrobin&metadata-type=remote&methods=RpcInvoke&pid=20316&qos.enable=false&release=2.7.8&side=consumer&sticky=false&timeout=30000&timestamp=1710519547651, urls: [dubbo://192.168.80.1:20100/com.hundsun.demo.dubbo.provider.api.service.ProviderService?anyhost=true&application=demo-dubbo-provider&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&interface=com.hundsun.demo.dubbo.provider.api.service.ProviderService&metadata-type=remote&methods=RpcInvoke&pid=7752&release=2.7.8&side=provider&timestamp=1710521177820&token=admin], dubbo version: 2.7.8, current host: 192.168.80.1``  
当第一次调用服务提供者提供的服务时,可以看到消费者会与服务提供者建立连接  
``24-03-16 00:46:54.991 INFO  [http-nio-9000-exec-3] o.a.d.r.p.d.LazyConnectExchangeClient.initClient(77) :  [DUBBO] Lazy connect to dubbo://192.168.80.1:20100/com.hundsun.demo.dubbo.provider.api.service.ProviderService?_client_memo=referencecounthandler.replacewithlazyclient&anyhost=true&application=demo-dubbo-consumer&check=false&cluster=failsafe&codec=dubbo&connect.lazy.initial.state=true&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&heartbeat=60000&init=false&interface=com.hundsun.demo.dubbo.provider.api.service.ProviderService&lazyclient_request_with_warning=true&loadbalance=roundrobin&metadata-type=remote&methods=RpcInvoke&pid=20316&qos.enable=false&reconnect=false&register.ip=192.168.80.1&release=2.7.8&remote.application=demo-dubbo-provider&send.reconnect=true&side=consumer&sticky=false&timeout=30000&timestamp=1710519518589&token=admin&warning=true, dubbo version: 2.7.8, current host: 192.168.80.1``  
``24-03-16 00:46:55.010 INFO  [NettyClientWorker-1-2] o.a.d.r.t.n.NettyClientHandler.channelActive(62) :  [DUBBO] The connection of /192.168.80.1:9289 -> /192.168.80.1:20100 is established., dubbo version: 2.7.8, current host: 192.168.80.1``   
``24-03-16 00:46:55.010 INFO  [http-nio-9000-exec-3] o.a.d.r.t.AbstractClient.connect(200) :  [DUBBO] Successed connect to server /192.168.80.1:20100 from NettyClient 192.168.80.1 using dubbo version 2.7.8, channel is NettyChannel [channel=[id: 0x2fb817dc, L:/192.168.80.1:9289 - R:/192.168.80.1:20100]], dubbo version: 2.7.8, current host: 192.168.80.1``  
``24-03-16 00:46:55.011 INFO  [http-nio-9000-exec-3] o.a.d.r.t.AbstractClient.<init>(73) :  [DUBBO] Start NettyClient /192.168.80.1 connect to the server /192.168.80.1:20100, dubbo version: 2.7.8, current host: 192.168.80.1``
 因 dubbo 协议采用单一长连接，假设网络为千兆网卡(1024Mbit=128MByte), 根据测试经验数据每条连接最多只能压满 7MByte(不同的环境可能不一样，供参考)，理论上 1 个服务提供者需要 20 个服务消费者才能压满网卡。
---

# Rpc协议是什么

RPC 协议被设计成远程调用就像是本地调用一样，其具体实现包括以下几个主要步骤：

1. **接口定义**: 首先，需要定义远程服务的接口，包括服务的方法名称、参数类型和返回值类型等。这个接口可以使用通用的接口定义语言（IDL）进行定义，例如 Protocol Buffers、Thrift 的 IDL 等。

2. **序列化与反序列化**: 在进行远程调用时，需要将方法调用的参数和返回值进行序列化（编码）成字节流，并在远程端进行反序列化（解码）成对应的数据结构。常见的序列化工具包括 Protocol Buffers、JSON、XML 等。

3. **通信协议**: 选择合适的通信协议进行数据传输。常见的通信协议包括 HTTP、TCP、UDP 等。通常情况下，RPC 框架会使用 TCP 协议来实现可靠的数据传输。

4. **网络通信**: 客户端通过网络将序列化后的请求数据发送给服务端，服务端接收到请求数据后进行处理，并将处理结果序列化后返回给客户端。

5. **错误处理**: 在远程调用过程中，可能会出现各种错误，例如网络故障、服务端异常等。因此，RPC 框架需要提供相应的错误处理机制，以确保调用的可靠性和稳定性。

6. **负载均衡和容错**: 在实际的分布式环境中，可能会有多个服务提供者提供同一个服务，RPC 框架需要提供负载均衡和容错机制，以确保请求能够被合理地分发和处理。

综上所述，RPC 协议的实现涉及到接口定义、序列化与反序列化、通信协议、网络通信、错误处理、负载均衡和容错等多个方面，通过这些步骤可以将远程调用封装成类似于本地调用的方式，从而简化分布式系统的开发和维护。不同的 RPC 框架可能有不同的实现细节和特性，但基本的原理和步骤是相似的。

----

# dubbo服务治理篇
## 什么是服务降级
服务降级是一种分布式系统设计中的策略，用于在服务不可用或性能下降时，以一种有限但可控的方式维持系统的可用性和稳定性。服务降级通常在以下情况下发挥作用：

1. **服务不可用**: 当服务因为网络故障、硬件故障或其他原因导致不可用时，降级策略可以保证系统的其他部分仍然可以正常运行，而不会因为单个服务的故障而导致整个系统崩溃。

2. **服务性能下降**: 当服务因为负载过高、资源不足或其他原因导致性能下降时，降级策略可以通过限制资源使用或调整服务响应行为来减轻服务的负载，从而保证系统的整体性能不受影响。

服务降级的具体实现方式包括以下几个方面：

1. **限流**: 限制对服务的请求流量，避免过多的请求导致服务的负载过高。可以通过配置最大并发数、最大请求速率等参数来控制流量。

2. **返回默认值或缓存数据**: 当服务不可用时，可以返回一个默认值或者使用缓存数据代替实时数据，以确保系统的正常运行。

3. **熔断**: 当服务的响应时间超过一定阈值或者出现一定比例的错误时，可以触发熔断机制，暂时停止对该服务的请求，避免继续向故障的服务发起请求，减少资源浪费和系统负载。

4. **降级页面**: 当服务不可用时，可以向用户返回一个友好的降级页面，提示用户当前服务不可用，并提供一些备选方案或者帮助信息。

通过合理使用服务降级策略，可以提高系统的可用性和稳定性，保证系统在面对异常情况时能够以一种有限但可控的方式继续提供服务，而不会因为局部故障而导致整个系统的崩溃。

