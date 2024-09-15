package org.hulei.jdk.root.generic.work;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.jdk
 * @className: Generic
 * @description:
 * @author: hl
 * @createDate: 2023/5/20 11:41
 */

public class GenericMain {

    public static void main(String[] args) {

    }

    public void animal(String[] args) {

        Dog[] dogs = new Dog[5];
        dogs[0] = new Dog();
        dogs[1] = new Dog();
        Animal[] animals = dogs; // 如果支持协变，可以将 Dog[] 赋值给 Animal[]
        animals[2] = new Animal(); // 运行时出现 ArrayStoreException 异常
        /*
        在上述示例中，我们声明了一个`Dog`类型的数组`dogs`，并将其前两个元素初始化为`Dog`对象。
        然后，我们将`dogs`赋值给`Animal`类型的数组`animals`，由于数组支持协变，可以将`Dog[]`赋值给`Animal[]`。
        但是，当我们对`animals`数组进行操作时，会发生`ArrayStoreException`异常，
        因为我们将一个`Animal`对象存储在`animals`数组的第三个位置，而这是一个不合法的操作，因为`Dog`数组只能存储`Dog`对象。

        如果数组不支持协变，那么编译过程中会检测到类型不匹配的错误，从而我们无法将`Dog[]`赋值给`Animal[]`。但是，由于数组支持协变，运行时才会出现异常，给我们带来了一些麻烦。

        综上所述，支持协变意味着子类型可以隐式地转换为超类型，但这也给数组带来了一些安全隐患。当我们使用带有泛型的类型时，需要小心处理数组的协变性，避免运行时出现异常。
        */
        List<Dog> dogList = new ArrayList<>();
        dogList.add(new Dog());
        dogList.add(new Dog());
        // List<Animal> animalList = dogList; // 编译错误：Type mismatch
        // animalList.add(new Animal()); // 运行时出现异常
        /*
        在这个示例中，我们声明了一个List<Dog>类型的列表dogs，并向其中添加了两个Dog对象。
        然后，我们尝试将dogs赋值给List<Animal>类型的列表animals，但是编译器会报错：类型不匹配（Type mismatch），因为List<Dog>和List<Animal>是不兼容的类型。

        如果泛型支持协变，那么这段代码在编译时可能会通过，但是在运行时出现异常。因为我们可以通过animals列表向其中添加一个Animal对象，但是这对于原始的dogs列表来说是不合法的。

        为了避免在运行时出现错误，Java 的泛型系统选择了不支持协变，而是在编译期间强制执行类型检查，确保我们只能往泛型列表中添加与泛型类型相匹配的对象。这提供了更高的类型安全性，减少了出错的可能性。

        总结来说，泛型不支持协变是为了在编译期间准确捕捉类型不匹配的错误，以提高代码的可靠性和类型安全性。这样可以避免在运行时出现类型转换错误或其他相关的异常。
        */
    }

    class Animal {
        public void eat() {
            System.out.println("Animal eats.");
        }
    }

    class Dog extends Animal {
        public void bark() {
            System.out.println("Dog barks.");
        }
    }

    /* ************************************************************************************************************************* */

    public <Stand, Transfer> void doProcess() {
        /*
        编译报错
        Required type:Function<DataTransferReq<Stand>,Transfer>
        Provided:Function<DataTransferReq<?>,capture of ?>
         */
        // Function<DataTransferReq<Stand>, Transfer> subProcess = this.getSubProcess();
        // this.singleDataProcess(subProcess);
    }

    public <Stand, Transfer> void singleDataProcess(Function<DataTransferReq<Stand>, Transfer> subProcess) {
    }

    public Function<DataTransferReq<?>, ?> getSubProcess() {
        return (req) -> null;
    }

    public <Stand, Transfer> void doProcess2() {
        this.singleDataProcess2(this.getSubProcess2());
    }

    public <Stand, Transfer extends DataTransfer<Stand>> void singleDataProcess2(Function<DataTransferReq<Stand>, Transfer> subProcess) {

    }

    public <Stand, Transfer extends DataTransfer<Stand>> Function<DataTransferReq<Stand>, Transfer> getSubProcess2() {
        return (req) -> null;
    }

    class DataTransferReq<T> {

    }

    class DataTransfer<T> {

    }

    /* ************************************************************************************************************************* */
    public <Stand> void doMsgProcess() {
        // Consumer<?> msgProcess = this.getMsgProcess();
        Consumer<Stand> msgProcess = this.getStandConsumer();
        Consumer<DataTransferReq<Stand>> msgProcess3 = this.getExtendDataTransferReqWildcardConsumer();

        /*
        第一个情况中的强制类型转换是允许的，因为编译器认为任何Consumer<?>类型都可以被转换成Consumer<Stand>。
        而在第二个情况中，编译器拒绝了赋值操作，因为Consumer<DataTransferReq<?>>和Consumer<DataTransferReq<Stand>>在Java泛型中不是兼容的类型。
         */
        Consumer<Stand> msgProcess4 = (Consumer<Stand>) this.getWildcardConsumer();
        // Consumer<DataTransferReq<Stand>> msgProcess2 = (Consumer<DataTransferReq<Stand>>) this.getMsgProcess2(); // compiler failed

        this.singleDataProcess(this.getStandConsumer());
        // 无法推断
        // reason: Incompatible equality constraint: Transfer and ?
        // this.singleDataProcess2(this.getMsgProcess2());
        // 正常推断
        this.singleDataProcess(this.getExtendDataTransferReqWildcardConsumer());
    }

    public <Stand> Consumer<Stand> getStandConsumer() {
        return transfer -> {

        };
    }

    public Consumer<?> getWildcardConsumer() {
        return transfer -> {

        };
    }

    public Consumer<DataTransferReq<?>> getDataTransferReqWildcardConsumer() {
        return transfer -> {

        };
    }

    public <Stand, Transfer extends DataTransferReq<Stand>> Consumer<Transfer> getExtendDataTransferReqWildcardConsumer() {
        return transfer -> {

        };
    }

    public <Stand, Transfer extends DataTransferReq<Stand>> Consumer<Transfer> getMsgProcess4() {
        return transfer -> {

        };
    }

    public <Stand, Transfer extends DataTransferReq<Stand>> void singleDataProcess(Consumer<Transfer> msgProcess) {
    }

/*
您提出的问题非常好，我将详细解释这两个例子之间的区别。

在第一个例子中，我们有以下代码片段：

```java
public void doProcess() {
    this.singleDataProcess(this.getSubProcess());
}

public <Stand, Transfer> void singleDataProcess(Function<Stand, Transfer> subProcess) {
}

public Function<?, ?> getSubProcess() {
    return (req) -> null;
}
```

在这个例子中，虽然`Function<?, ?>`表示一个未知的通配符类型，但这个通配符类型可以匹配到`Function<Stand, Transfer>`，因为`Function<Stand, Transfer>`是一个函数类型，可以接受任何输入类型为`Stand`或其子类型的函数，并返回一个`Transfer`类型的结果。
换句话说，`Function<Stand, Transfer>`是一个能够处理任意类型的函数的类型，而`Function<?, ?>` 表示一个可以接受任意输入和输出类型的函数的类型。因此，`Function<?, ?>` 可以被匹配到 `Function<Stand, Transfer>`，编译器不会报错。

在第二个例子中，我们有以下代码片段：

```java
public void doProcess() {
    this.singleDataProcess(this.getSubProcess());
}

public <Stand, Transfer> void singleDataProcess(Function<DataTransferReq<Stand>, Transfer> subProcess) {
}

public Function<DataTransferReq<?>, ?> getSubProcess() {
    return (req) -> null;
}

class DataTransferReq<T> {

}
```

在这个例子中，`singleDataProcess()`方法定义了一个范型类型参数`<Stand, Transfer>`，它期望接受一个`Function<DataTransferReq<Stand>, Transfer>`类型的参数。而`getSubProcess()`方法返回的是一个`Function<DataTransferReq<?>, ?>`类型的函数。在这种情况下，编译器无法将`Function<DataTransferReq<?>, ?>`类型的参数匹配到`Function<DataTransferReq<Stand>, Transfer>`，因为`DataTransferReq<Stand>`和`DataTransferReq<?>`不是相同的类型。

具体来说：
- `Function<DataTransferReq<Stand>, Transfer>` 表示一个能够接受输入类型为 `DataTransferReq<Stand>` 并返回类型为 `Transfer` 的函数。
- `Function<DataTransferReq<?>, ?>` 表示一个能够接受任意输入类型为 `DataTransferReq<?>` 并返回任意类型的函数。

这两个类型是不同的，因为 `?` 通配符表示未知类型，无法确定 `DataTransferReq<Stand>` 和 `DataTransferReq<?>` 是相同类型或具有继承关系。编译器因为无法确定具体的类型参数，所以会给出编译错误。

简而言之，出现编译错误的原因是因为：
- 在第一个例子中，通常情况下，我们可以将`Function<?, ?>`类型参数匹配到`Function<Stand, Transfer>`类型，因为`Function<Stand, Transfer>`可以接受任何类型的函数。
- 在第二个例子中，`DataTransferReq<Stand>`和`DataTransferReq<?>`是不同的类型，无法将`Function<DataTransferReq<?>, ?>`类型参数匹配到`Function<DataTransferReq<Stand>, Transfer>`。

希望这次能够更清楚地解释了，如果还有任何疑问，请随时提出。我会尽力帮助您理解。
 */

}
