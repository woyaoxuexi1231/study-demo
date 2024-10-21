# 概要介绍

## 简要介绍一下 JVM

Java一次编写,到处运行
跨平台的 jvm会负责在不同操作系统上帮我们把class文件翻译成对应的指令
语言无关: 只要是class文件就行

# 内存管理

## 内存分区

### JVM 运行时数据区

JVM的运行时数据区主要包括以下几个部分：

1. **方法区（Method Area）**：
    - 方法区是线程共享的内存区域，用于存储类信息、常量、静态变量、即时编译器编译后的代码等数据。
    - 在HotSpot虚拟机中，方法区被称为永久代（Permanent Generation），但在Java 8后被元空间（Metaspace）取代。

2. **堆（Heap）**：
    - 堆是JVM管理的最大一块内存区域，被所有线程共享，用于存储对象实例。
    - 堆空间在Java程序启动时被创建，用于存放Java应用程序中几乎所有的对象实例，包括数组和对象。

3. **Java虚拟机栈（Stack）**：
    - 栈是线程私有的，用于存储线程执行方法的局部变量、操作数栈、方法出口等信息。
    - 每个方法在执行时会创建一个栈帧（Stack Frame），用于存储方法的局部变量和部分运算结果。

4. **程序计数器（Program Counter）**：
    - 程序计数器是线程私有的，用于存储当前线程正在执行的字节码指令的地址。
    - 在多线程环境下，程序计数器为每个线程都维护了一个独立的计数器，保证线程切换后能正确恢复到正确的执行位置。

5. **本地方法栈（Native Method Stack）**：
    - 本地方法栈与栈类似，不过是为虚拟机使用到的Native方法服务的。
    - 它也会抛出StackOverflowError和OutOfMemoryError。

线程私有区域: 程序计数器,本地方法栈,虚拟机栈
线程共享区域: 堆(新生代(e,s+s),老年代),方法区(还包含运行时常量池)

### JVM 内存分代

JVM内存分代是一种优化策略，主要用于提升垃圾回收的效率和性能。通常情况下，JVM将堆内存划分为不同的代，每个代有不同的生命周期和垃圾回收算法。

主要的JVM内存分代包括：

1. **新生代（Young Generation）**：
    - 新生代是存放新创建的对象的区域。
    - 一般将新生代分为Eden区和两个Survivor区（通常是From和To区，也可以称为S0和S1）。
    - 大多数对象在新生代被创建并很快被回收，因此新生代使用效率高的复制算法进行垃圾回收（如标记-复制算法）。

2. **老年代（Old Generation）**：
    - 老年代主要用于存放长期存活的对象。
    - 经过多次垃圾回收仍然存活的对象会被移到老年代。
    - 老年代一般使用标记-整理算法或标记-清除-整理算法进行垃圾回收，以便整理出更大的连续空间。

3. **永久代/元空间（Permanent Generation / Metaspace）**：
    - 在较早版本的JVM中，用于存放静态文件、常量、JIT编译器优化后的代码等数据。
    - 从Java 8开始，永久代被元空间取代，元空间使用本地内存来存放类的元数据，有效地减少了永久代的内存限制和垃圾回收压力。

这种分代的管理策略是为了更好地适应不同对象的生命周期和特性，通过不同的垃圾回收算法和优化策略，提升JVM的性能和响应速度。

### jvm内存溢出

* 栈溢出 - 压栈过深了,栈一般来说1000-2000完全够了,jvm默认1m
* 堆溢出 - 堆内对象太多了,又回收不了,就堆溢出了,单纯的虚拟机内存分配不足或者是机器本身就不够
* 方法区溢出
* 本机直接内存溢出

### 什么是内存泄漏,哪些情况可能会导致内存泄漏

原本已经不适用的内存空间,但是却没有释放,并且回收不了.那么就产生了内存泄漏

### 对象的内存布局

首先,一个对象在堆内存的布局大概分为三个部分: 1.对象头,2.示例数据,3.对齐填充
**对象头** 大小在32比特或者64比特
一类是用于存储对象自身运行时的数据(哈希码,GC分代年龄,锁状态标志,线程持有状态,偏向锁ID,偏向时间戳等等)
第二类是类型指针,即对象指向他的类型元数据的指针(
Java虚拟机通过这个来确定这个对象是哪个类的实例,并非所有的虚拟机都必须在对象数据上保存类型指针)
**实例数据** 真正存储对象数据的(会包含父类的)
**对其填充** 对象头被设计成正好是8的倍数,为了满足这个设计,而存在的

### 给一个具体的类,分析对象的内存占用

**基本类型**

在Java中，基本数据类型的大小是固定的，它们所占的字节数如下所示：

byte: 1字节
short: 2字节
int: 4字节
long: 8字节
float: 4字节
double: 8字节
char: 2字节
boolean: 在实际存储时，可能占用不定大小，但一般认为是1字节
这些基本数据类型的大小是根据Java虚拟机规范确定的，这些大小是跨平台的，不会因为不同操作系统或机器架构而有所变化。当然，这些数据类型的大小可能在不同的Java实现中有所不同，但一般遵循上述规定。

**封装的基本类型**

1. 对象头 32位或64位 即4个字节或者八个字节
2. 封装的基本类型和基本类型的大小差不多一致

**对象**

对象的大小计算也就是对象头+所有的字段

如果一个对象内有一个Integer和一个String，String最大长度是100个字符（可能是中文可能是英文），我有100个这样的对象，大概占多大的内存?

1. **Integer 对象**：一个 Integer 对象通常占用 16 字节的对象头，再加上 4 字节的 int 值。所以，一个 Integer 对象的内存开销为
   20 字节。
2. **String 对象**：一个 String 对象通常占用 40 字节的对象头，再加上 一个 char[] 对象引用（8 字节）和 3 个 int 类型字段（总共
   12 字节），以及一个 int 字段（存储字符串长度，4
   字节），再加上 char[]

数组占用的内存空间（根据字符串长度而定）。假设每个字符占用 2 字节，那么一个 String 对象的平均内存开销为：

String 对象的内存开销 = 40 字节（对象头） + 8 字节（char[] 对象引用） + 12 字节（字段） + 4 字节（存储长度） + 100（假设每个字符串长度为
100 个字符） * 2 字节（每个字符占用 2 字节）

String 对象的内存开销 ≈ 40 字节 + 8 字节 + 12 字节 + 4 字节 + 200 字节 = 264 字节

因此，100 个对象中 Integer 和 String 类型的总内存开销为：

100 * (20 字节 + 264 字节) = 28400 字节

再加上对象本身的内存开销，总内存开销为：

1600 字节 + 28400 字节 = 30000 字节

所以，大约占用 30 KB 左右的内存空间。感谢您的指正，希望这次的计算是正确的。

## GC

GC通过检查堆内存中的对象，标记出哪些对象是活动的（即仍在被引用），然后回收那些不再被引用的对象所占用的内存空间。  
不同的垃圾回收算法（如标记-清除、标记-整理、复制算法等）被设计用来处理不同类型和生命周期的对象，以达到最优的内存管理效果。

### GC的判定方法

1. 引用计数算法: 有一个地方引用的时候就+1,当一个引用失效的时候就-1,当为0的时候就说明对象没有再被使用了.
2. 可达性分析算法: 以GC Roots作为其实节点,从这些节点开始,根据引用关系向下搜索,搜过过程中走过的路径成为引用链,如果某个对象到GCRoots之间没有任何的引用链,那么说明对象没有再被使用了.
   GC Roots: 线程堆栈中的变量,类的静态变量等

### GC的三种收集算法

1. 标记-清除(Mark-Sweep): 首先标记出所有需要回收的对象,在标记完成后,对这些对象进行回收. 缺点:执行效率不稳定,内存空间碎片化
2. 标记-复制(Copying): 每次使用一半, 标记处还存活的对象, 然后把这些对象复制到另一半(同时更新引用到新的位置),然后把之前的全部清理掉.
   缺点: 空间浪费
3. 标记-整理(Mark-Compact): 像标记-清楚算法一样,先标记之后然后清理不需要的对象,最后把这些存活对象移动到空间的一端,然后清除之外的空间.
   缺点: 执行效率不高

### 内存分代 GC

新生代: minor GC
老年代: major GC
整个Java堆和方法区的垃圾回收: Full GC

在 Java 的垃圾回收 (Garbage Collection, GC) 机制中，Minor GC、Major GC 和 Full GC 是不同类型的垃圾回收操作，它们分别触发的节点和条件如下：

1. **Minor GC**：
    - **触发条件**：当新生代（Young Generation）中的 Eden 区域满时，Minor GC 会被触发。
    - **主要处理对象**：主要回收新生代中的对象。存活下来的对象会被移动到 Survivor 区域（如果已经存活足够长时间，则会被晋升到老年代）。

2. **Major GC** (也叫 Old GC)：
    - **触发条件**：当老年代（Old Generation）中的内存满时，或者当晋升到老年代的对象无法找到足够的连续空间时，Major GC 会被触发。
    - **主要处理对象**：主要回收老年代中的对象。由于老年代的对象生命周期较长，Major GC 的频率相对较低，但耗时较长。

3. **Full GC**：
    - **触发条件**：Full GC 会清理整个堆，包括新生代和老年代，甚至永久代（如果使用的是较老的 Java 版本）或元空间（Metaspace）。
    - **触发条件**可能包括但不限于：
        - 显式调用 `System.gc()` 方法。
        - 老年代没有足够的空间分配新的大对象时。
        - 永久代或元空间的空间不足时。
        - Minor GC 和 Major GC 之后仍然需要进行垃圾回收时。
    - **主要处理对象**：Full GC 清理整个堆空间，因此它的代价最高，通常会引发长时间的停顿（stop-the-world）。

总结：

- **Minor GC** 触发于新生代的 Eden 区域满时。
- **Major GC** 触发于老年代内存不足或对象晋升失败时。
- **Full GC** 触发于整个堆内存需要清理时，可能包括多个原因。

### 什么是STW, SafePoint

STW: stop the word,在进行垃圾收集的时候,需要暂停所有业务线程
safepoint: 在一些不会导致引用关系变化的点(各种跳转).设置这些点的好处是,我们去清理垃圾对象的时候,就不会因为用户线程改变了引用关系而导致部分垃圾收集失败
就是线程只有运行到了 SafePoint 的位置，他的一切状态信息，才是确定的，也只有这个时候，才知道这个线程用了哪些内存，没有用哪些；并且，只有线程处于
SafePoint 位置，这时候对 JVM
的堆栈信息进行修改，例如回收某一部分不用的内存，线程才会感知到，之后继续运行，每个线程都有一份自己的内存使用快照，这时候其他线程对于内存使用的修改，线程就不知道了，只有再进行到
SafePoint 的时候，才会感知。

### JVM的垃圾回收器

| 垃圾回收器                          | 类型                 | 主要回收区域 | 出现时间    | 解决的问题                                                                           | 存在的问题                                    | 适用场景                       |
|--------------------------------|--------------------|--------|---------|---------------------------------------------------------------------------------|------------------------------------------|----------------------------|
| Serial GC                      | 标记-复制（单线程）         | 年轻代    | JDK 1.3 | 解决了简单应用的垃圾收集，易于实现                                                               | 停止世界（stop-the-world）长暂停, 不适合多核心处理器       | 小型应用或开发环境，单核处理器或小堆内存       |
| Serial Old                     | 标记-整理（单线程）         | 老年代    | JDK 1.3 | 用作更大应用的老年代垃圾收集                                                                  | 同Serial GC, 长暂停时间                        | 较老的系统或作为CMS的后备方案           |
| ParNew                         | 标记-复制              | 年轻代    | JDK 1.4 | 为了更好地服务多处理器机器与CMS的搭配比Serial GC更优                                                | 停停顿时间较长, 不适合大堆内存                         | 多核心处理器且小到中等堆内存, 需要较短GC停顿时间 |
| Parallel GC(Parallel Scavenge) | 标记-复制              | 年轻代    | JDK 1.4 | 提高吞吐量，利用多核性能，更关注用户代码执行的吞吐量(用户执行代码的时间/(用户代码执行时间+垃圾回收时间))。而其他垃圾回收器则更关注怎么样减少STW的时间 | 高GC暂停时间仍可能影响用户体验                         | 多核服务器环境, 注重吞吐量, 可以容忍较长GC暂停 |
| Concurrent Mark Sweep (CMS)    | 标记-清除(用户线程和标记并发执行) | 老年代    | JDK 1.5 | 减少GC停顿时间，提高应用响应性                                                                | 产生内存碎片, 'Concurrent Mode Failure', 维护成本高 | 需要较短GC停顿的WEB或应用服务器         |
| Parallel Old                   | 标记-整理              | 老年代    | JDK 1.6 | 扩展Parallel GC到老年代，进一步提高吞吐量                                                      | 分享Parallel GC的问题，即较长GC暂停                 | 大型应用且需要高吞吐量的环境             |
| G1 (Garbage First)             | 区域化的标记-整理复制()      | 整个堆    | JDK 1.7 | 解决了大内存系统中长暂停时间问题，提高预测性                                                          | 初始化配置较复杂, 可能出现性能下降                       | 大内存，多核服务器, 追求低延迟和高吞吐量      |
| ZGC (Z Garbage Collector)      | 标记-压缩              | 整个堆    | JDK 11  | 提供极低停顿时间，适合大内存应用                                                                | 目前尚处于实验阶段, 未被广泛采用                        | 实验或最新版本JVM, 大内存管理需求        |
| Shenandoah                     | 标记-压缩              | 整个堆    | JDK 12  | 目标是进一步减少GC停顿时间，独立于堆大小进行GC                                                       | 同样处于较新状态, 可能在具体系统中遇到问题                   | 大内存应用, 需要非常低的暂停时间          |

**起始阶段**
在JDK 1.3时代，最基础的垃圾回收器包括Serial GC，它主要用于单核处理器或者资源较少的环境。
这时，对于老年代的GC，会默认使用Serial Old GC。这种搭配主要是因为那时的硬件和应用的规模尚未大幅度扩展。

**进化阶段**
进入JDK 1.4和JDK 1.6，出现了更多针对多核处理器和服务器环境优化的垃圾回收器。
比如Parallel GC（针对年轻代）和Parallel Old GC（针对老年代），这两者一起使用，提供了针对多核服务器环境更好的吞吐性能。
同时，CMS的引入针对需要低延迟的应用提供了选项，通常与ParNew GC结合使用来处理年轻代，以保持较短的停顿时间。

**现代推荐组合**
从JDK 7引入的G1 GC开始，垃圾回收技术发生了重大变革。
G1 GC旨在替代老一代的CMS加ParNew的组合，同时提供对整个堆的高效管理，包括年轻代和老年代。
它通过分区的方式，尽量平衡吞吐量和响应时间，适合大内存和多核心处理器的环境。

**最新进展**
JDK 11及之后，引入了ZGC和Shenandoah GC，这些都是面向未来的垃圾回收技术，专注于几乎消除GC停顿时间，适用于高要求的大内存应用，并且继续支持多核心处理器优化。

**总结**

新生代收集器: Serial GC,Parallel GC,ParNew
老年代收集器: Serial Old,CMS,Parallel Old
整个堆: G1,ZGC,Shenandoah

- **起始阶段**：Serial + Serial Old | ParNew(Serial的多线程版本) + Serial Old
- **服务器优化**：Parallel + Serial Old -> ParNew + CMS | Parallel + Parallel Old
- **现代大内存应用**：G1 GC
- **最新技术**：ZGC | Shenandoah

选择和搭配应基于具体应用的需求，包括处理器的核心数、应用的内存需求以及对延迟的敏感度。
随着新型垃圾回收技术的引入，Java JVM的性能和效率持续提升，允许开发者针对不同的应用场景选择最合适的GC策略。

#### CMS垃圾收集器

1) 初始标记(CMS initial mark)     - STW, 初始标记仅仅只是标记一下GC Roots能直接关联到的对象,速度很快
2) 并发标记(CMS concurrent mark)  - 并发标记阶段就是从GC Roots的直接关联对象开始遍历整个对象图的过程,这个过程耗时较长但是不需要停顿用户线程,可以与垃圾收集线程一起并发运行
3) 重新标记(CMS remark)           - STW,
   为了修正并发标记期间,因用户程序继续运作而导致标记产生变动的那一部分对象的标记记录,这个阶段的停顿时间通常会比初始标记阶段稍长一些,但也远比并发标记阶段的时间短
4) 并发清除(CMS concurrent sweep) - 这个阶段的停顿时间通常会比初始标记阶段稍长一些,但也远比并发标记阶段的时间短

由于CMS收集器无法处理"浮动垃圾"(Floating Garbage),有可能出现“Con-current Mode Failure”失败进而导致另一次完全“Stop The
World”的Full GC的产生。
在CMS的并发标记和并发清理阶段，用户线程是还在继续运行的，程序在运行自然就还会伴随有新的垃圾对象不断产生，但这一部分垃圾对象是出现在标记过程结束以后，CMS无法在当次收集中处理掉它们，只好留待下一次垃圾收集时再清理掉。这一部分垃圾就称为"
浮动垃圾"

Con-current Mode Failure: 要是CMS运行期间预留的内存无法满足程序分配新对象的需要，就会出现一次“并发失败”(Concurrent Mode
Failure)，这时候虚拟机将不得不启动后备预案：冻结用户线程的执行，临时启用Serial Old收集器来重新进行老年代的垃圾收集，但这样停顿时间就很长了

通过三色标记法(在并发的可达性分析下,会存在漏标的情况)我们可以知道并发标记可能存在漏标问题 -> cms使用重新标记来修正

优点: 暂停时间短
缺点: 对CPU敏感,无法处理浮动垃圾(设置触发阈值,92%已使用就赶紧触发GC),内存碎片很多(无法匹配大对象的分配时又不得不触发full
GC)

#### G1垃圾收集器详解

G1(Garbage-First) 垃圾回收器是Java平台上一种比较先进的垃圾回收机制，主要针对多处理器机器和大内存环境设计，致力于在提供高吞吐量的同时实现可预测的停顿时间，尤其适用于需要处理大量数据的应用。
自JDK 9起，G1成为了默认的垃圾回收器，替代了之前的Parallel GC。

G1收集器去跟踪各个Region里面的垃圾堆积的“价值”大小，价值即回收所获得的空间大小以及回收所需时间的经验值，
然后在后台维护一个优先级列表，每次根据用户设定允许的收集停顿时间（使用参数-XX：MaxGCPauseMillis指定，默认值是200毫秒），
优先处理回收价值收益最大的那些Region，这也就是“Garbage First”名字的由来。

**G1垃圾回收器的主要特点**

1. **区域划分**：
   G1将Java堆分为多个区域（Regions），这些区域可以是Eden区、Survivor区或Old区。这种区域划分使得G1可以并行、独立地收集区域，而不必每次都全堆收集，从而有效控制停顿时间。
   并且这些区域的角色并不是固定的,他们可能会根据内存使用情况动态的转换角色.
2. **停顿时间预测**：
   G1的目标是允许用户指定期望的停顿时间目标（例如，不超过50ms），然后G1将尽力在这个范围内完成垃圾收集。这一功能通过优化各区域的回收顺序和调整回收量来实现。
3. **增量清理**：
   G1回收器逐步进行清理工作，每次只清理一部分区域，而不是整个堆，这帮助它更好地控制和预测GC的停顿时间。
4. **并行与并发能力**：
   G1能够利用多核心的处理能力，在多线程之间并行地处理垃圾收集，同时也支持并发的垃圾回收，以减少应用暂停的时间。

**G1垃圾回收的工作过程包括**

1. **初始标记（Initial Marking）**：
   来自并发标记阶段的暂停，标记从GC根直接可达的对象。
2. **并发标记（Concurrent Marking）**：
   G1并发地遍历对象图，标记存活的对象。这个阶段与应用程序线程同时运行，不需要暂停应用线程。
3. **最终标记（Final Marking）**：
   这是又一次暂停，处理自并发标记以来改变了状态的引用，如SATB（Snapshot At The Beginning）标记中的对象。
4. **筛选回收（Evacuation）**：
   在这个阶段，G1将收集并回收那些确定为可以清理的区域。它会将存活的对象复制到新区域，释放被占用的区域。这个阶段是并行的，并且也会导致应用暂停。

**使用和配置**

-XX:+UseG1GC 开启G1 GC

G1收集器提供了多个配置选项，其中`-XX:MaxGCPauseMillis`是一个关键的性能调优参数，它允许开发者指定期望的最大GC停顿时间。

总结来说，G1垃圾回收器通过其设计能够在大内存环境下，提供高吞吐量和预测性质的低停顿时间，是现代Java应用的理想选择。

### GC的参数

#### 垃圾收集器参数

**-XX:+UseSerialGC**

- 使用Serial GC作为新生代和老年代的垃圾收集器。
- 适合单CPU的客户端应用。
  
使用jmap -heap 查看日志:
Mark Sweep Compact GC
好像看不到具体的GC收集器

**-XX:+UseParallelGC**

- 使用Parallel GC作为新生代的垃圾收集器。
- 适合多CPU的服务器应用，专注于高吞吐量。

Parallel GC with 15 thread(s)
有15个线程进行垃圾回收的工作,可以通过 -XX:ParallelGCThreads 来配置垃圾回收的线程数量

**-XX:+UseConcMarkSweepGC**

- 使用CMS GC作为老年代的垃圾收集器。
- 旨在最小化应用程序的暂停时间。
- 从Java 9开始被标记为废弃。

使用这种垃圾回收器会自动启动+UseParNewGC

using parallel threads in the new generation.
using thread-local object allocation.
Concurrent Mark-Sweep GC

**-XX:+UseG1GC**

- 使用G1 GC作为整个堆的垃圾收集器。
- 旨在平衡吞吐量和低延迟。
- 从Java 9开始成为默认的垃圾收集器。

**-XX:+UseZGC**

- 使用ZGC作为整个堆的垃圾收集器。
- 旨在提供低延迟的垃圾收集。
- 从Java 11开始可用。

**-XX:+UseShenandoahGC**

- 使用Shenandoah GC作为整个堆的垃圾收集器。
- 旨在提供低延迟的垃圾收集。
- 从Java 12开始可用。

**-XX:+PrintGCDetails**

- 打印详细的GC事件信息。
- 用于调试和监控。

**-XX:+PrintGCDateStamps**

- 在GC日志中打印日期时间戳。
- 便于查看GC事件发生的时间。

**-XX:+PrintGCCause**

- 打印触发GC的原因。
- 有助于理解GC行为。

**-XX:+PrintGCTimeStamps**

- 打印GC事件的持续时间。
- 用于性能分析。

**-XX:+PrintHeapAtGC**

- 打印每次GC前后的堆内存状态。
- 用于监控内存使用情况。

#### 新生代参数

这些参数用于配置新生代垃圾收集器。

**-Xmn**

- 设置新生代的大小。
- 默认情况下，新生代占堆总大小的1/3。

**-XX:SurvivorRatio**

- 设置Eden区与Survivor区的比例。
- 默认值为8，意味着Eden区与两个Survivor区的总比例为8:1:1。

**-XX:MaxTenuringThreshold**

- 设置对象晋升到老年代的最大年龄。
- 控制对象在新生代中存活的次数。

**-XX:NewRatio**

- 设置新生代与老年代的比例。
- 默认值为2，意味着新生代与老年代的比例为1:2。

#### 老年代参数

这些参数用于配置老年代垃圾收集器。

**-XX:MaxHeapFreeRatio**

- 设置垃圾收集后堆的最大空闲比例。
- 控制何时触发老年代的GC。

**-XX:MinHeapFreeRatio**

- 设置垃圾收集后堆的最小空闲比例。
- 控制何时触发老年代的GC。

**-XX:InitiatingHeapOccupancyPercent**

- 设置触发G1 GC的堆占用百分比阈值。
- 仅适用于G1 GC。

**-XX:MaxHeapSize**

- 设置堆的最大大小。
- 通常使用-Xmx参数指定。

#### CMS Collector参数

这些参数适用于CMS垃圾收集器。

**-XX:CMSInitiatingOccupancyFraction**

- 设置触发CMS GC的堆占用百分比阈值。
- 仅适用于CMS GC。

**-XX:CMSMaxAbortablePrecleanTime**

- 设置CMS GC的预清理阶段的最大可中止时间。
- 仅适用于CMS GC。

#### G1 Collector参数

这些参数适用于G1垃圾收集器。

**-XX:G1HeapRegionSize**

- 设置G1 GC的堆分区大小。
- 影响G1 GC的性能。

**-XX:G1ReservePercent**

- 设置保留的内存百分比，用于防止堆内存不足。
- 仅适用于G1 GC。

**-XX:G1HeapWastePercent**

- 设置G1 GC认为堆浪费的百分比。
- 仅适用于G1 GC。

#### ZGC Collector参数

这些参数适用于ZGC垃圾收集器。

**-XX:ZGCHeapSize**

- 设置ZGC GC的堆大小。
- 仅适用于ZGC GC。

**-XX:ZGCHeapReserved**

- 设置ZGC GC的初始堆预留大小。
- 仅适用于ZGC GC。

**-XX:ZGCPauseTarget**

- 设置ZGC GC的目标暂停时间。
- 仅适用于ZGC GC。

#### Shenandoah Collector参数

这些参数适用于Shenandoah垃圾收集器。

**-XX:ShenandoahGCHeapSize**

- 设置Shenandoah GC的堆大小。
- 仅适用于Shenandoah GC。

**-XX:ShenandoahGCHeapReserve**

- 设置Shenandoah GC的初始堆预留大小。
- 仅适用于Shenandoah GC。

**-XX:ShenandoahGCHeapMax**

- 设置Shenandoah GC的最大堆大小。
- 仅适用于Shenandoah GC。

**-XX:ShenandoahGCInitialHeapSize**

- 设置Shenandoah GC的初始堆大小。
- 仅适用于Shenandoah GC。

# 虚拟机执行子系统

## 类的加载过程

加载->连接(验证->准备->解析)->初始化->使用->卸载

**加载**(读文件): 通过一个类的全限定名来获取class文件的二进制字节流->
将这个字节流所代表的静态存储结构方法转化成方法区的运行时数据结构->
在内存中生成一个代表这个类的java.lang.class对象,作为方法去这个类的各种数据的访问入口
**验证**(读了,但是需要判断给的东西是不是符合规范):确保class文件的字节流中包含的信息是符合规范和约束的
**准备**(类的静态变量申请空间和设置初始值)
**解析**(符号引用变成直接引用)
**初始化**:给静态变量赋值->调用对象的构造方法
**使用**.
**卸载**.

## 什么是双亲委派机制,具体有什么作用?

**BootStrap**(rt.jar)最顶层 - **Extension**(扩展加载器,jre/lib...) - 应用程序类加载器

如果一个类加载器收到了一个类加载的请求,它首先不会加载这个类,而是把它委派给父类加载器去完成,每一个层次的加载器都如此.
只有当最上级的父类加载器尝试加载却无法记载时,会向下反馈,然后一级级向下传递

1. **通过带有优先级的层级关系可以避免类的重复加载**
2. **保证Java程序的安全稳定运行, Java核心的API定义不会被随意的替换**

## 动态绑定

(（也称为晚期绑定或运行时多态性）是一种在运行时（而非编译时）确定要调用方法的对象的机制)

# 程序编译与代码优化

## JVM的解释器和JIT(just-in-time)

Java解释器就是在我们执行代码的过程中,jvm会帮我们把class文件的指令翻译成机器指令,然后执行
JIT编译器会监视程序的运行情况,根据实际路径和数据来优化,生成更高效的机器代码

# 并发

当线程本地存储、缓冲区分配、同步对象、栈、程序计数器等准备好以后，就会创建一个操作系统原生线程。
Java 线程结束，原生线程随之被回收。操作系统负责调度所有线程，并把它们分配到任何可用的 CPU 上。
当原生线程初始化完毕，就会调用 Java 线程的 run() 方法。当线程结束时，会释放原生线程和 Java 线程的所有资源。

## 线程执行的机制

Java虚拟机（JVM）中的线程执行机制涉及到多个方面，包括线程的创建、调度、执行以及线程之间的同步和通信。下面详细介绍JVM中的线程资源执行机制：

### 1. 线程的创建

在JVM中，线程是通过创建`java.lang.Thread`对象来表示的。当创建一个新的`Thread`对象并调用其`start()`
方法时，JVM会为该线程创建一个线程栈，并将线程的状态设置为就绪状态。线程栈用于存储线程的局部变量和方法调用栈帧。

### 2. 线程栈

每个线程都有一个独立的线程栈，用于存储该线程的方法调用栈帧。线程栈的大小可以通过`-Xss`参数来设置。栈帧包含局部变量表、操作数栈、动态链接和方法出口等信息。

### 3. 线程调度

JVM中的线程调度遵循操作系统级别的调度策略。JVM本身不负责线程的调度，而是依赖于操作系统的调度器。线程调度器负责决定哪些线程可以获得CPU时间片。在多线程环境中，JVM会与其他线程争夺CPU资源。

### 4. 线程状态

线程在执行过程中会有不同的状态，包括：

- **新建**（New）：线程对象已经创建，但尚未调用`start()`方法。
- **就绪**（Runnable）：线程已经准备好执行，等待调度器分配CPU时间片。
- **运行**（Running）：线程正在执行中。
- **阻塞**（Blocked）：线程因为等待某种条件而暂时停止运行。
- **等待**（Waiting）：线程进入了等待状态，等待某种条件满足后才能继续运行。
- **超时等待**（Timed Waiting）：线程进入了等待状态，并设置了等待时间。
- **终止**（Terminated）：线程执行完毕或因异常而终止。

### 5. 线程同步

为了确保多线程程序的正确执行，JVM提供了多种线程同步机制：

- **锁**（Locks）：使用`synchronized`关键字或显式锁（如`ReentrantLock`）来保护共享资源。
- **volatile**变量：确保变量的可见性和有序性。
- **原子变量**（Atomic Variables）：提供原子操作，避免了显式锁定。

### 6. 线程间的通信

线程间通信通常通过以下方式实现：

- **等待/通知机制**（Wait/Notify）：通过`synchronized`关键字和`Object.wait()`、`Object.notify()`方法实现。
- **生产者/消费者模式**：通过队列或阻塞队列（如`BlockingQueue`）实现。
- **信号量**（Semaphores）：通过`Semaphore`类实现。

### 7. 死锁检测

JVM提供了死锁检测机制，通过`ThreadMXBean`类可以查询和检测死锁情况。`ThreadMXBean`提供了方法来检测死锁和获取死锁信息。

### 8. 线程池

线程池是一种线程管理机制，用于复用一组预创建的线程来执行任务。线程池可以提高线程的重用率，减少线程创建和销毁的开销。
`java.util.concurrent.ExecutorService`接口提供了创建和管理线程池的API。

## Java内存模型和线程

Java内存模型（Java MemoryModel，简称JMM）是一种规范，定义了Java程序中多线程并发访问共享内存的行为。  
它规定了在多线程环境下，如何进行内存的读写操作以及线程之间如何进行通信，以确保程序在不同平台上的一致性和可预测性。

JMM 主要包含以下几个方面的内容：

1. **主内存（Main Memory）：** 主内存是所有线程共享的内存区域，用于存储 Java 对象实例、静态变量等数据。所有的线程都可以访问主内存中的数据。
2. **工作内存（Working Memory）：** 每个线程都拥有自己的工作内存，用于存储主内存中的部分数据副本。线程对数据的读写操作都是在工作内存中进行的，而不直接在主内存中操作。

Java 内存模型的设计旨在提供一种统一的并发编程规范，使得开发者能够编写出线程安全的、正确的并发程序。通过遵循 JMM 规范，可以确保在不同的
JVM 实现上，程序的行为是一致的。

[Java内存模型（JMM）总结](Java内存模型.mhtml) 这篇文章写得很好

## 线程的可见性,原子性,有序性

线程的可见性、原子性和有序性是多线程编程中的重要概念，它们分别指：

1. **可见性（Visibility）**
   当一个线程修改了共享变量的值后，其他线程能够立即看到这一修改。这确保了线程之间对共享数据的操作是同步的。可见性通常涉及主内存和工作内存之间的数据同步，可以通过volatile关键字或同步机制来保证。

2. **原子性（Atomicity）**
   指一个操作是不可分割的，要么全部执行成功，要么全部不执行。在多线程环境下，原子性保证了某个操作在同一时刻只能被一个线程执行，避免了并发操作导致的数据不一致性。Java中的原子操作可以通过synchronized关键字、Lock对象或原子类（如AtomicInteger）来实现。

3. **有序性（Ordering）**
   指程序执行的顺序与代码编写的顺序一致。在多线程环境下，由于指令重排序优化等原因，可能会导致代码的执行顺序与预期不一致。有序性保证了程序按照代码编写的顺序执行，可以通过volatile关键字、synchronized关键字和显式的锁来实现有序性。

这些概念是多线程编程中必须考虑的因素，合理地处理可见性、原子性和有序性可以避免由于线程间竞争导致的数据错乱、死锁等问题，保证程序的正确性和稳定性。

---

---

---

# 问题排查

## jvm oom了如何排查

1. 一个拥有一百多万行的excel(大小也就6M),上传后使用XSSFWorkbook进行解析之后,内存涨到了3个G,后续导致调用rpc接口OOM
   XSSFWorkbook会创建大量的对象,当然excel本身数据也有点太多了

## 讲一讲JVM调优，有哪些参数可以修改，新生代什么情况要修改。

JVM调优是一个复杂的过程，涉及到多种参数的调整，以确保应用程序运行的高效与稳定。
JVM调优主要目的是优化堆内存的管理，减少垃圾回收的影响，增强应用的响应能力和吞吐量。以下是一些关键的参数和考虑因素：

### 常用的JVM调优参数

1. **堆内存大小设置**：

    - `-Xms`：设置堆的初始大小。
    - `-Xmx`：设置堆的最大大小。
    - 设置这些参数可以避免JVM频繁扩展或缩小堆内存，影响性能。
2. **新生代大小设置**：

    - `-Xmn`：设置新生代大小，或者通过 `-XX:NewSize` 和 `-XX:MaxNewSize`。
    - 调整新生代的大小可以影响老年代的大小，以及整体的GC行为。
3. **垃圾回收器选择与配置**：

    - `-XX:+UseG1GC`, `-XX:+UseParallelGC` 等，用来选择不同的垃圾回收器。
    - 根据应用的需求选择合适的GC策略，如低停顿或高吞吐。
4. **垃圾回收日志**：

    - `-Xlog:gc*:file=<file_path>`：启用GC日志，并指定日志文件（适用于较新版本的JDK）。
    - 日志有助于分析GC表现和问题定位。
5. **针对GC的性能调优参数**：

    - `-XX:SurvivorRatio`：设置Eden区与Survivor区的比例。
    - `-XX:MaxGCPauseMillis`：希望GC的最大停顿时间。
    - `-XX:GCTimeRatio`：设置吞吐量目标等。

#### 新生代的调整时机和理由

新生代的大小直接影响应用性能，尤其是垃圾回收的频率和持续时间。下面是一些可能需要调整新生代大小的情况：

1. **频繁的Minor GC**：

    - 如果观察到频繁的Minor GC，而且每次GC回收的数据量很小，可能需要增加新生代的大小。
2. **内存占用太高**：

    - 如果系统的内存占用过高，而新生代设置过大，减少新生代的大小可能有助于余留更多内存给老年代，或者其他应用使用。
3. **应用启动性能**：

    - 在应用启动阶段，可能会大量使用堆内存。合理的调整新生代大小可以改善启动性能和响应速度。
4. **调整GC停顿时间**：

    - 如果应用需要非常短的GC停顿时间，调整新生代的大小和选择合适的GC算法是关键。

综合以上，JVM调优通常需要基于实际应用的行为、资源消耗和性能指标进行。通常建议进行全面的性能测试，收集和分析GC日志，然后根据测试结果和业务需求调整JVM参数。调优是一个迭代的过程，可能需要多次调整才能达到最佳性能。

## 哪个命令可以查看GC次数？哪个命令可以看线程？有没有研究过这些命令是怎么起作用的，为什么这写命令能拿到进程或者线程的状态？

在监控和调试 JVM 性能时，了解垃圾回收（GC）次数和线程状况是很有帮助的。下面是一些相关的命令和工具：

### 查看 GC 次数

1. **JStat：**
   `jstat` 是一个命令行工具，提供了监控 JVM 中各种性能统计信息的功能。你可以使用它来查看 GC 信息。例如：

   ```sh
   jstat -gc <pid>
   ```

   `<pid>` 是 JVM 进程的 ID。这个命令将会显示当前垃圾回收的统计数据，包括 Full GC 和 Young GC 的次数。
2. **JMX (Java Management Extensions)：**
   利用 JMX，你也可以访问 GC 信息。例如，使用 `jconsole` 或者通过编写 Java 程序连接到 JMX 服务，查看垃圾回收的 MBean 信息。

### 查看线程

1. **JStack：**
   `jstack` 是一个命令行工具，用于生成 Java 线程的转储（thread dump），显示当前所有线程的状态，包括线程栈信息。

   ```sh
   jstack <pid>
   ```
2. **JVisualVM：**
   `jvisualvm` 是一个更高级的图形化监控工具。你可以在其中查看详细的线程信息，包括线程的运行状态、堆栈跟踪等。
3. **JConsole：**
   `jconsole` 是一个基于 JMX 的图形化监控工具，也可以用来查看线程信息。启动 `jconsole` 并连接到目标 JVM
   后，可以浏览线程的状态和线程堆栈信息。

### 这些命令如何起作用

1. **jstat：**
   `jstat` 从 JVM 内存管理子系统中收集性能统计数据。JVM 内部维护了一系列性能计数器（performance
   counters），记录了各类数据，如垃圾回收次数、内存使用情况等。`jstat` 通过 JVM
   提供的诊断功能接口（如 HotSpot 内部的 PerfData API）读取这些计数器信息并展示给用户。
2. **jstack：**
   `jstack` 会向目标 JVM 发送信号（例如在 Unix-like 系统上使用 SIGQUIT 信号），要求 JVM 生成线程转储。这些转储信息来自 JVM
   内部的线程管理系统，由 JVM 内部的数据结构和堆栈信息填充。
3. **JMX：**
   Java Management Extensions (JMX) 提供了一套标准接口，允许对 JVM 的管理和监控。JVM 暴露了一系列 MBeans（Managed
   Beans），如 `GarbageCollectorMXBean`
   和 `ThreadMXBean`，这些 MBeans 提供了运行时的详细信息和性能数据。工具如 `jconsole` 和 `jvisualvm` 会通过 JMX 接口与 JVM
   进行交互，获取所需的监控数据。

总体来说，这些工具和命令能工作主要依赖于 JVM 内部已经实现的监控和管理机制，通过不同的接口和标准协议（如 JMX），它们能够实时获取和展示
JVM 的内部状态和性能数据。

### 例子

#### 解析 `jstat -gc` 命令输出结果

```plaintext
S0C      S1C      S0U    S1U   EC         EU         OC         OU        MC       MU       CCSC     CCSU       YGC   YGCT    FGC    FGCT     GCT
28160.0  32768.0  0.0    0.0   509440.0   226066.6   396800.0   59595.9   78464.0  73389.2  10624.0  9703.0     10    0.129   4      0.393    0.521
```

各个字段的含义如下：

- `S0C` (Survivor space 0 capacity): Survivor 0 区的容量，单位是 KB。这里是 28160.0 KB。
- `S1C` (Survivor space 1 capacity): Survivor 1 区的容量，单位是 KB。这里是 32768.0 KB。
- `S0U` (Survivor space 0 utilization): Survivor 0 区的使用量，单位是 KB。这里是 0.0 KB。
- `S1U` (Survivor space 1 utilization): Survivor 1 区的使用量，单位是 KB。这里是 0.0 KB。
- `EC` (Eden space capacity): Eden 区的容量，单位是 KB。这里是 509440.0 KB。
- `EU` (Eden space utilization): Eden 区的使用量，单位是 KB。这里是 226066.6 KB。
- `OC` (Old space capacity): 老年代的容量，单位是 KB。这里是 396800.0 KB。
- `OU` (Old space utilization): 老年代的使用量，单位是 KB。这里是 59595.9 KB。
- `MC` (Metaspace capacity): Metaspace 的容量，单位是 KB。这里是 78464.0 KB。
- `MU` (Metaspace utilization): Metaspace 的使用量，单位是 KB。这里是 73389.2 KB。
- `CCSC` (Compressed class space capacity): 压缩类空间的容量，单位是 KB。这里是 10624.0 KB。
- `CCSU` (Compressed class space utilization): 压缩类空间的使用量，单位是 KB。这里是 9703.0 KB。
- `YGC` (Number of young generation GC events): 年轻代 GC（垃圾回收）事件的次数。这里是 10 次。
- `YGCT` (Young generation garbage collection time): 年轻代 GC 的总时间，单位是秒。这里是 0.129 秒。
- `FGC` (Number of full GC events): Full GC（完全垃圾回收）事件的次数。这里是 4 次。
- `FGCT` (Full garbage collection time): Full GC 的总时间，单位是秒。这里是 0.393 秒。
- `GCT` (Total garbage collection time): GC 的总时间，单位是秒。这里是 0.521 秒。

总结：

- 当前系统有两个 Survivor 区，但都没有被使用。
- Eden 区已经使用了约 226066.6 KB 的内存。
- 老年代使用了约 59595.9 KB 的内存。
- Metaspace 使用了约 73389.2 KB 的内存。
- 压缩类空间使用了约 9703.0 KB 的内存。
- 系统共进行了 10 次年轻代 GC，总耗时 0.129 秒。
- 系统共进行了 4 次 Full GC，总耗时 0.393 秒。
- 总的 GC 时间为 0.521 秒。

#### 解析jmap的输出结果

C:\Users\h1123>jmap -heap 30144
Attaching to process ID 30144, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.381-b09

using thread-local object allocation.
Parallel GC with 10 thread(s)

Heap Configuration:
MinHeapFreeRatio = 0
MaxHeapFreeRatio = 100
MaxHeapSize = 8552185856 (8156.0MB)
NewSize = 178257920 (170.0MB)
MaxNewSize = 2850553856 (2718.5MB)
OldSize = 356515840 (340.0MB)
NewRatio = 2
SurvivorRatio = 8
MetaspaceSize = 21807104 (20.796875MB)
CompressedClassSpaceSize = 1073741824 (1024.0MB)
MaxMetaspaceSize = 17592186044415 MB
G1HeapRegionSize = 0 (0.0MB)

Heap Usage:
PS Young Generation
Eden Space:
capacity = 521666560 (497.5MB)
used = 306182824 (291.99869537353516MB)
free = 215483736 (205.50130462646484MB)
58.69320510020807% used
From Space:
capacity = 28835840 (27.5MB)
used = 0 (0.0MB)
free = 28835840 (27.5MB)
0.0% used
To Space:
capacity = 33554432 (32.0MB)
used = 0 (0.0MB)
free = 33554432 (32.0MB)
0.0% used
PS Old Generation
capacity = 406323200 (387.5MB)
used = 61026248 (58.19916534423828MB)
free = 345296952 (329.3008346557617MB)
15.019139443674396% used

33351 interned Strings occupying 3406648 bytes.

解析这个 JVM 输出结果如下：

**附加信息**

```
Attaching to process ID 30144, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.381-b09
```

- 成功连接到进程 ID 30144。
- 检测到服务器编译器。
- JVM 版本是 25.381-b09。

**GC 线程配置**

```
using thread-local object allocation.
Parallel GC with 10 thread(s)
```

- 使用线程本地对象分配。
- 并行 GC 使用了 10 个线程。

**堆配置 (Heap Configuration)**

```
MinHeapFreeRatio         = 0
MaxHeapFreeRatio         = 100
MaxHeapSize              = 8552185856 (8156.0MB)
NewSize                  = 178257920 (170.0MB)
MaxNewSize               = 2850553856 (2718.5MB)
OldSize                  = 356515840 (340.0MB)
NewRatio                 = 2
SurvivorRatio            = 8
MetaspaceSize            = 21807104 (20.796875MB)
CompressedClassSpaceSize = 1073741824 (1024.0MB)
MaxMetaspaceSize         = 17592186044415 MB
G1HeapRegionSize         = 0 (0.0MB)
```

- `MinHeapFreeRatio` 和 `MaxHeapFreeRatio` 设置分别是 0 和 100，这些值表示堆空闲内存的最小和最大百分比。
- 最大堆大小 (`MaxHeapSize`) 是 8552185856 字节（约 8156.0 MB）。
- 新生代初始大小 (`NewSize`) 是 178257920 字节（约 170.0 MB）。
- 新生代最大大小 (`MaxNewSize`) 是 2850553856 字节（约 2718.5 MB）。
- 老年代初始大小 (`OldSize`) 是 356515840 字节（约 340.0 MB）。
- 新生代和老年代比例 (`NewRatio`) 是 2。
- Survivor 区的比例 (`SurvivorRatio`) 是 8。
- Metaspace 初始大小 (`MetaspaceSize`) 是 21807104 字节（约 20.796875 MB）。
- 压缩类空间大小 (`CompressedClassSpaceSize`) 是 1073741824 字节（约 1024.0 MB）。
- 最大 Metaspace 大小 (`MaxMetaspaceSize`) 是一个非常大的值，表示没有上限。
- G1 堆区大小 (`G1HeapRegionSize`) 为 0，表示未使用 G1 垃圾回收器。

**堆使用情况 (Heap Usage)**

```
PS Young Generation
Eden Space:
   capacity = 521666560 (497.5MB)
   used     = 306182824 (291.99869537353516MB)
   free     = 215483736 (205.50130462646484MB)
   58.69320510020807% used
From Space:
   capacity = 28835840 (27.5MB)
   used     = 0 (0.0MB)
   free     = 28835840 (27.5MB)
   0.0% used
To Space:
   capacity = 33554432 (32.0MB)
   used     = 0 (0.0MB)
   free     = 33554432 (32.0MB)
   0.0% used
PS Old Generation
   capacity = 406323200 (387.5MB)
   used     = 61026248 (58.19916534423828MB)
   free     = 345296952 (329.3008346557617MB)
   15.019139443674396% used
```

**PS Young Generation (年轻代)**

- **Eden Space**（Eden 区）
    - 容量：521666560 字节（约 497.5 MB）
    - 已使用：306182824 字节（约 291.9987 MB）
    - 空闲：215483736 字节（约 205.5013 MB）
    - 使用率：58.69%
- **From Space**（Survivor 区 1）
    - 容量：28835840 字节（约 27.5 MB）
    - 已使用：0 字节
    - 空闲：28835840 字节（约 27.5 MB）
    - 使用率：0.0%
- **To Space**（Survivor 区 2）
    - 容量：33554432 字节（约 32.0 MB）
    - 已使用：0 字节
    - 空闲：33554432 字节（约 32.0 MB）
    - 使用率：0.0%

**PS Old Generation (老年代)**

- **Old Generation**（老年代）
    - 容量：406323200 字节（约 387.5 MB）
    - 已使用：61026248 字节（约 58.1992 MB）
    - 空闲：345296952 字节（约 329.3008 MB）
    - 使用率：15.02%

**其他信息**

```
33351 interned Strings occupying 3406648 bytes.
```

- **字符串常量池**中有 33351 个字符串，占用内存 3406648 字节（约 3.25 MB）。

总结：

- 新生代的 Eden 区使用率较高，接近 60%。
- Survivor 区几乎未使用。
- 老年代使用率较低，约 15%。
- Metaspace 和压缩类空间的配置显示，系统为类元数据和类定义保留了较大的空间。
- 使用的是 Parallel GC，并行垃圾回收机制，配置了 10 个 GC 线程。

# JVM的一些常见启动参数

[Java官方文档的jvm参数](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/java.html)

-Xmx40m/4g
-Xms40M/4G
-XX:+UseG1GC -XX:MaxGCPauseMillis=50
-XX:ParallelGCThreads=4 设置并行垃圾收集器线程数目。
-XX:+PrintGCDetails

-Xmx: 设置Java堆的最大内存大小。
-Xms: 设置Java堆的初始内存大小。
-Xss: 设置每个线程的栈大小。
-XX:PermSize / -XX:MaxPermSize: 设置持久代（Java 8之前）的初始大小和最大大小。
-XX:MetaspaceSize / -XX:MaxMetaspaceSize: 设置元空间（Java 8及更高版本）的初始大小和最大大小。
-XX:NewSize / -XX:MaxNewSize: 设置新生代的初始大小和最大大小。
-XX:SurvivorRatio: 设置Eden区与Survivor区的比率。
-XX:MaxTenuringThreshold: 设置对象晋升到老年代之前在新生代的最大存活次数。
-XX:InitialCodeCacheSize / -XX:ReservedCodeCacheSize: 设置初始代码缓存大小和保留代码缓存大小。
-XX:MaxDirectMemorySize: 设置直接内存的最大大小。
-XX:ParallelGCThreads: 设置并行垃圾收集器线程数目。
-XX:+UseConcMarkSweepGC: 启用CMS垃圾收集器。
-XX:+UseG1GC: 启用G1垃圾收集器。
-XX:+UseSerialGC: 启用串行垃圾收集器。
-XX:CompileThreshold: 设置即时编译的阈值。
-XX:CICompilerCount: 设置并行编译器的数量。
-XX:+PrintGCDetails: 打印详细的垃圾收集信息。
-XX:+HeapDumpOnOutOfMemoryError: 当发生内存溢出时自动生成堆转储文件。

