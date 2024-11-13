package org.hulei.entity.jpa.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Accessors(chain = true)
@Getter
@Setter
@Entity
@Table(name = "offices", schema = "test")
public class Office {

    @Id
    @Size(max = 10)
    @Column(name = "office_code", nullable = false, length = 10)
    private String officeCode;

    @Size(max = 50)
    @NotNull
    @Column(name = "city", nullable = false, length = 50)
    private String city;

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
    @Column(name = "state", length = 50)
    private String state;

    @Size(max = 50)
    @NotNull
    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Size(max = 15)
    @NotNull
    @Column(name = "postal_code", nullable = false, length = 15)
    private String postalCode;

    @Size(max = 10)
    @NotNull
    @Column(name = "territory", nullable = false, length = 10)
    private String territory;

}