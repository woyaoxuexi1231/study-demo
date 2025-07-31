package org.hulei.springboot.utils;

import com.googlecode.aviator.AviatorEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionTokenizer {

    public static String tokenizeExpressionPreservingOrder(String expression) {
        // 定义模式以匹配数字（整数或浮点数）和运算符
        Pattern tokenPattern = Pattern.compile("\\b\\d+(?:\\.\\d*)?|[+\\-*/()]");

        // 创建一个列表来保存所有的标记（数字和运算符）
        List<String> tokens = new ArrayList<>();

        // 使用正则表达式查找所有标记
        Matcher matcher = tokenPattern.matcher(expression);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        StringBuilder sb = new StringBuilder();
        for (String token : tokens) {
            sb.append(token);
        }
        System.out.println("处理后" + sb);
        return sb.toString();
    }

    public static boolean isNumberStrict(String str) {
        // 正则表达式解释：
        // ^ 表示字符串的开始
        // [+-]? 表示可选的正负号
        // (?:0|[1-9]\d*) 表示整数部分（零或至少一位非零数字后跟任意数量的数字）
        // (?:\.\d+)? 表示可选的小数部分（小数点后跟至少一位数字）
        // $ 表示字符串的结束
        String regex = "^[+-]?(?:0|[1-9]\\d*)(?:\\.\\d+)?$";
        return str != null && str.matches(regex);
    }

    public static void main(String[] args) {
        // 测试表达式
        // String expression = "1+2/3*5+3/4+2";
        String expression = "3 + 2 * 5 / 3";

        System.out.println(AviatorEvaluator.execute(tokenizeExpressionPreservingOrder(expression)));
        System.out.println(isNumberStrict("12345.3"));
    }
}