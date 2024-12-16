package org.hulei.springcloud.apisixadmin;

import java.util.Map;

public class Node {
    private String host;
    private int port;
    private int weight;

    public Node(String host, int port, int weight) {
        this.host = host;
        this.port = port;
        this.weight = weight;
    }

    // Getters and Setters

    @Override
    public String toString() {
        return "Node{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", weight=" + weight +
                '}';
    }
}
