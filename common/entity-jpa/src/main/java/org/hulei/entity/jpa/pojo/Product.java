package org.hulei.entity.jpa.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
@Accessors(chain = true)
@Data
@Entity
@Table(name = "products", schema = "test")
public class Product {

    @Id
    @Size(max = 15)
    @Column(name = "product_code", nullable = false, length = 15)
    private String productCode;

    @Size(max = 70)
    @NotNull
    @Column(name = "product_name", nullable = false, length = 70)
    private String productName;

    @Size(max = 50)
    @NotNull
    @Column(name = "product_line", nullable = false, length = 50)
    private String productLine;

    @Size(max = 10)
    @NotNull
    @Column(name = "product_scale", nullable = false, length = 10)
    private String productScale;

    @Size(max = 50)
    @NotNull
    @Column(name = "product_vendor", nullable = false, length = 50)
    private String productVendor;

    @NotNull
    @Lob
    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @NotNull
    @Column(name = "quantity_in_stock", nullable = false)
    private Short quantityInStock;

    @NotNull
    @Column(name = "buy_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal buyPrice;

    @NotNull
    @Column(name = "msrp", nullable = false, precision = 10, scale = 2)
    private BigDecimal msrp;

}