package com.hundsun.demo.java.pattern.bridge;

public class Linux implements OperationSystem {
    @Override
    public void display(Image image) {
        image.printImg();
    }
}
