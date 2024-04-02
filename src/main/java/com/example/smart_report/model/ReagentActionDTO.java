package com.example.smart_report.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReagentActionDTO {

    private Integer actionId;

    @NotNull
    @Size(max = 50)
    private String actionName;

    private Integer reagent;

}
