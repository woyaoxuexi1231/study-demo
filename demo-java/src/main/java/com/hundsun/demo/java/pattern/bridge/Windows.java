package com.hundsun.demo.java.pattern.bridge;

public class Windows implements OperationSystem {
    @Override
    public void display(Image image) {
        image.printImg();
    }
}
