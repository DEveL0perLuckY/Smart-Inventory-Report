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
public class HealthTest {

    @Id
    private Integer testId;

    @NotNull
    @Size(max = 255)
    private String testName;

    @NotNull
    @Size(max = 50)
    private String testType;

    @DocumentReference(lazy = true, lookup = "{ 'test' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Machine> testMachines;

    @DocumentReference(lazy = true, lookup = "{ 'test' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<HealthParameter> testHealthParameters;

    @DocumentReference(lazy = true)
    private Set<Machine> testMachineMappingMachines;

    @DocumentReference(lazy = true, lookup = "{ 'test' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<TestLabMapping> testTestLabMappings;

    @DocumentReference(
            lazy = true,
            lookup = "{ 'reagentTestMappingHealthTests' : ?#{#self._id} }"
    )
    @ReadOnlyProperty
    private Set<Reagent> reagentTestMappingReagents;

    @DocumentReference(lazy = true, lookup = "{ 'item' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Inventory> itemInventories;

}
