package org.hulei.entity.jpa.starter.dao;

import org.hulei.entity.jpa.pojo.BigDataUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hulei
 * @since 2025/8/7 21:54
 */

@Repository
public interface BigDataUserRepository extends JpaRepository<BigDataUser, Long> {

    @Query(value = "select * from big_data_users limit 0, 100", nativeQuery = true)
    public List<BigDataUser> fetchTop100();

    @Query(value = "select * from big_data_users limit 0, 1", nativeQuery = true)
    public BigDataUser fetchOne();

    // 分页查询（自动处理分页逻辑）
    @Query(value = "select * from big_data_users", nativeQuery = true)
    Page<BigDataUser> findDataPage(Pageable pageable);

    // 多条件分页
    @Query(value = "SELECT * FROM big_data_users u WHERE u.name LIKE CONCAT('%', :name, '%')", nativeQuery = true)
    Page<BigDataUser> findByNameContaining(@Param("name") String name, Pageable pageable);
}
