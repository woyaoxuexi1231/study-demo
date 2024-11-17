package org.hulei.entity.jpa.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customer_number", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "customer_name", nullable = false, length = 50)
    private String customerName;

    @Size(max = 50)
    @NotNull
    @Column(name = "contact_last_name", nullable = false, length = 50)
    private String contactLastName;

    @Size(max = 50)
    @NotNull
    @Column(name = "contact_first_name", nullable = false, length = 50)
    private String contactFirstName;

    @Size(max = 50)
    @NotNull
    @Column(name = "phone", nullable = false, length = 50)
    private String phone;

    @Size(max = 50)
    @NotNull
    @Column(name = "address_line1", nullable = false, length = 50)
    private String addressLine1;

    @Size(max = 50)
    @Column(name = "address_line2", length = 50)
    private String addressLine2;

    @Size(max = 50)
    @NotNull
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Size(max = 50)
    @Column(name = "state", length = 50)
    private String state;

    @Size(max = 15)
    @Column(name = "postal_code", length = 15)
    private String postalCode;

    @Size(max = 50)
    @NotNull
    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Column(name = "sales_rep_employee_number")
    private Integer salesRepEmployeeNumber;

    @Column(name = "credit_limit", precision = 10, scale = 2)
    private BigDecimal creditLimit;

}