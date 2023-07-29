import com.hundsun.demo.java.jdk.pattern.structural.proxy.MySQLServiceImpl;
import lombok.SneakyThrows;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

/**
 * @projectName: study-demo
 * @package: PACKAGE_NAME
 * @className: JunitTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/26 16:02
 */

public class JunitTest {

    private static final String test = "test";

    @Test
    public void testEquals() {
        assertEquals("test", test);
    }

    @SneakyThrows
    public static void main(String[] args) {
        MySQLServiceImpl mySQLService = new MySQLServiceImpl();
        Method method = mySQLService.getClass().getMethod("update", String.class);
    }
}
