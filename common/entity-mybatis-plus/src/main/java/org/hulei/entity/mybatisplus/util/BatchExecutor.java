package org.hulei.entity.mybatisplus.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author hulei
 * @since 2024/10/10 20:55
 */

@Slf4j
public class BatchExecutor {

    public static  <T extends List<R>, R> void exeBatch(SqlSessionTemplate sqlSessionTemplate,T t, BiConsumer<SqlSession, R> consumer) {
        // 新获取一个模式为 BATCH, 自动提交为false的session
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (R r : t) {
                // 或者使用
                // sqlSession.insert("com.example.demo.db.dao.PersonModelMapper.insertSelective", new PersonModel());
                // 主意这时候不能正确返回影响条数了
                consumer.accept(sqlSession, r);
            }
            // sqlSession.flushStatements();

            sqlSession.commit();
            // 清理缓存，防止溢出
            // sqlSession.clearCache();
        } catch (Exception e) {
            // 异常回滚
            log.error("exeBatch出现异常,", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }
}
