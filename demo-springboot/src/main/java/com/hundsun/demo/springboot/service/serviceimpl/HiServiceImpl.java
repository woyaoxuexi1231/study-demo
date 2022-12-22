package com.hundsun.demo.springboot.service.serviceimpl;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.service.serviceimpl
 * @className: HiServceImpl
 * @description:
 * @author: h1123
 * @createDate: 2022/11/1 19:56
 * @updateUser: h1123
 * @updateDate: 2022/11/1 19:56
 * @updateRemark:
 * @version: v1.0
 * @see :
 */
@Component
public class HiServiceImpl {

    public void sayHi(){
        System.out.println("hi");
    }

    public static void main(String[] args) {
        for (int i = 1; i < 200; i++) {
            System.out.println("dat"+i+"=1");
        }
    }
}
