package org.hulei.basic.pattern.creational;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.factory.simple
 * @className: Car
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 17:36
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Car {

    /**
     * 轮胎
     */
    private String wheel;

    /**
     * 引擎
     */
    private String engine;
}
