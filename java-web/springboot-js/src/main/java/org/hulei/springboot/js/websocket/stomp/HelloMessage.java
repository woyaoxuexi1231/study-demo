package org.hulei.springboot.js.websocket.stomp;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.utils.websocket.stomp
 * @className: HelloMessage
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/11 22:02
 */

public class HelloMessage {

    private String name;

    public HelloMessage() {
    }

    public HelloMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
