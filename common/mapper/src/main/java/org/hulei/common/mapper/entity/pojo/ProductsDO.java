package org.hulei.common.mapper.entity.pojo;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.model.pojo
 * @className: ProductsDO
 * @description:
 * @author: h1123
 * @createDate: 2023/2/18 16:37
 */

@Entity
@Table(name = "products")
@Data
public class ProductsDO {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "productCode")
    private String productCode;
    @Basic
    @Column(name = "productName")
    private String productName;
    @Basic
    @Column(name = "productLine")
    private String productLine;
    @Basic
    @Column(name = "productScale")
    private String productScale;
    @Basic
    @Column(name = "productVendor")
    private String productVendor;
    @Basic
    @Column(name = "productDescription")
    private String productDescription;
    @Basic
    @Column(name = "quantityInStock")
    private short quantityInStock;
    @Basic
    @Column(name = "buyPrice")
    private BigDecimal buyPrice;
    @Basic
    @Column(name = "MSRP")
    private BigDecimal msrp;

}
