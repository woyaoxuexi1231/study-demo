package com.hundsun.demo.java;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-12-21 15:36
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class MavenNote {
    /*
    https://www.cnblogs.com/davidhhuan/p/17004656.html
    Since Maven 3.8.1 http repositories are blocked.
    把这个版本的maven全局配置里的配置改一下, 把配置里的这个给注释掉就行了

    idea在的安装目录下: C:\Program Files\JetBrains\IntelliJ IDEA 2022.1\plugins\maven\lib\maven3\conf

    <mirror>
      <id>maven-default-http-blocker</id>
      <mirrorOf>external:http:*</mirrorOf>
      <name>Pseudo repository to mirror external repositories initially using HTTP.</name>
      <url>http://0.0.0.0/</url>
      <blocked>true</blocked>
    </mirror>
    -->
     */
}
