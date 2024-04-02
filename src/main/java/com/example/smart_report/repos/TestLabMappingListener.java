package com.example.smart_report.repos;

import com.example.smart_report.domain.TestLabMapping;
import com.example.smart_report.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@SuppressWarnings("null")
@Component
public class TestLabMappingListener extends AbstractMongoEventListener<TestLabMapping> {

    private final PrimarySequenceService primarySequenceService;

    public TestLabMappingListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<TestLabMapping> event) {
        if (event.getSource().getLabId() == null) {
            event.getSource().setLabId(((int) primarySequenceService.getNextValue()));
        }
    }

}
