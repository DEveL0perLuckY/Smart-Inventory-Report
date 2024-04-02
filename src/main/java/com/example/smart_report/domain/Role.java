package com.example.smart_report.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
@Setter
@Getter
public class Role {

    @Id
    private Integer roleId;

    @NotNull
    @Size(max = 80)
    private String name;

    @DocumentReference(lazy = true, lookup = "{ 'roleId' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<User> userId;

}