package org.hulei.springcloud.apisixadmin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonAdapter {

    public static List<Node> parseNodes(String jsonString) {
        List<Node> nodes = new ArrayList<>();

        JSONObject jsonObject = JSON.parseObject(jsonString);

        // 检查 "nodes" 属性是数组还是对象
        Object nodesObject = jsonObject.get("nodes");

        if (nodesObject instanceof JSONArray nodeArray) {
            // 情况 1: "nodes" 是一个数组
            for (int i = 0; i < nodeArray.size(); i++) {
                JSONObject nodeJson = nodeArray.getJSONObject(i);
                String host = nodeJson.getString("host");
                int port = nodeJson.getIntValue("port");
                int weight = nodeJson.getIntValue("weight");
                nodes.add(new Node(host, port, weight));
            }
        } else if (nodesObject instanceof JSONObject nodeMap) {
            // 情况 2: "nodes" 是一个对象
            for (Map.Entry<String, Object> entry : nodeMap.entrySet()) {
                String hostPort = entry.getKey();
                int weight = (Integer) entry.getValue();

                // 分离出 host 和 port
                String[] parts = hostPort.split(":");
                String host = parts[0];
                int port = Integer.parseInt(parts[1]);

                nodes.add(new Node(host, port, weight));
            }
        }

        return nodes;
    }

    public static void main(String[] args) {

        String json = "{\n" +
                "  \"value\" : {\n" +
                "    \"update_time\" : 1731483218,\n" +
                "    \"upstream\" : {\n" +
                "      \"retries\" : 2,\n" +
                "      \"nodes\" : {\n" +
                "        \"192.168.3.233:12101\" : 1\n" +
                "      },\n" +
                "      \"hash_on\" : \"vars\",\n" +
                "      \"scheme\" : \"http\",\n" +
                "      \"type\" : \"roundrobin\",\n" +
                "      \"pass_host\" : \"pass\"\n" +
                "    },\n" +
                "    \"create_time\" : 1731483218,\n" +
                "    \"methods\" : [ \"GET\", \"POST\", \"PUT\", \"DELETE\", \"PATCH\", \"HEAD\", \"OPTIONS\", \"CONNECT\", \"TRACE\", \"PURGE\" ],\n" +
                "    \"plugins\" : {\n" +
                "      \"limit-count\" : {\n" +
                "        \"allow_degradation\" : false,\n" +
                "        \"rejected_code\" : 429,\n" +
                "        \"time_window\" : 60,\n" +
                "        \"key_type\" : \"var\",\n" +
                "        \"count\" : 2,\n" +
                "        \"rejected_msg\" : \"Too Many Requests\",\n" +
                "        \"show_limit_quota_header\" : true,\n" +
                "        \"key\" : \"arg_user\",\n" +
                "        \"policy\" : \"local\"\n" +
                "      },\n" +
                "      \"ext-plugin-pre-req\" : {\n" +
                "        \"allow_degradation\" : false,\n" +
                "        \"conf\" : [ {\n" +
                "          \"name\" : \"TokenValidator\",\n" +
                "          \"value\" : \"\"\n" +
                "        } ]\n" +
                "      },\n" +
                "      \"ext-plugin-post-resp\" : {\n" +
                "        \"allow_degradation\" : false,\n" +
                "        \"conf\" : [ {\n" +
                "          \"name\" : \"RspBodyValidator\",\n" +
                "          \"value\" : \"\"\n" +
                "        } ]\n" +
                "      }\n" +
                "    },\n" +
                "    \"name\" : \"route-demo-test\",\n" +
                "    \"id\" : \"00000000000000000897\",\n" +
                "    \"priority\" : 1,\n" +
                "    \"uri\" : \"/hi\",\n" +
                "    \"status\" : 1\n" +
                "  },\n" +
                "  \"key\" : \"/apisix/routes/00000000000000000897\"\n" +
                "}";

        parseNodes(json);
    }
}
