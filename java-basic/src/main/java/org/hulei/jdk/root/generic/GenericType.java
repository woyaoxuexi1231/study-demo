package org.hulei.jdk.root.generic;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
    输出结果将是 `java.lang.String`，这是 `List`的实际类型参数。--这是错误的
    实际运行结果是 Type argument: E
通过使用反射和`ParameterizedType`，我们可以在运行时获取到泛型参数的类型信息，从而对泛型进行更灵活的操作。
 */


    public static void main(String[] args) {
        List<String> stringList = new ArrayList<>();
        String s = "";
        MyClass<String, Integer> myClass = new MyClass<>();
        System.out.println(stringList.getClass().getTypeParameters());
        System.out.println(s.getClass().getTypeParameters());
        System.out.println(stringList.getClass().getGenericSuperclass());
        System.out.println(s.getClass().getGenericSuperclass());
        System.out.println(stringList.getClass().getGenericInterfaces());
        System.out.println(s.getClass().getGenericInterfaces());


        ArrayList<String> list = new ArrayList<>();

        Type genericType = list.getClass().getGenericSuperclass();

        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();

            if (typeArguments.length > 0) {
                Type typeArgument = typeArguments[0];
                System.out.println("Generic type: " + typeArgument.getTypeName());
            }
        }

        // 这里能获取具体的类型, 所以结果就是对于一个不确定的泛型类型, 无论如何在运行时都无法获取其确切的类型
        // *除非, 能拿到代表这个泛型类型本身的对象, 再通过获取这个泛型对象本身的class信息就能确定其类型
        MyList<String> myList = new MyList<String>() {
        };
        Class<String> myListGenericType = myList.getGenericType();
        System.out.println("Generic type: " + myListGenericType.getName());
    }

    // getTypeParameters() - 使用 getTypeParameters() 方法可以返回一个 TypeVariable 数组，其中包含了定义该类型参数的类型变量。
    // ((ParameterizedTypeImpl) SubMyClass.class.getGenericSuperclass()).getActualTypeArguments() -> 可以得到SubMyClass上写的 String,Integer
}

class MyClass<T, U> {
    public void myMethod(T arg1, U arg2) {
        // 方法体...
    }

    public <R, V> void myMethod2(R arg1, V arg2) {
        // 方法体...
    }
}

class SubMyClass extends MyClass<String, Integer> {

}


class MyList<E> extends ArrayList<E> {
    public Class<E> getGenericType() {
        TypeToken<?> typeToken = TypeToken.of(getClass())
                .resolveType(MyList.class.getTypeParameters()[0]);
        return (Class<E>) typeToken.getRawType();
    }
}