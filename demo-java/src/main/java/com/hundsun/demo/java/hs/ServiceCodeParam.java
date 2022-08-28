package com.hundsun.demo.java.hs;

import lombok.Data;

/**
 * @ProductName: Java
 * @Package:
 * @Description:
 * @Author: HL
 * @Date: 2021/10/12 16:13
 * @Version: 1.0
 * <p>
 */
@Data
public class ServiceCodeParam {

    /**
     * 是否需要分页
     */
    private boolean isNeedPage;

    /**
     * request名
     */
    private String requestDTOName;

    /**
     * response名
     */
    private String responseDTOName;
}
