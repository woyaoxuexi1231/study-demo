package org.hulei.springdata.jdbc.dao;

import org.hulei.springdata.jdbc.entity.EmployeeDO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hulei
 * @since 2024/10/10 15:43
 */

@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeDO, Long> {

    List<EmployeeDO> findByLastName(String name);
}
