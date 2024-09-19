package org.hulei.keeping.server.spring.validate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.controller
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-01-05 10:58
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@Slf4j
@RestController
@RequestMapping("/validate")
// @Validated
public class ValidateController {

    @GetMapping("/simpleValidate")
    public void simpleValidate(User user) {
        this.createUser(user);
    }

    // public void createUser(User user) {
    //     ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    //     Validator validator = factory.getValidator();
    //     Set<ConstraintViolation<User>> violations = validator.validate(user);
    //
    //     if (violations.isEmpty()) {
    //         log.info("数据合法，执行相应的逻辑");
    //     } else {
    //         log.error("数据不合法，进行相应的处理");
    //     }
    // }

    @PostMapping("/users")
    public String createUser(@Valid @RequestBody User user) {
        // 处理用户创建逻辑
        return "User created successfully!";
    }

    @Data
    public static class User {

        @NotEmpty
        private String name;

        @Email
        private String email;

        // 其他属性和方法
    }
}
