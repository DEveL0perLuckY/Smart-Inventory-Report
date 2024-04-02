package com.example.smart_report.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document
@Getter
@Setter
public class Inventory {

    @Id
    private Integer inventoryId;

    private Integer quantity;

    private LocalDateTime lastUpdated;

    @DocumentReference(lazy = true)
    private HealthTest item;

}
