package org.hulei.basic.jdk.java8.lambda;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hulei
 * @since 2024/12/23 22:36
 */

@Slf4j
public class CloneableTest {
    public static void main(String[] args) {
        /*
        关于克隆：Java中对于克隆分为深克隆和浅克隆
            浅克隆：浅克隆只复制对象本身及其字段，对于字段中的引用类型，只复制引用，不复制对象。换句话说，浅克隆会共享引用类型字段所指向的对象。
            深克隆：深克隆不仅复制对象本身，还递归地复制对象内部所有引用类型的字段所指向的对象，保证克隆对象与原对象完全独立。
         */
        SimplePerson simplePerson = new SimplePerson("tom", 21, new Pet("cat"));
        // 进行浅克隆

        SimplePerson clone = null;
        try {
            clone = simplePerson.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        SimplePerson deepClone = null;
        try {
            deepClone = simplePerson.deepClone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        // 修改源对象内部的值，浅克隆的对象也会跟着变，因为浅克隆只克隆了引用
        simplePerson.setAge(22);
        simplePerson.getPet().setName("dog");

        // 浅克隆对象内部的可变对象会跟着源对象发生改变
        log.info("clone: {}", clone);

        // 深克隆的对象已经完全独立
        log.info("deepClone: {}", deepClone);
    }

    @ToString
    @Getter
    @Setter
    static class SimplePerson implements Cloneable {
        String name;
        Integer age;
        Pet pet;

        public SimplePerson(String name, Integer age, Pet pet) {
            this.name = name;
            this.age = age;
            this.pet = pet;
        }

        /**
         * 重新专门写一个深克隆的方法，这里仅作演示，这些逻辑应该写在 clone 方法里
         *
         * @return 深克隆对象
         * @throws CloneNotSupportedException 已调用类 Object 中的 clone 方法来克隆对象，但该对象的类未实现 Cloneable 接口。覆盖 clone 方法的应用程序也可以引发此异常，以指示无法或不应克隆对象。
         */
        public SimplePerson deepClone() throws CloneNotSupportedException {

            SimplePerson clone = (SimplePerson) super.clone();

            // 要实现深克隆，一定要克隆对象中所有可变的实例字段，这里再继续克隆宠物对象
            Pet petClone = pet.clone();
            clone.setPet(petClone);

            return clone;
        }

        @Override
        public SimplePerson clone() throws CloneNotSupportedException {
            // 如果仅仅这样返回，依旧是浅克隆，调用 protected native Object clone() 这个方法，底层实现通过直接操作内存来复制对象的状态，可以得到一个新的对象，但不会递归地复制对象引用的对象
            return (SimplePerson) super.clone();
        }
    }

    @ToString
    @Getter
    @Setter
    static class Pet implements Cloneable {

        String name;

        public Pet(String name) {
            this.name = name;
        }

        @Override
        protected Pet clone() throws CloneNotSupportedException {
            return (Pet) super.clone();
        }
    }
}
