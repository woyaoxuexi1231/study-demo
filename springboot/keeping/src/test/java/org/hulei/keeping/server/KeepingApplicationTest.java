package org.hulei.keeping.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author hulei
 * @since 2024/10/13 21:46
 */

/*
@RunWith作为一个Junit注解,提供一个测试运行器来引导JUnit运行测试用例
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KeepingApplication.class)
public class KeepingApplicationTest {

    @Test
    public void contextLoads() {
    }
}