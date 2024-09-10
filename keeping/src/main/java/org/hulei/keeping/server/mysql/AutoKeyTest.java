package org.hulei.keeping.server.mysql;

import lombok.Data;

import javax.persistence.Table;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.mysql
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-03-13 20:20
 * @UpdateRemark:
 */

@Table(name = "autokeytest")
@Data
public class AutoKeyTest {
    private String a;
    private String b;
}
