## Servlet Stack 和 Reactive Stack

[Spring | Reactive](https://spring.io/reactive)

![img](https://spring.io/img/extra/reactive-5.svg)



## Reactive-Stream

[08.为什么有Reactive-Stream规范_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1sC4y1K7ET?spm_id_from=333.788.player.switch&vd_source=8d7ce9dd45b35258ee11a3c3ce982ea9&p=8)





## Flux 和 Mono

参考文章： [响应式编程——初识 Flux 和 Mono](https://segmentfault.com/a/1190000044156563)

Flux 和 Mono 都是数据流的发布者，使用 Flux 和 Mono 都可以发出三种数据信号：元素值，错误信号，完成信号；错误信号和完成信号都代表终止信号，终止信号用于告诉订阅者数据流结束了，错误信号终止数据流同时把错误信息传递给订阅者。