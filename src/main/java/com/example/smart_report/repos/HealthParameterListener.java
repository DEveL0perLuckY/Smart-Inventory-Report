package com.example.smart_report.repos;

import com.example.smart_report.domain.HealthParameter;
import com.example.smart_report.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@SuppressWarnings("null")

@Component
public class HealthParameterListener extends AbstractMongoEventListener<HealthParameter> {

    private final PrimarySequenceService primarySequenceService;

    public HealthParameterListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<HealthParameter> event) {
        if (event.getSource().getParameterId() == null) {
            event.getSource().setParameterId(((int) primarySequenceService.getNextValue()));
        }
    }

}
