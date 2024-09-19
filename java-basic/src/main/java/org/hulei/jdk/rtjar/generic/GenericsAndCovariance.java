package org.hulei.jdk.rtjar.generic;// generics/GenericsAndCovariance.java
// (c)2021 MindView LLC: see Copyright.txt
// We make no guarantees that this code is fit for any purpose.
// Visit http://OnJava8.com for more book information.

import java.util.ArrayList;
import java.util.List;

public class GenericsAndCovariance {
    public static void main(String[] args) {
        /*
        首先这里先抛出一点就是泛型没有内建的协变类型
        1. 数组支持协变, 对于 cat extend Animal, 那么可以说 cat[] 是 Animal[] 的子类型
        2. 泛型是不支持协变的, 所以对于 List<Cat> 并不能说他是 List<Animal> 的子类型
         */

        // 现在的问题是,对于这种情况,我能够知道的是List存储的是某一个继承自Fruit或者Fruit本身的对象,那么这个对象到底是什么呢?List需要知道一个确切的对象
        // 所以现在List犯难了,他不知道具体的类型是什么,这导致他认为你放入的一切对象都似乎不能完全跟自己定义的类型划等号,所以编译器决定拒绝一切对象来保证安全
        List<? extends Fruit> flist = new ArrayList<>();
        // Compile Error: can't add any type of object:
        // flist.add(new Apple());
        // flist.add(new Fruit());
        // flist.add(new Object());
        flist.add(null); // Legal but uninteresting
        // We know it returns at least Fruit:
        Fruit f = flist.get(0);
    }
}
