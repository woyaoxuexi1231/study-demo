package org.hulei.common.mapper.entity.pojo;

import lombok.Data;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Column(name = "product_code")
    private String productCode;
    @Basic
    @Column(name = "product_name")
    private String productName;
    @Basic
    @Column(name = "product_line")
    private String productLine;
    @Basic
    @Column(name = "product_scale")
    private String productScale;
    @Basic
    @Column(name = "product_vendor")
    private String productVendor;
    @Basic
    @Column(name = "product_description")
    private String productDescription;
    @Basic
    @Column(name = "quantity_in_stock")
    private short quantityInStock;
    @Basic
    @Column(name = "buy_price")
    private BigDecimal buyPrice;
    @Basic
    @Column(name = "MSRP")
    private BigDecimal msrp;

}
