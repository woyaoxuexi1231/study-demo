package org.hulei.springdata.jdbc.controller;

import org.hulei.springdata.jdbc.dao.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/10/10 16:03
 */


@RestController
public class SimpleController {

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping("/crudRepositoryTest")
    public void crudRepositoryTest(String lastName) {
        employeeRepository.findByLastName(lastName).forEach(System.out::println);
    }
}
