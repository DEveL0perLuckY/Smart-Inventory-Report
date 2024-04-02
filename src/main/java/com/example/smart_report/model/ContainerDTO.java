package com.example.smart_report.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ContainerDTO {

    private Integer containerId;

    @NotNull
    @Size(max = 255)
    private String containerName;

    @Size(max = 255)
    private String chemicals;

    @NotNull
    @Size(max = 50)
    private String transportMethod;

}
