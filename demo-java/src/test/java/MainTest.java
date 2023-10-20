// import cn.hutool.core.thread.ExecutorBuilder;
// import lombok.Data;
//
// import java.util.concurrent.Executor;
// import java.util.function.Consumer;
// import java.util.function.Supplier;
//
// /**
//  * @ProjectName: study-demo
//  * @Package: PACKAGE_NAME
//  * @Description:
//  * @Author: hulei42031
//  * @Date: 2023-06-15 12:40
//  */
//
// public class MainTest {
//
//     public static void main(String[] args) {
//         // TestConsumer test1 = test2();
//         // test1.print(1);
//         Integer i1 = 1;
//         Integer i2 = 1;
//         Integer i3 = 1000;
//         Integer i4 = 1000;
//         final Integer i5 = i4;
//         // i4++;
//         System.out.println(1);
//     }
//
//
//     public static TestConsumer test() {
//         TestObject testObject = new TestObject();
//         TestConsumer testConsumer = new TestConsumer();
//         testConsumer.setConsumer(
//                 (i) -> {
//                     if (testObject.getInteger().equals(i)) {
//                         System.out.println("hello");
//                     }
//                 }
//         );
//         testObject.setInteger(2);
//         return testConsumer;
//     }
//
//     public static TestConsumer test2() {
//         Integer integer = 100000;
//         TestConsumer testConsumer = new TestConsumer();
//         testConsumer.setConsumer(
//                 (i) -> {
//                     if (integer.equals(i)) {
//                         System.out.println("hello");
//                     }
//                 }
//         );
//         return testConsumer;
//     }
//
//     @Data
//     public static class TestConsumer {
//
//         private Consumer<Integer> consumer;
//
//         public void print(Integer i) {
//             consumer.accept(i);
//         }
//     }
//
//     @Data
//     public static class TestObject {
//         Integer integer;
//     }
//
//     // public void localVariableMultithreading() {
//     //     Executor executor = ExecutorBuilder.create().build();
//     //     boolean run = true;
//     //     executor.execute(() -> {
//     //         while (run) {
//     //             // do operation
//     //         }
//     //     });
//     //
//     //     run = false;
//     // }
//     private int start = 0;
//
//     Supplier<Integer> incrementer() {
//         return () -> start++;
//     }
//
// }
