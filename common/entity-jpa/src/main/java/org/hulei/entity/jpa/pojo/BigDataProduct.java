package org.hulei.entity.jpa.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "big_data_products", schema = "test")
public class BigDataProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 200)
    @Column(name = "name", length = 200)
    private String name;

    @Size(max = 100)
    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}