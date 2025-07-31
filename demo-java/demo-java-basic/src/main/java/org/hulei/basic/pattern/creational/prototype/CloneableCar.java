package org.hulei.basic.pattern.creational.prototype;

import org.hulei.basic.pattern.creational.Car;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.create.prototype
 * @className: CloneableCar
 * @description: 原型模式
 * @author: h1123
 * @createDate: 2023/2/5 18:46
 */

public class CloneableCar extends Car implements Cloneable, Serializable {

    @Override
    public CloneableCar clone() {
        try {
            return (CloneableCar) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public CloneableCar deepClone() {

        // 使用序列化技术实现深克隆, 将对象写入流中, 再从流中取出
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bao);
            oos.writeObject(this);

            ByteArrayInputStream bis = new ByteArrayInputStream(bao.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);

            return (CloneableCar) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
