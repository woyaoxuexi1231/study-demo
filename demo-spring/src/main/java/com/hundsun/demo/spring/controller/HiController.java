package com.hundsun.demo.spring.controller;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.controller
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-07-22 16:21
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public class HiController {


    public static void main(String[] args) {
        List<HiController> hiControllerList = new ArrayList<>();
        while (true){
            hiControllerList.add(new HiController());
        }
    }


}
interface A {
    void setName();
    void setAge();
    void getName();
    void getAge();
}

class A1 implements A{

    private String name;

    private String age;

    @Override
    public void setName() {

    }

    @Override
    public void setAge() {

    }

    @Override
    public void getName() {

    }

    @Override
    public void getAge() {

    }
}
