package org.hulei.mybatis.spring.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hulei.entity.jpa.pojo.BigDataOrder;
import org.hulei.entity.jpa.pojo.BigDataUser;

import java.util.List;

/**
 * @author hulei
 * @since 2024/9/26 15:53
 */

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class UserWithOrder extends BigDataUser {

    List<BigDataOrder> customers;
}
