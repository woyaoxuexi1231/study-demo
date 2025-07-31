package org.hulei.springboot.utils;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author hulei
 * @since 2024/12/30 16:49
 */

public class JsonUtil {

    // 这个字段如果为空，将不会被序列化
    @JSONField(serialize = false)
    private String exampleField;

    public String getExampleField() {
        return exampleField;
    }

    public void setExampleField(String exampleField) {
        this.exampleField = exampleField;
    }

    public static void main(String[] args) {
        JsonUtil example = new JsonUtil();
        example.setExampleField("just");
        String jsonString = com.alibaba.fastjson.JSON.toJSONString(example);
        System.out.println("Serialized JSON: " + jsonString);
    }
}

