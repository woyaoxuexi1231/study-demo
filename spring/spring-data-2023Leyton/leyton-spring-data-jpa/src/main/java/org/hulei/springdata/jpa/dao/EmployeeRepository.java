package org.hulei.springdata.jpa.dao;

import org.hulei.entity.jpa.pojo.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hulei
 * @since 2024/10/10 22:42
 */

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

}
