// import com.hundsun.demo.java.jdk.pattern.structural.proxy.MySQLServiceImpl;
// import lombok.SneakyThrows;
// import lombok.Data;
// import org.junit.Test;
//
// import java.lang.reflect.Method;
//
// import java.util.List;
// import java.util.function.Consumer;
//
// import static org.junit.Assert.assertEquals;
//
// /**
//  * @projectName: study-demo
//  * @package: PACKAGE_NAME
//  * @className: JunitTest
//  * @description:
//  * @author: h1123
//  * @createDate: 2023/2/26 16:02
//  */
//
// public class JunitTest {
//
//     private static final String test = "test";
//
//     @Test
//     public void testEquals() {
//         assertEquals("test", test);
//     }
//
//     @SneakyThrows
//     public static void main(String[] args) {
//         MySQLServiceImpl mySQLService = new MySQLServiceImpl();
//         Method method = mySQLService.getClass().getMethod("update", String.class);
//     }
//
//     @Test
//     public TestConsumer test() {
//         Integer integer = 1;
//         TestConsumer testConsumer = new TestConsumer();
//         testConsumer.setConsumer((i) -> System.out.println(integer));
//         return testConsumer;
//     }
//
//     @Test
//     public void print() {
//         TestConsumer test1 = test();
//         test1.print();
//     }
//
//     @Data
//     public static class Sharding {
//         /**
//          * 产品列表(fund_code)
//          */
//         private List<Integer> fundList;
//     }
//
//     @Data
//     public static class TestConsumer {
//
//         private Consumer consumer;
//
//         public void print() {
//             consumer.accept(new Object());
//         }
//     }
// }
