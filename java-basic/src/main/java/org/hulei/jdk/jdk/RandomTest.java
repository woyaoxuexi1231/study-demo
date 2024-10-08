package org.hulei.jdk.jdk;

import java.util.Random;

/**
 * @author hulei
 * @since 2024/9/25 21:30
 */

public class RandomTest {

    public static void main(String[] args) {
        new Thread(() -> {
            Random rand = new Random();
            for (int i = 0; i < 10; i++) {
                int nextInt = rand.nextInt();
                System.out.println(nextInt < 0 ? nextInt >>> 1 : nextInt);
            }
        }).start();
        new Thread(() -> {
            Random rand = new Random();
            for (int i = 0; i < 10; i++) {
                int nextInt = rand.nextInt();
                System.out.println(nextInt < 0 ? nextInt >>> 1 : nextInt);
            }
        }).start();
    }
}
