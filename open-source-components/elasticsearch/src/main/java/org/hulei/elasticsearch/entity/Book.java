package org.hulei.elasticsearch.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;


@Data
@Document(indexName = "books")
public class Book {
    @Field(value = "id")
    private String id;
    @Field(value = "title")
    private String title;
    @Field(value = "author")
    private String author;
}
