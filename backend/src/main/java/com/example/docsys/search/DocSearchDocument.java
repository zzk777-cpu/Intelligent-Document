package com.example.docsys.search;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "doc_info")
public class DocSearchDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String content;

    @Field(type = FieldType.Keyword)
    private String courseName;

    @Field(type = FieldType.Keyword)
    private String docType;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String keywords;

    @Field(type = FieldType.Keyword)
    private String category;
}
