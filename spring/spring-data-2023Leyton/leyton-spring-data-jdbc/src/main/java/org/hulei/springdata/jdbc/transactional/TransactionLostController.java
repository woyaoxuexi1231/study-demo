package org.hulei.springdata.jdbc.transactional;

import org.hulei.entity.mybatisplus.domain.Employees;
import org.hulei.springdata.jdbc.transactional.mapper.EmployeesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 测试注解失效的场景, 失效的主要原因: 1.没有通过springbean的方式去调用方法,方法没有被代理,事务不生效 2.过程中切换了线程导致事务失效
 *
 * @author hulei
 * @since 2024/9/17 23:21
 */

@SuppressWarnings("CallToPrintStackTrace")
@RestController
@RequestMapping("/spring-transaction-lost")
public class TransactionLostController {

    final ThreadPoolExecutor commonPool;
    final EmployeesMapper employeesMapper;

    public TransactionLostController(
            @Qualifier("commonPool") ThreadPoolExecutor commonPool,
            EmployeesMapper employeesMapper) {
        this.commonPool = commonPool;
        this.employeesMapper = employeesMapper;
    }

    TransactionLostController controller;

    @Autowired
    public void setController(TransactionLostController controller) {
        this.controller = controller;
    }

    /**
     * 类没有在 spring 的 IOC 容器中,这样 spring 无法生成事务管理的代理对象, 也就失效了: 可能类是 new 出来的, 没有使用 bean 声明
     */
    @GetMapping("/innerClass")
    public void innerClass() {
        // 事务失效, 获取的这个匿名内部类并没有被 spring 管理, 所以没有注册相关的事务管理器
        commonPool.execute(new Runnable() {
            @Transactional
            @Override
            public void run() {
                Employees employeeDO = new Employees();
                employeeDO.setEmployeeNumber(1002L);
                employeeDO.setFirstName("innerClass");
                employeesMapper.updateById(employeeDO);
                throw new RuntimeException("阻止提交");
            }
        });
    }

    /**
     * 异常未被正确捕获,默认捕获RuntimeException,其他异常是不会触发回滚的.所以需要配置rollbackFor参数
     */
    @GetMapping("/unCatchException")
    public void unCatchException() {
        // 事务失效,默认捕获 RuntimeException, 但是方法抛出了一个非免检异常, 并且没有指定需要回滚这种异常, 所以这里会失效
        commonPool.execute(() -> {
            try {
                controller.runWithException();
            } catch (CustomException e) {
                e.printStackTrace();
            }
        });
    }

    @Transactional
    public void runWithException() throws CustomException {
        Employees employeeDO = new Employees();
        employeeDO.setEmployeeNumber(1002L);
        employeeDO.setFirstName("unCatchException");
        employeesMapper.updateById(employeeDO);
        throw new CustomException("this is a check exception");
    }

}
