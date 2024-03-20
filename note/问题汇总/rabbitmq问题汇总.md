> 24-03-12 21:53:49.938 ERROR [AMQP Connection 192.168.80.128:5672] o.s.a.r.c.CachingConnectionFactory.log(1575) : Channel shutdown: channel error;
> protocol method: #method<channel.close>(reply-code=406, reply-text=PRECONDITION_FAILED - inequivalent arg 'x-dead-letter-exchange' for queue '
> topic-queue-slave' in vhost '/': received none but current is the value 'exchange-test-dead' of type 'longstr', class-id=50, method-id=10)
>
> **启动报错** 这是因为配置的参数与已有队列的参数不一致导致 received none but current is the value 'exchange-test-dead'


> 24-03-16 00:00:51.473 ERROR[AMQP Connection 192.168.80.128:5672] o.s.a.r.c.CachingConnectionFactory(1575) - Channel shutdown: channel error;
> protocol method: #method<channel.close>(reply-code=406, reply-text=PRECONDITION_FAILED - unknown delivery tag 1, class-id=60, method-id=80)
>
> 

