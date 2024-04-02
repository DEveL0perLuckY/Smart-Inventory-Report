package com.example.smart_report.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReagentDTO {

    private Integer reagentId;

    @NotNull
    @Size(max = 255)
    private String reagentName;

    @NotNull
    @Size(max = 50)
    private String usageType;

    private List<Integer> reagentTestMappingHealthTests;

}
