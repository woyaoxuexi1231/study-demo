package com.hundsun.demo.java.pattern.decorator;

public class Rectangle implements Shape {
 
   @Override
   public void draw() {
      System.out.println("Shape: Rectangle");
   }
}