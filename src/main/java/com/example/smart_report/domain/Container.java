package com.example.smart_report.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Getter
@Setter
public class Container {

    @Id
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
