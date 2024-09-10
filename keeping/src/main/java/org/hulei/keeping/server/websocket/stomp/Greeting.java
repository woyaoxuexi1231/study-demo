package org.hulei.keeping.server.websocket.stomp;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.utils.websocket.stomp
 * @className: Greeting
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/11 22:02
 */

public class Greeting {

    private String content;

    public Greeting() {
    }

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
