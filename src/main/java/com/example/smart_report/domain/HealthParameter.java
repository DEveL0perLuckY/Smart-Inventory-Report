package com.example.smart_report.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document
@Getter
@Setter
public class HealthParameter {

    @Id
    private Integer parameterId;

    @NotNull
    @Size(max = 255)
    private String parameterName;

    @DocumentReference(lazy = true)
    private HealthTest test;

}
