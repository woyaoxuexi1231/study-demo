package org.hulei.basic.jdk.java9;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * @author johnny
 * @create 2020-02-24 下午5:44
 **/

@Slf4j
public class ReactiveStreamTest {

    public static void main(String[] args) throws InterruptedException {

        // 1.创建 生产者Publisher JDK9自带的 实现了Publisher接口
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();

        // 2.创建 订阅者 Subscriber，需要自己去实现内部方法
        Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<>() {

            private Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                System.out.println("订阅成功。。");
                subscription.request(1);
                System.out.println("订阅方法里请求一个数据");
            }

            @Override
            public void onNext(Integer item) {
                log.info("【onNext 接受到数据 item : {}】 ", item);
                subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("【onError 出现异常】");
                subscription.cancel();
            }

            @Override
            public void onComplete() {
                log.info("【onComplete 所有数据接收完成】");
            }
        };

        // 3。发布者和订阅者 建立订阅关系 就是回调订阅者的onSubscribe方法传入订阅合同
        publisher.subscribe(subscriber);


        // 4.发布者 生成数据
        for (int i = 1; i <= 5; i++) {
            log.info("【生产数据 {} 】", i);
            // submit是一个阻塞方法，此时会调用订阅者的onNext方法
            publisher.submit(i);
        }


        // 5.发布者 数据都已发布完成后，关闭发送，此时会回调订阅者的onComplete方法
        publisher.close();

        // 主线程睡一会
        Thread.currentThread().join(100000);

    }
}
