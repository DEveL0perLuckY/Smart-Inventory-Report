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
public class Reagent {

    @Id
    private Integer reagentId;

    @NotNull
    @Size(max = 255)
    private String reagentName;

    @NotNull
    @Size(max = 50)
    private String usageType;

    @DocumentReference(lazy = true, lookup = "{ 'reagent' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<ReagentAction> reagentReagentActions;

    @DocumentReference(lazy = true)
    private Set<HealthTest> reagentTestMappingHealthTests;

}
