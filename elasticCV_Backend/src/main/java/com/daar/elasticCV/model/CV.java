package com.daar.elasticCV.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;
import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "cvs")
public class CV {
    @Id
    private String id;

    @Field(type = Text, name = "title")
    private String title;

    @Field(type = Keyword, name = "skill")
    private String[] skill;
}