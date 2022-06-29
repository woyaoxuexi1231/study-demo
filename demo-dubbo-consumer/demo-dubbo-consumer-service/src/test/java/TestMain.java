import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: PACKAGE_NAME
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-27 13:59
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public class TestMain {

    public static void main(String[] args) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(classLoader.getClass().getName());

        new A(new B());

    }


    static class A{

        B b;

        public A(B b) {
            System.out.println("A");
            this.b = b;
        }
    }

    static class B{

        public B(){
            System.out.println("B");
        }

    }

}
