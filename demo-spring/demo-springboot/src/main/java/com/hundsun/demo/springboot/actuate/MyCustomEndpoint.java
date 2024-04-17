package com.hundsun.demo.springboot.actuate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@Endpoint(id = "mycustomendpoint")
public class MyCustomEndpoint {

    @ReadOperation
    public String myCustomEndpoint() {
        return "Hello from my custom endpoint!";
    }
}
