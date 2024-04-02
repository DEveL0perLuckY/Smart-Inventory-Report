package com.example.smart_report.repos;

import com.example.smart_report.domain.Container;
import com.example.smart_report.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@SuppressWarnings("null")

@Component
public class ContainerListener extends AbstractMongoEventListener<Container> {

    private final PrimarySequenceService primarySequenceService;

    public ContainerListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Container> event) {
        if (event.getSource().getContainerId() == null) {
            event.getSource().setContainerId(((int) primarySequenceService.getNextValue()));
        }
    }

}
