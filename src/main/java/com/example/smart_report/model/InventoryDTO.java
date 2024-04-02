package com.example.smart_report.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InventoryDTO {

    private Integer inventoryId;
    private Integer quantity;
    private LocalDateTime lastUpdated;
    private Integer item;

}
