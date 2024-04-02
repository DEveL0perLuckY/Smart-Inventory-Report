package com.example.smart_report.repos;

import com.example.smart_report.domain.Inventory;
import com.example.smart_report.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@SuppressWarnings("null")

@Component
public class InventoryListener extends AbstractMongoEventListener<Inventory> {

    private final PrimarySequenceService primarySequenceService;

    public InventoryListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Inventory> event) {
        if (event.getSource().getInventoryId() == null) {
            event.getSource().setInventoryId(((int) primarySequenceService.getNextValue()));
        }
    }

}
