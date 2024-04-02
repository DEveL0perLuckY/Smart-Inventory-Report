package com.example.smart_report.repos;

import com.example.smart_report.domain.LabFault;
import com.example.smart_report.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@SuppressWarnings("null")

@Component
public class LabFaultListener extends AbstractMongoEventListener<LabFault> {

    private final PrimarySequenceService primarySequenceService;

    public LabFaultListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<LabFault> event) {
        if (event.getSource().getFaultId() == null) {
            event.getSource().setFaultId(((int) primarySequenceService.getNextValue()));
        }
    }

}
