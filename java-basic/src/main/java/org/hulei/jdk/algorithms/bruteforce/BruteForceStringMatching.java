package org.hulei.jdk.algorithms.bruteforce;

/**
 *
 */

public class BruteForceStringMatching {
    public static int bruteForceMatch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();

        // 遍历文本中每个可能的起始位置
        for (int i = 0; i <= n - m; i++) {
            int j;

            // 在当前位置开始，逐个比较文本和模式中的字符
            for (j = 0; j < m; j++) {
                if (text.charAt(i + j) != pattern.charAt(j))
                    break; // 如果有字符不匹配，则跳出内层循环
            }

            if (j == m)
                return i; // 如果内层循环完成，则表示找到了匹配的子串，返回其起始位置
        }

        return -1; // 如果未找到匹配的子串，则返回 -1
    }

    public static void main(String[] args) {
        String text = "hello world";
        String pattern = "world";

        int index = bruteForceMatch(text, pattern);
        if (index != -1) {
            System.out.println("找到匹配的子串，起始位置为：" + index);
        } else {
            System.out.println("未找到匹配的子串");
        }
    }
}
