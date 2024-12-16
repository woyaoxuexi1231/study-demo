import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @ProjectName: study-demo
 * @Package: PACKAGE_NAME
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-06-15 12:40
 */

@Slf4j
public class MainTest {

    /**
     * ThreadPoolExecutor
     */
    private static final ThreadPoolExecutor CONSUMER_TEST_POOL = new ThreadPoolExecutor(2, 2, 20,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(5), new ThreadFactoryBuilder()
            .setNamePrefix("consumer-test-thread-").build(), new ThreadPoolExecutor.CallerRunsPolicy());

    @SneakyThrows
    public static void main(String[] args) {
        // 获取对象的所有属性(包括父类)
        Class<?> temp = Grandfather.class;
        Set<Field> fields = new HashSet<>();
        do {
            Field[] declaredFields = temp.getDeclaredFields();
            fields.addAll(Arrays.asList(declaredFields));
            temp = temp.getSuperclass();
        } while (temp == Object.class);

        fields.forEach(i -> {
            Set<Annotation> objects = new HashSet<>(Arrays.asList(i.getDeclaredAnnotations()));
            System.out.println(objects);
            System.out.println(i.getName());
        });

        Object o = new Grandfather();
        System.out.println(o.getClass());
        for (Field declaredField : Grandfather.class.getDeclaredFields()) {
            System.out.println(declaredField.getClass());
            System.out.println(declaredField.getType());
        }

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5 * 60 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.setName("你好");
        thread.start();

        Thread thread2 = new Thread(() -> {
            while (true) ;
        });
        thread2.setName("你好2");
        thread2.start();

        Thread thread3 = new Thread(() -> {
            while (true) ;
        });
        thread3.setName("你好3");
        thread3.start();

        CONSUMER_TEST_POOL.execute(() -> {
            while (true) ;
        });

        CONSUMER_TEST_POOL.execute(() -> {
            System.out.println(11);
        });

    }


    public static class Grandfather {
        private String name;

        public void print() {
            System.out.println("Grandfather");
        }
    }

    public static class Father extends Grandfather {
        @Override
        public void print() {
            System.out.println("Father");
        }
    }

    public static class Son extends Father {
        @Override
        public void print() {
            System.out.println("son");
        }
    }

    public static TestConsumer test() {
        TestObject testObject = new TestObject();
        TestConsumer testConsumer = new TestConsumer();
        testConsumer.setConsumer(
                (i) -> {
                    if (testObject.getInteger().equals(i)) {
                        System.out.println("hello");
                    }
                }
        );
        testObject.setInteger(2);
        return testConsumer;
    }

    public static TestConsumer test2() {
        Integer integer = 100000;
        TestConsumer testConsumer = new TestConsumer();
        testConsumer.setConsumer(
                (i) -> {
                    if (integer.equals(i)) {
                        System.out.println("hello");
                    }
                }
        );
        return testConsumer;
    }

    @Data
    public static class TestConsumer {

        private Consumer<Integer> consumer;

        public void print(Integer i) {
            consumer.accept(i);
        }
    }

    @Data
    public static class TestObject {
        Integer integer;
    }

    // public void localVariableMultithreading() {
    //     Executor executor = ExecutorBuilder.create().build();
    //     boolean run = true;
    //     executor.execute(() -> {
    //         while (run) {
    //             // do operation
    //         }
    //     });
    //
    //     run = false;
    // }
    private int start = 0;

    Supplier<Integer> incrementer() {
        return () -> start++;
    }

}
