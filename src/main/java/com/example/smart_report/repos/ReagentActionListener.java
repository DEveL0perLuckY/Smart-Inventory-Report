package com.example.smart_report.repos;

import com.example.smart_report.domain.ReagentAction;
import com.example.smart_report.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@SuppressWarnings("null")

@Component
public class ReagentActionListener extends AbstractMongoEventListener<ReagentAction> {

    private final PrimarySequenceService primarySequenceService;

    public ReagentActionListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<ReagentAction> event) {
        if (event.getSource().getActionId() == null) {
            event.getSource().setActionId(((int) primarySequenceService.getNextValue()));
        }
    }

}
