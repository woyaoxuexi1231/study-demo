package org.hulei.entity.jpa.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Entity
@Table(name = "product_lines", schema = "test")
public class ProductLine {

    @Id
    @Size(max = 50)
    @Column(name = "product_line", nullable = false, length = 50)
    private String productLine;

    @Size(max = 4000)
    @Column(name = "text_description", length = 4000)
    private String textDescription;

    @Lob
    @Column(name = "html_description")
    private String htmlDescription;

    @Column(name = "image")
    private byte[] image;

}