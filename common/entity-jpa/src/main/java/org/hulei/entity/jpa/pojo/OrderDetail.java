package org.hulei.entity.jpa.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Accessors(chain = true)
@Data
@Entity
@Table(name = "test_order_details", schema = "test")
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