package com.example.smart_report.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document
@Getter
@Setter
public class TestLabMapping {

    @Id
    private Integer labId;

    @DocumentReference(lazy = true)
    private HealthTest test;

}
