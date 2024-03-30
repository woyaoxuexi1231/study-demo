### 什么是redis

Redis 本质上是一个 Key-Value 类型的内存数据库，很像 memcached，整个数据库统统加载
在内存当中进行操作，定期通过异步操作把数据库数据 flush 到硬盘上进行保存。

1. **内存数据库架构**：
    - Redis 是一个内存数据库，将整个数据库加载到内存中进行操作，这使得其能够实现非常快速的读写操作。
    - 数据库定期通过异步操作将数据刷新到硬盘上进行保存，以防止数据丢失。

2. **优异性能**：
    - Redis 的性能出色，每秒可以处理超过 10 万次读写操作，是已知性能最快的 Key-Value 数据库。
    - 这种高性能主要归因于其纯内存操作和单线程模型的设计。

3. **多种数据结构支持**：
    - Redis 支持多种数据结构，包括字符串、列表、集合、哈希、有序集合等。
    - 允许单个 value 的最大限制为 1GB，这比起 memcached 的 1MB 更加灵活。

4. **功能丰富**：
    - 可以利用 Redis 的不同数据结构来实现各种功能，如使用列表实现 FIFO 双向链表，使用集合实现高性能的标签系统等。
    - Redis 还支持为存储的 Key-Value 设置过期时间，可以作为功能更强大的 memcached 使用。

5. **容量限制**：
    - Redis 的数据库容量受到物理内存的限制，不适合处理海量数据的高性能读写。
    - 主要适用于处理较小数据量的高性能操作和计算场景。

### 为什么redis单线程架构还能那么快

尽管 Redis 是单线程架构，但它之所以能够达到如此出色的性能主要是由于以下几个方面的原因：

1. **非阻塞 I/O**：Redis 使用了非阻塞 I/O 操作，这意味着当一个客户端发送请求时，Redis 可以继续处理其他客户端的请求而不必等待 I/O 操作完成。这种异步处理方式允许 Redis 在一个线程上同时处理多个请求，提高了系统的吞吐量。

2. **基于内存的数据存储**：Redis 将数据存储在内存中，而内存的读写速度远远快于磁盘。这意味着 Redis 可以以非常低的延迟读取和写入数据，从而提高了整体的性能。

3. **优化的数据结构和算法**：Redis 使用了高效的数据结构和算法来实现其各种功能，例如使用哈希表来实现键值对存储，使用跳表来实现有序集合等。这些数据结构和算法的选择使得 Redis 在执行各种操作时具有高效性能。

4. **单线程的优势**：虽然 Redis 是单线程架构，但单线程带来了一些优势，例如避免了多线程之间的竞争和同步开销，减少了上下文切换的开销等。此外，由于 Redis 主要是基于内存的操作，单线程也能够满足大部分应用的需求。

综合以上因素，即使是单线程架构，Redis 仍然能够提供出色的性能。当然，在极高并发和大规模数据量的情况下，可能需要通过集群和分片等方式来进一步提高 Redis 的性能和扩展性。

### 什么是非阻塞I/O

非阻塞 I/O（Non-blocking I/O）是一种 I/O 操作的处理模式，它允许程序在进行输入输出操作时不被阻塞，从而使得程序能够同时处理多个任务或请求。

在传统的阻塞 I/O 模式中，当一个 I/O 操作被调用时，程序会一直等待，直到操作完成或者超时。这意味着在进行 I/O 操作时，程序无法执行其他任务，从而可能导致程序的性能下降。

相比之下，非阻塞 I/O 模式允许程序在进行 I/O 操作时继续执行其他任务，而不必等待操作完成。当一个 I/O 操作被调用时，程序会立即返回，而不会阻塞在那里等待操作完成。程序可以通过轮询或事件驱动的方式来检查 I/O 操作的状态，一旦操作完成，程序就可以继续处理数据或执行其他任务。

非阻塞 I/O 可以通过以下几种方式来实现：

1. **轮询（Polling）**：程序周期性地查询 I/O 设备或文件描述符的状态，以确定是否有数据可读或可写。

2. **选择（Select）**：使用 select() 系统调用或类似的机制来监视多个文件描述符的状态，以确定哪些文件描述符已经准备好进行读取或写入。

3. **多路复用（Multiplexing）**：使用多路复用技术（如 epoll、kqueue 等）来同时监视多个文件描述符的状态，从而避免了轮询和选择的效率问题。

4. **异步 I/O（Asynchronous I/O）**：通过操作系统提供的异步 I/O 接口，在发起一个 I/O 操作后，程序可以立即返回，而不必等待操作完成。当操作完成时，操作系统会通知程序，从而实现了真正的非阻塞 I/O。

非阻塞 I/O 可以提高程序的并发性和吞吐量，特别适用于需要同时处理大量 I/O 操作的场景，如网络服务器、文件系统等。然而，非阻塞 I/O 也会增加程序的复杂性，因为程序需要自行管理 I/O 操作的状态和处理逻辑。

非阻塞 I/O 可以通过多种方式来实现，以下是其中几种常见的实现方式及其特点：

1. **轮询（Polling）**：
    - **实现原理**：程序周期性地查询 I/O 设备或文件描述符的状态，以确定是否有数据可读或可写。
    - **工作方式**：当调用非阻塞的 I/O 操作时，程序会立即返回，然后周期性地轮询检查 I/O 设备或文件描述符的状态。
    - **优点**：简单易实现，适用于较少的并发连接。
    - **缺点**：效率较低，需要频繁地查询状态，可能会造成 CPU 资源浪费。
    - **示例**：使用 Java NIO 的 Selector 类进行轮询式非阻塞 I/O 操作。
   ```
   Selector selector = Selector.open();
   channel.configureBlocking(false);
   channel.register(selector, SelectionKey.OP_READ);
   while (true) {
       int readyChannels = selector.select();
       if (readyChannels == 0) {
           continue;
       }
       Set<SelectionKey> selectedKeys = selector.selectedKeys();
       Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
       while (keyIterator.hasNext()) {
           SelectionKey key = keyIterator.next();
           if (key.isReadable()) {
               // 处理读事件
           }
           keyIterator.remove();
       }
   }
   ```

2. **选择（Select）**：
    - **实现原理**：使用 select() 系统调用或类似的机制来监视多个文件描述符的状态，以确定哪些文件描述符已经准备好进行读取或写入。
    - **工作方式**：通过操作系统提供的 select() 系统调用，程序可以监视多个文件描述符的状态，一旦某个文件描述符可读或可写，程序就可以进行相应的读写操作。
    - **优点**：比轮询方式效率更高，可以同时监视多个文件描述符。
    - **缺点**：对文件描述符的数量有限制，且在高并发场景下可能存在性能瓶颈。
    - **示例**：使用 C 语言的 select() 系统调用进行非阻塞 I/O 操作。
   ```c
   fd_set readfds;
   FD_ZERO(&readfds);
   FD_SET(fd, &readfds);
   while (true) {
       int ready = select(fd + 1, &readfds, NULL, NULL, NULL);
       if (ready == -1) {
           perror("select");
           break;
       }
       if (FD_ISSET(fd, &readfds)) {
           // 处理读事件
       }
   }
   ```

3. **多路复用（Multiplexing）**：
    - **实现原理**：使用多路复用技术（如 epoll、kqueue 等）来同时监视多个文件描述符的状态，从而避免了轮询和选择的效率问题。
    - **工作方式**：多路复用技术允许程序注册多个文件描述符，并在某个文件描述符就绪时得到通知，从而可以立即处理对应的 I/O 操作。
    - **优点**：效率高，支持大量并发连接，适用于高性能网络服务器。
    - **示例**：使用 epoll() 系统调用进行非阻塞 I/O 操作。
   ```c
   int epollfd = epoll_create1(0);
   struct epoll_event event;
   event.events = EPOLLIN | EPOLLET;
   event.data.fd = fd;
   epoll_ctl(epollfd, EPOLL_CTL_ADD, fd, &event);
   while (true) {
       struct epoll_event events[MAX_EVENTS];
       int n = epoll_wait(epollfd, events, MAX_EVENTS, -1);
       for (int i = 0; i < n; ++i) {
           if (events[i].events & EPOLLIN) {
               // 处理读事件
           }
       }
   }
   ```

4. **异步 I/O（Asynchronous I/O）**：
    - **实现原理**：通过操作系统提供的异步 I/O 接口，在发起一个 I/O 操作后，程序可以立即返回，而不必等待操作完成。当操作完成时，操作系统会通知程序。
    - **工作方式**：程序通过调用异步 I/O 接口发起 I/O 操作，并指定操作完成后的回调函数。当操作完成时，操作系统会自动调用回调函数，从而实现异步操作。
    - **优点**：效率高，避免了轮询和选择的开销，适用于高并发和高吞吐量的场景。
    - **缺点**：编程复杂度较高，操作系统支持程度不一，可能在某些平台上无法完全实现。
    - **示例**：使用 Java NIO 的 AsynchronousFileChannel 类进行异步文件读取操作。
   ```
   Path path = Paths.get("file.txt");
   AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
   ByteBuffer buffer = ByteBuffer.allocate(1024);
   channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
       @Override
       public void completed(Integer result, ByteBuffer attachment) {
           System.out.println("Read " + result + " bytes");
       }
       @Override
       public void failed(Throwable exc, ByteBuffer attachment) {
           exc.printStackTrace();
       }
   });
   ```

以上是几种常见的非阻塞 I/O 实现方式及其示例。不同的实现方式适用于不同的场景，具体选择取决于应用程序的需求和平台的支持情况。

### Redis 有哪几种数据淘汰策略?

Redis 提供了多种数据淘汰策略，用于在内存达到设定的最大使用量时清除部分数据，以便为新数据腾出空间。以下是 Redis 中常见的数据淘汰策略：

1. **LRU（Least Recently Used）**：最近最少使用策略。根据数据的访问时间，淘汰最近最少使用的数据。当内存空间不足时，优先淘汰最近最少被访问的数据项。

2. **LFU（Least Frequently Used）**：最不经常使用策略。根据数据的访问频率，淘汰使用频率最低的数据。当内存空间不足时，优先淘汰访问频率最低的数据项。

3. **TTL（Time To Live）**：生存时间策略。当数据设定了过期时间（TTL），在过期时间到达后将被自动淘汰。此策略不受内存空间限制，而是根据数据自身的过期时间来进行淘汰。

4. **Random（随机策略）**：随机淘汰策略。随机选择一些数据进行淘汰，没有特定的顺序或优先级。

5. **Maxmemory Policy = NoEviction**：禁止淘汰策略。当内存达到最大使用量时，新的写操作将会失败，客户端会收到错误响应。这种情况下需要客户端适当处理错误并清理数据以释放空间。

在 Redis 中，可以通过配置 `maxmemory-policy` 参数来选择所使用的淘汰策略。
默认情况下，`maxmemory-policy` 参数为 `volatile-lru`，表示使用 LRU 策略来淘汰设置了过期时间的数据。用户可以根据自己的需求和场景选择合适的淘汰策略。

1. `noeviction`：当内存使用超过配置的时候会返回错误，不会驱逐任何键²⁴。
2. `allkeys-lru`：从所有键中使用LRU算法进行淘汰¹²³⁴。
3. `volatile-lru`：从设置了过期时间的键中使用LRU算法进行淘汰¹²³⁴。
4. `allkeys-random`：从所有键中随机淘汰数据¹²³⁴。
5. `volatile-random`：从设置了过期时间的键中随机淘汰¹²³⁴。
6. `volatile-ttl`：在设置了过期时间的键中，淘汰过期时间剩余最短的¹²³⁴。
7. `volatile-lfu`：从已设置过期时间的数据集挑选使用频率最低的数据淘汰¹⁵。
8. `allkeys-lfu`：从所有键中驱逐使用频率最少的键¹⁵。


