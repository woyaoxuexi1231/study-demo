package org.hulei.entity.jpa.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;
@Accessors(chain = true)
@Getter
@Setter
@Embeddable
public class OrderDetailId implements java.io.Serializable {

    private static final long serialVersionUID = 2340275294638244092L;
    @NotNull
    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @Size(max = 15)
    @NotNull
    @Column(name = "product_code", nullable = false, length = 15)
    private String productCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetailId entity = (OrderDetailId) o;
        return Objects.equals(this.orderNumber, entity.orderNumber) &&
                Objects.equals(this.productCode, entity.productCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, productCode);
    }

}