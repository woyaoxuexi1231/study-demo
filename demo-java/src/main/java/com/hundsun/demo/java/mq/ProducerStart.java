package com.hundsun.demo.java.mq;

import com.hundsun.demo.java.mq.config.ConnectFactory;
import com.hundsun.demo.java.mq.work.MsgProducer;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq
 * @className: ProducerStart
 * @description:
 * @author: h1123
 * @createDate: 2023/3/1 21:07
 */
@Slf4j
public class ProducerStart {

    public static void main(String[] args) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            new MsgProducer(ConnectFactory.getConnect(), scanner.nextLine()).start();
        }
    }
}
