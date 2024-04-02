package com.example.smart_report.repos;

import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@SuppressWarnings("null")

@Component
public class HealthTestListener extends AbstractMongoEventListener<HealthTest> {

    private final PrimarySequenceService primarySequenceService;

    public HealthTestListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<HealthTest> event) {
        if (event.getSource().getTestId() == null) {
            event.getSource().setTestId(((int) primarySequenceService.getNextValue()));
        }
    }

}
