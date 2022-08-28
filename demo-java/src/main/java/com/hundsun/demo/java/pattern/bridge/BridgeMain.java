package com.hundsun.demo.java.pattern.bridge;

public class BridgeMain {


    public static void main(String[] args) {

        Windows windows = new Windows();

        windows.display(new JpgImage());
    }
}
