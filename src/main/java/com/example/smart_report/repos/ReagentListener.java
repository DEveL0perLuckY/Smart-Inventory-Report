package com.example.smart_report.repos;

import com.example.smart_report.domain.Reagent;
import com.example.smart_report.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@SuppressWarnings("null")
@Component
public class ReagentListener extends AbstractMongoEventListener<Reagent> {

    private final PrimarySequenceService primarySequenceService;

    public ReagentListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Reagent> event) {
        if (event.getSource().getReagentId() == null) {
            event.getSource().setReagentId(((int) primarySequenceService.getNextValue()));
        }
    }

}
