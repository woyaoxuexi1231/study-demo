package org.hulei.jdk.root.generic;// generics/SuperTypeWildcards.java
// (c)2021 MindView LLC: see Copyright.txt
// We make no guarantees that this code is fit for any purpose.
// Visit http://OnJava8.com for more book information.

import java.util.ArrayList;
import java.util.List;

public class SuperTypeWildcards {

    static void writeTo(List<? super Apple> apples) {
        apples.add(new Apple());
        apples.add(new Jonathan());
        // apples.add(new Fruit()); // Error
    }

    public static void main(String[] args) {
        /*
        超类型通配符, super, 对于 ? super Apple, 我们能够知道的是, 这个 List 内保存的是 Apple类或者是Apple类的超类
        那我们能知道的是,不论是保存 Apple类还是 Apple类的子类都是符合这个条件的

        可以想象如下:
        这个List的具体类型不管如何定义,我们都能够安全的放入下界,以及下界的子类
        相反对于extend关键字,我们只知道上界是Apple,但是如果具体的类型是任何一种子类,我们都无法安全的放入任何一种类型:
            因为List<Jonathan>不能放入Apple,不能放入任何除了Jonathan之外的类型
            相反List<Apple>或者List<Object>可以放入任何Apple类型或者其子类
        对于获取元素来说:
            对于extend关键字,我们知道这个List内部放的是任何有关于Apple或者其子类,所以获取元素可以直接使用Apple类作为接收
            但是对于super关键字,我们仅仅只是知道内部放的是Apple或其超类,但是并不知道具体是什么类,在获取元素的时候举个例子就是
                如果实际类型是Fruit,我们就无法使用Apple来接收 这是一种不安全的接收方式
         */
        List<? super Apple> apples = new ArrayList<>();
        writeTo(apples);

        Fruit object = (Fruit) apples.get(0);
    }
}
