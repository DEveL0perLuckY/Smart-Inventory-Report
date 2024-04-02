package com.example.smart_report.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HealthTestDTO {

    private Integer testId;

    @NotNull
    @Size(max = 255)
    private String testName;

    @NotNull
    @Size(max = 50)
    private String testType;

    private List<Integer> testMachineMappingMachines;

}
