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
public class ReagentAction {

    @Id
    private Integer actionId;

    @NotNull
    @Size(max = 50)
    private String actionName;

    @DocumentReference(lazy = true)
    private Reagent reagent;

}
