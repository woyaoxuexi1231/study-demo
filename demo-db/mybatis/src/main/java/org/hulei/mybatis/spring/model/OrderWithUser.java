package org.hulei.mybatis.spring.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hulei.entity.jpa.pojo.BigDataOrder;
import org.hulei.entity.jpa.pojo.BigDataUser;

/**
 * @author hulei
 * @since 2024/9/26 23:17
 */

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderWithUser extends BigDataOrder {
    /**
     * 产品线信息
     */
    BigDataUser user;
}
