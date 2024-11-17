package org.hulei.entity.jpa.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(chain = true)
@Data
@Entity
@Table(name = "order_details", schema = "test")
public class OrderDetail {

    @EmbeddedId
    private OrderDetailId id;

    @NotNull
    @Column(name = "quantity_ordered", nullable = false)
    private Integer quantityOrdered;

    @NotNull
    @Column(name = "price_each", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceEach;

    @NotNull
    @Column(name = "order_line_number", nullable = false)
    private Short orderLineNumber;

}