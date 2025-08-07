package org.hulei.springdata.jdbc.transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hulei.entity.mybatisplus.domain.BigDataUsers;
import org.hulei.springdata.jdbc.mapper.BigDataUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author hulei42031
 * @since 2024-05-09 15:02
 */

@RequiredArgsConstructor
@Configuration
@Slf4j
@RestController
@RequestMapping("/transactional")
public class TransactionController {

    final SubUserService subUserService;
    final ThreadPoolExecutor commonPool;
    final BigDataUserMapper bigDataUserMapper;


    /**
     * 测试 @Transactional 注解的继承问题
     */
    @GetMapping("/transactionalInherited")
    public void transactionalInherited() {
        subUserService.save(BigDataUsers.gen());
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
                BigDataUsers gen = BigDataUsers.gen();
                bigDataUserMapper.insert(gen);
                throw new RuntimeException("阻止提交");
            }
        });
    }

    @GetMapping("/unCatchException")
    @Transactional
    public void runWithException() throws CustomException {
        BigDataUsers gen = BigDataUsers.gen();
        bigDataUserMapper.insert(gen);
        /*
        异常未被正确捕获, 默认捕获RuntimeException, 其他异常是不会触发回滚的. 所以需要配置rollbackFor参数
        事务失效, 默认捕获 RuntimeException, 但是方法抛出了一个非免检异常, 并且没有指定需要回滚这种异常, 所以这里会失效s
         */
        throw new CustomException("this is a check exception");
    }

}


abstract class ParentUserService {

    @Autowired
    BigDataUserMapper bigDataUserMapper;

    @Transactional
    public void save(BigDataUsers user) {
        bigDataUserMapper.insert(user);
    }
}


@Component
class SubUserService extends ParentUserService {

    /**
     * Transactional注解是可以继承的,所以子类即使在重写的方法上不加这个注解,也会默认使用父类的注解
     * 要打破继承关系,那么只能覆盖父类的配置
     *
     * @param user 新增的用户信息
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void save(BigDataUsers user) {
        bigDataUserMapper.insert(user);
        throw new RuntimeException("阻止落库");
    }
}
