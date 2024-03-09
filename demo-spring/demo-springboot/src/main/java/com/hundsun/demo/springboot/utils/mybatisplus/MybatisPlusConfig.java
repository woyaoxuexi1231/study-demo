package com.hundsun.demo.springboot.utils.mybatisplus;


import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.SqlInfo;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;


/**
 * MybatisPlusConfig
 *
 * @author chendd
 * @date 2022/7/3 20:40
 */

@Slf4j
// @Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件配置/动态表名规则设置
     *
     * @return 插件配置
     */
    @Bean
    public PaginationInterceptor getPaginationInterceptor() {
        PaginationInterceptor interceptor = new PaginationInterceptor();
        // //构建动态标名解析器
        // DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
        // Map<String, ITableNameHandler> tableNameMap = new HashMap<>();
        // tableNameMap.put("user", (metaObject, sql, tableName) -> {
        //     log.info(sql);
        //     log.info(tableName);
        //     return tableName;
        // });
        // dynamicTableNameParser.setTableNameHandlerMap(tableNameMap);
        interceptor.setSqlParserList(Lists.newArrayList(getAbc()));
        return interceptor;
    }

    @Value("${customization.schema}")
    private String schema;

    private ISqlParser getAbc() {
        return (metaObject, sql) -> {
            log.info(sql);
            if (StringUtils.containsIgnoreCase(sql, "{h-schema}")) {
                return SqlInfo.newInstance().setSql(sql.replace("{h-schema}", schema + "."));
            }
            return SqlInfo.newInstance().setSql(sql);
        };
    }
}