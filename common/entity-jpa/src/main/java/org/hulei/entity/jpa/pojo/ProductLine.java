package org.hulei.entity.jpa.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Accessors(chain = true)
@Data
@Entity
@Table(name = "test_product_lines", schema = "test")
public class ProductLine {

    @Id
    @Size(max = 50)
    @Column(name = "product_line", nullable = false, length = 50)
    private String productLineName;

    @Size(max = 4000)
    @Column(name = "text_description", length = 4000)
    private String textDescription;

    @Lob
    @Column(name = "html_description")
    private String htmlDescription;

    @Column(name = "image")
    private byte[] image;

}