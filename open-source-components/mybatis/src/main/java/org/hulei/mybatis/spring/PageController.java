package org.hulei.mybatis.spring;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hulei.entity.jpa.pojo.ProductInfo;
import org.hulei.mybatis.mapper.ProductInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hulei
 * @since 2025/7/27 15:29
 */

@RequestMapping("/page")
@RestController
public class PageController {

    @Autowired
    ProductInfoMapper productInfoMapper;
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
            List<ProductInfo> productInfos = sqlSession.selectList("org.hulei.mybatis.mapper.ProductInfoMapper.selectAll", null, rowBounds);
            System.out.println(productInfos.size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // 使用 PageHelper 插件后，紧接着的第一条查询语句生效，实现原理就是使用 ThreadLocal，查询完之后清楚当前线程的这个值
        PageHelper.startPage(1, 10);
        List<ProductInfo> productInfos = productInfoMapper.selectAll();
        System.out.println(productInfos.size());

    }


}
