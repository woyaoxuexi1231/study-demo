package org.hulei.springboot.js.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.hundsun.demo.commom.core.model.TkUser;
import org.hulei.springboot.js.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author hulei42031
 * @since 2024-04-03 17:14
 */

@Slf4j
@RestController
@RequestMapping("/tkmybatis")
public class TkMybatisController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    TkMybatisController tkMybatisController;

    public <T extends List<R>, R> void exeBatch(T t, BiConsumer<SqlSession, R> consumer) {
        //新获取一个模式为 BATCH, 自动提交为false的session
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (R r : t) {
                //或者使用
                //sqlSession.insert("com.example.demo.db.dao.PersonModelMapper.insertSelective", new PersonModel());
                //主意这时候不能正确返回影响条数了
                consumer.accept(sqlSession, r);
            }
            // sqlSession.flushStatements();

            sqlSession.commit();
            //清理缓存，防止溢出
            //sqlSession.clearCache();
        } catch (Exception e) {
            //异常回滚
            log.error("exeBatch出现异常,", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @RequestMapping("/test")
    @Transactional
    public void test() {
        log.info("{}", userMapper.selectAll().size());
        tkMybatisController.exeBatch(CollectionUtil.newArrayList(new TkUser("hulei")), (sqlSession, user) -> {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.insert(user);
        });
        log.info("{}", userMapper.selectAll().size());
    }
}
