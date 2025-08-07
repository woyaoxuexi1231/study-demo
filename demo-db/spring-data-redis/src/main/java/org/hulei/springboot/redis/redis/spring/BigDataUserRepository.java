package org.hulei.springboot.redis.redis.spring;

import org.hulei.entity.jpa.pojo.BigDataUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hulei
 * @since 2025/8/6 23:07
 */

@Repository
public interface BigDataUserRepository extends CrudRepository<BigDataUser, Long> {
    @Query(value = "select * from big_data_users limit 0, 100", nativeQuery = true)
    public List<BigDataUser> fetchTop100();

    @Query(value = "select * from big_data_users limit 0, 1", nativeQuery = true)
    public BigDataUser fetchOne();
}
