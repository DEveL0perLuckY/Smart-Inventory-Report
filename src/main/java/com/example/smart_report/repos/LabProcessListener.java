package com.example.smart_report.repos;

import com.example.smart_report.domain.LabProcess;
import com.example.smart_report.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@SuppressWarnings("null")

@Component
public class LabProcessListener extends AbstractMongoEventListener<LabProcess> {

    private final PrimarySequenceService primarySequenceService;

    public LabProcessListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<LabProcess> event) {
        if (event.getSource().getProcessId() == null) {
            event.getSource().setProcessId(((int) primarySequenceService.getNextValue()));
        }
    }

}
