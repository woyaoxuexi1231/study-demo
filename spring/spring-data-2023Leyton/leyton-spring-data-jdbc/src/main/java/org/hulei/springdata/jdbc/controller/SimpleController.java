package org.hulei.springdata.jdbc.controller;

import org.hulei.springdata.jdbc.dao.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/10/10 16:03
 */


@RestController
public class SimpleController {

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping("/getEmployeeByName")
    public void getEmployeeByName(@RequestParam(value = "lastName") String lastName) {
        /*
        这里使用 spring-boot-starter-data-jdbc 这个依赖并不依赖于 jpa 的规范, 而是 spring 自己抽了一套规范出来
        org.springframework.data.relational.core.mapping
         */
        employeeRepository.findByLastName(lastName).forEach(System.out::println);
    }
}
