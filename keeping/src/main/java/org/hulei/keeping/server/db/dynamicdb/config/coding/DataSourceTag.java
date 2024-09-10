package org.hulei.keeping.server.db.dynamicdb.config.coding;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.jdbc
 * @className: DataSourceType
 * @description: 定义三个数据源标志
 * @author: h1123
 * @createDate: 2023/2/18 16:05
 */

public enum DataSourceTag {

    MASTER("master"),
    SECOND("second"),
    THIRD("third");

    String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    DataSourceTag(String tag) {
        this.tag = tag;
    }
}
