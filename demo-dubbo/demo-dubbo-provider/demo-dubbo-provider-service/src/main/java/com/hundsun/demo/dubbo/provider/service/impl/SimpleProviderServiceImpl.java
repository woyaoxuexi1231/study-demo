package com.hundsun.demo.dubbo.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.commom.core.utils.ResultDTOBuild;
import com.hundsun.demo.dubbo.provider.api.model.request.UserRequestDTO;
import com.hundsun.demo.dubbo.provider.api.model.request.UserSelectReqDTO;
import com.hundsun.demo.dubbo.provider.api.service.SimpleProviderService;
import com.hundsun.demo.dubbo.provider.mapper.UserMapper;
import com.hundsun.demo.dubbo.provider.model.domain.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: dubbo-demo
 * @Package: com.hundsun.amust.dubbodemo.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-21 15:15
 */
@DubboService
@Slf4j
public class SimpleProviderServiceImpl implements SimpleProviderService {

    @Resource
    UserMapper userMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public ResultDTO getHelloWorld(String hello) {
        return ResultDTOBuild.resultSuccessBuild(hello + " world");
    }

    @Override
    public ResultDTO insertUser(UserRequestDTO userRequestDTO) {
        UserDO userDO = new UserDO();
        BeanUtil.copyProperties(userRequestDTO, userDO);
        return ResultDTOBuild.resultSuccessBuild(this.userMapper.insertSelective(userDO));
    }

    @Override
    public ResultDTO<?> selectUser(UserSelectReqDTO req) {

        Condition condition = new Condition(UserDO.class);
        condition.createCriteria().andLike("name", req.getName());

        List<UserDO> userDOS = userMapper.selectByCondition(condition);
        List<UserRequestDTO> rsp = new ArrayList<>();

        for (UserDO userDO : userDOS) {
            UserRequestDTO requestDTO = new UserRequestDTO();
            BeanUtils.copyProperties(userDO, requestDTO);
            rsp.add(requestDTO);
        }

        return ResultDTOBuild.resultSuccessBuild(rsp);

    }


    // @Autowired
    // RedissonClient redissonClient;

    /**
     *
     */
    private static final ThreadPoolExecutor RABBITMQ_POOL = new ThreadPoolExecutor(5, 20, 20,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(150), new ThreadFactoryBuilder()
            .setNamePrefix("RedissonServiceImpl-thread-").build(), new ThreadPoolExecutor.CallerRunsPolicy());

    // @RabbitHandler
    // @RabbitListener(queuesToDeclare = @Queue("notice_queue"))
    public void receiveMsg(String msg) {
        // log.info("im receive " + msg);
        // RABBITMQ_POOL.execute(() -> {
        //     Lock lock = redissonClient.getLock("DEMO:RABBITMQ:INSERTTEST");
        //     lock.lock();
        //     log.info("当前线程:" + Thread.currentThread().getName() + "续约成功");
        //     try {
        //         // 再往这张表插入40万条数据
        //         jdbcTemplate.execute("INSERT INTO io_test2(c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,c19,c20,c21,c22,c23,c24,c25,c26,c27,c28,c29,c30,c31,c32) SELECT * FROM io_test");
        //     } finally {
        //         lock.unlock();
        //         log.info("当前线程:" + Thread.currentThread().getName() + "解约成功");
        //     }
        // });
        // log.info("execute end");
        // throw new RuntimeException("error!!!");
    }

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue("notice_queue"))
    public void receiveMsg2(String msg) {
        log.info("im receive " + msg + "too");
    }
}
