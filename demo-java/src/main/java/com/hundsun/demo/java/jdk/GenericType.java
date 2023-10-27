package com.hundsun.demo.java.jdk;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.jdk
 * @className: GenericType
 * @description:
 * @author: h1123
 * @createDate: 2023/10/28 1:04
 */

public class GenericType {
/*
当使用泛型时，我们可以使用反射来获取泛型参数的类型信息。下面是一个示例，展示如何在运行时获取泛型参数的类型信息：

```java
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GenericExample<T> {
    public static void main(String[] args) {
        List<String> stringList = new ArrayList<>();

        Type type = stringList.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (typeArguments.length > 0) {
                Type typeArgument = typeArguments[0];
                System.out.println("Type argument: " + typeArgument.getTypeName());
            }
        }
    }
}
```

在上的示例中，我们创建了一个带有泛型参数的`List`对象 `stringList`。我们通过反射获取了 `stringList` 对象的类型信息。
首先，我们使用`getClass()`方法获取`List`对象的`Class`对象。然后，我们使用`getGenericSuperclass()`方法来获取`List`对象的父类，也就是参数化的`List`接口。接着，我们检查父类是否是`ParameterizedType`类型。
如果父类是`ParameterizedType`类型，表示该类型是带有参数化的泛型类型。我们可以使用`getActualTypeArguments()`方法获取参数的类型信息。在这个示例中，我们将只有一个类型参数，所以我们可以获取第一个类型参数 `typeArgument` 并输出它的名称。
输出结果将是 `java.lang.String`，这是 `List`的实际类型参数。
通过使用反射和`ParameterizedType`，我们可以在运行时获取到泛型参数的类型信息，从而对泛型进行更灵活的操作。
 */

}
