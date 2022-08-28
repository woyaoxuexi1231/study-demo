package com.hundsun.demo.java.pattern.simplefac;

import com.hundsun.demo.java.pattern.Car;
import com.hundsun.demo.java.pattern.Ferrari;
import com.hundsun.demo.java.pattern.Lamborghini;

public class CarStaticFactory {

    public Car getCar(String carName) {
        switch (carName) {
            case "Ferrari":
                return new Ferrari();
            case "Lamborghini":
                return new Lamborghini();
            default:
                return new Car();
        }
    }
}
