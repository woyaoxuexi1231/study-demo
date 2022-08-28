package com.hundsun.demo.java.hs;

import lombok.Data;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

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
public class ReBody {

    /**
     * 功能号
     */
    private String functionId;

    /**
     * 功能号中文名
     */
    private String functionIdChName;

    /**
     * 功能号英文名
     */
    private String functionIdEnName;

    /**
     * 功能号参数
     */
    private Queue<String> functionIdParam;

    /**
     * 功能号参数类型
     */
    private Map<String, String> functionIdType;

    /**
     * 功能号参数说明
     */
    private Map<String, String> functionIdInfo;

    /**
     * 菜单名
     */
    private String menuName;

    public ReBody(String functionId) {
        this.functionId = functionId;
        this.functionIdType = new HashMap<>();
        this.functionIdInfo = new HashMap<>();
        this.functionIdParam = new ArrayDeque<>();
    }

}
