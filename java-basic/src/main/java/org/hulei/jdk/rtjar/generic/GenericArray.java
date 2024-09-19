package org.hulei.jdk.rtjar.generic;// generics/GenericArray.java
// (c)2021 MindView LLC: see Copyright.txt
// We make no guarantees that this code is fit for any purpose.
// Visit http://OnJava8.com for more book information.

public class GenericArray<T> {
    private final T[] array;

    @SuppressWarnings("unchecked")
    public GenericArray(int sz) {
        array = (T[]) new Object[sz];
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index) {
        return array[index];
    }

    // Method that exposes the underlying representation:
    public T[] rep() {
        return array;
    }

    public static void main(String[] args) {
        /*
        这里泛型类型内部的 T 类型已经被擦除了, (T[]) new Object[sz] 其实是 (Object[]) new Object[sz] 所以没有任何问题
        但是 Integer[] ia = gai.rep(); 就出现问题了 泛型类内部仅持有Object数组,转型为Integer数组有些强人所难了
         */
        GenericArray<Integer> gai = new GenericArray<>(10);
        try {
            Integer[] ia = gai.rep();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        // This is OK:
        Object[] oa = gai.rep();
    }
}
/* Output:
[Ljava.lang.Object; cannot be cast to
[Ljava.lang.Integer;
*/
