package org.hulei.springdata.jpa.controller;

import org.hulei.springdata.jpa.dao.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/10/10 22:58
 */

@RestController
public class SimplerController {

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping(value = "/selectEmployees")
    public void selectEmployees() {
        employeeRepository.findAll().forEach(System.out::println);
    }
}
