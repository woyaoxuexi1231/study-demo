package org.hulei.mybatis.spring;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hulei.entity.jpa.pojo.BigDataUser;
import org.hulei.mybatis.mapper.BigDataUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author hulei
 * @since 2025/7/27 15:29
 */

@Slf4j
@RequestMapping("/page")
@RestController
public class PageController {

    @Autowired
    BigDataUserMapper bigDataUserMapper;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @GetMapping("/page")
    public void page() {
        /*
        mybatis 实现分页的方式有很多种
          1. 直接进行物理分页，也是最简单的方式，在 xml 的 sql 语句中进行物理分页
          2. 使用分页插件，在插件内进行逻辑增强对 sql 进行修改，达到物理分页的效果，使用方便，且功能强大
          3. mybatis 自带的 RowBounds，不如分页插件方便，但是也可以起到分页作用
         */
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            RowBounds rowBounds = new RowBounds(0, 10);
            List<BigDataUser> productInfos = sqlSession.selectList("org.hulei.mybatis.mapper.BigDataUserMapper.selectAll", null, rowBounds);
            System.out.println(productInfos.size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // 使用 PageHelper 插件后，紧接着的第一条查询语句生效，实现原理就是使用 ThreadLocal，查询完之后清楚当前线程的这个值
        PageHelper.startPage(1, 10);
        List<BigDataUser> productInfos = bigDataUserMapper.selectAll();
        System.out.println(productInfos.size());

    }


    /**
     * 分页参数 startPage 的两个参数测试
     */
    @GetMapping(value = "/page-helper")
    public void pageHelper(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        /*
        pagehelper.auto-runtime-dialect=true 每次查询通过连接信息获取对应的数据源信息, 这个连接用完后关闭
        开启后, 每一次分页都会去获取连接, 根据这个连接的具体信息来开启不同的分页上下文
        PageAutoDialect.getDialect()

        pageSizeZero 参数 - pageSize 为 0 的时候会查出所有数据而不进行分页
            - 在稍低版本中 pageNum 为 0 不会影响这个参数的使用, 稍新版本中 pageNum 为 0 不会查数据(这里使用 5.2.0 版本)
            - pageNum=0 始终不会有数据,不管pagesize是多少
         */
        log.info("pageNum: {}, pageSize: {}", pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        System.out.println(Arrays.toString(bigDataUserMapper.selectAll().toArray(new BigDataUser[0])));
        PageHelper.startPage(pageNum, pageSize);
        System.out.println(Arrays.toString(bigDataUserMapper.selectAll().toArray(new BigDataUser[0])));
    }


}
