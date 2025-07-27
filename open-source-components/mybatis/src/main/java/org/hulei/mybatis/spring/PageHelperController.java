package org.hulei.mybatis.spring;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.hulei.entity.jpa.pojo.ProductInfo;
import org.hulei.mybatis.mapper.ProductInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author hulei
 * @since 2025/7/27 19:07
 */

@Slf4j
@RestController
@RequestMapping("/page-helper")
public class PageHelperController {

    @Autowired
    ProductInfoMapper productInfoMapper;

    /**
     * 分页参数 startPage 的两个参数测试
     */
    @GetMapping(value = "/pageHelper")
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
        System.out.println(Arrays.toString(productInfoMapper.selectAll().toArray(new ProductInfo[0])));
        PageHelper.startPage(pageNum, pageSize);
        System.out.println(Arrays.toString(productInfoMapper.selectAll().toArray(new ProductInfo[0])));
    }
}
