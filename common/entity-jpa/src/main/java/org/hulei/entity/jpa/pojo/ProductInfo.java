package org.hulei.entity.jpa.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
@Accessors(chain = true)
@Data
@Entity
@Table(name = "product_info", schema = "test")
public class ProductInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Size(max = 50)
    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Lob
    @Column(name = "description")
    private String description;

}