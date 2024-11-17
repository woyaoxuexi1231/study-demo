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

import java.time.LocalDate;

@Accessors(chain = true)
@Data
@Entity
@Table(name = "orders", schema = "test")
public class Order {
    @Id
    @Column(name = "order_number", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @NotNull
    @Column(name = "required_date", nullable = false)
    private LocalDate requiredDate;

    @Column(name = "shipped_date")
    private LocalDate shippedDate;

    @Size(max = 15)
    @NotNull
    @Column(name = "status", nullable = false, length = 15)
    private String status;

    @Lob
    @Column(name = "comments")
    private String comments;

    @NotNull
    @Column(name = "customer_number", nullable = false)
    private Integer customerNumber;

}