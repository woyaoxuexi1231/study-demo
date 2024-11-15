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
public class PaymentId implements java.io.Serializable {

    private static final long serialVersionUID = 418299435876157072L;
    @NotNull
    @Column(name = "customer_number", nullable = false)
    private Integer customerNumber;

    @Size(max = 50)
    @NotNull
    @Column(name = "check_number", nullable = false, length = 50)
    private String checkNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentId entity = (PaymentId) o;
        return Objects.equals(this.checkNumber, entity.checkNumber) &&
                Objects.equals(this.customerNumber, entity.customerNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkNumber, customerNumber);
    }

}