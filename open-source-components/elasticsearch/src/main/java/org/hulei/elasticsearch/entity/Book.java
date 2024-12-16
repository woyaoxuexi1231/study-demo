package org.hulei.elasticsearch.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;


@Accessors(chain = true)
@Data
@Document(indexName = "books")
public class Book {
    @Id
    @Field(value = "id")
    private Long id;
    @Field(value = "title")
    private String title;
    @Field(value = "author")
    private String author;
}
