package org.hulei.jdk.rtjar.generic;

import java.util.ArrayList;
import java.util.List;

class Fruit {
}

class Apple extends Fruit {
}

class Jonathan extends Apple {
}

class Orange extends Fruit {
}


public class GenericsAndCovariance {


    public static void main(String[] args) {
        /*
        首先这里先抛出一点就是泛型没有内建的协变类型
        1. 数组支持协变, 对于 cat extend Animal, 那么可以说 cat[] 是 Animal[] 的子类型
        2. 泛型是不支持协变的, 所以对于 List<Cat> 并不能说他是 List<Animal> 的子类型
         */

        // 这里实际声明了一个 Apple 类型的数组,然后我们用 Fruit 父类来接收
        Fruit[] fruit = new Apple[10];
        // fruit数组虽然使用Fruit接收的,但是实际上他仍然是Apple数组,所有添加Apple或者他的子类是被允许的
        fruit[0] = new Apple(); // OK
        fruit[1] = new Jonathan(); // OK

        // 添加Apple或其子类的其他类型都将失败
        try {
            // Compiler allows you to add Fruit:
            fruit[0] = new Fruit(); // ArrayStoreException
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // Compiler allows you to add Oranges:
            fruit[0] = new Orange(); // ArrayStoreException
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 这种操作是不被允许的,因为泛型不支持协变,所以List<Apple>并不能被赋值给List<Fruit>
        // List<Apple> appleList = new ArrayList<>();
        // List<Fruit> fruitList = appleList;


        // 现在的问题是,对于这种情况,我能够知道的是List存储的是某一个继承自Fruit或者Fruit本身的对象,那么这个对象到底是什么呢?List需要知道一个确切的对象
        // 所以现在List犯难了,他不知道具体的类型是什么,这导致他认为你放入的一切对象都似乎不能完全跟自己定义的类型划等号,所以编译器决定拒绝一切对象来保证安全
        List<? extends Fruit> flist = new ArrayList<>();
        // 这里将发生编译报错: 不能放入任何元素
        // List内部存放的是Fruit或者Fruit的子类,我们如果放入Apple类型,除了List<Apple>/List<Fruit>这种数组我们可以安全的放入对象,其他任何一种数组都不能安全的放入Apple对象
        // 对于其他任何类型都有类似的情况,编译器为了解决这种不安全的操作,禁止我们放入任何对象
        // flist.add(new Apple()); // 编译报错
        // flist.add(new Fruit()); // 编译报错
        // 对于获取操作是不受影响的,因为我们能确定的是这个数组内部存储的是Fruit或者Fruit的子类,我们可以用Fruit类型接收数组里的任何对象
        flist.add(null);
        Fruit f = flist.get(0);

        // 对于下界,编译器允许的行为是可以存放元素,但是不能读取元素
        // 已知:数组存放的是Apple类型或者是Apple类型的父类.
        // 从这个条件可以知道的我们放入Apple类型或者任何Apple类型的子类都是可行的,因为任何Apple类型或者Apple类型的子类放入这个数组也是安全的
        // 但是做不到是不能从中获取任何元素,因为我们无法确定具体的类型是什么,这导致无法获取元素
        List<? super Apple> apples = new ArrayList<>();
        apples.add(new Apple());
        apples.add(new Jonathan());
        // 不过通过Object是可以安全接收的
        Object o = apples.get(0);
    }

}
