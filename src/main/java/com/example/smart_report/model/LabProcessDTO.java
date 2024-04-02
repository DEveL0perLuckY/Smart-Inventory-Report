package com.example.smart_report.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LabProcessDTO {

    private Integer processId;

    @NotNull
    @Size(max = 255)
    private String processName;

}
