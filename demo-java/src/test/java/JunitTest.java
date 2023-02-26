import org.junit.Test;

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
}
