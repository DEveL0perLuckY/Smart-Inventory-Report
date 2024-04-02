package com.example.smart_report.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document
@Getter
@Setter
public class Machine {

    @Id
    private Integer machineId;

    @NotNull
    @Size(max = 255)
    private String machineName;

    @DocumentReference(lazy = true)
    private HealthTest test;

    @DocumentReference(
            lazy = true,
            lookup = "{ 'testMachineMappingMachines' : ?#{#self._id} }"
    )
    @ReadOnlyProperty
    private Set<HealthTest> testMachineMappingHealthTests;

}
