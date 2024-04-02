package com.example.smart_report.repos;

import com.example.smart_report.domain.Machine;
import com.example.smart_report.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@SuppressWarnings("null")

@Component
public class MachineListener extends AbstractMongoEventListener<Machine> {

    private final PrimarySequenceService primarySequenceService;

    public MachineListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Machine> event) {
        if (event.getSource().getMachineId() == null) {
            event.getSource().setMachineId(((int) primarySequenceService.getNextValue()));
        }
    }

}
