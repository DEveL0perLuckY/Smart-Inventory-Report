package com.example.smart_report.repos;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.example.smart_report.domain.Role;
import com.example.smart_report.service.PrimarySequenceService;

@SuppressWarnings("null")

@Component
public class RoleListener extends AbstractMongoEventListener<Role> {

    private final PrimarySequenceService primarySequenceService;

    public RoleListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Role> event) {
        if (event.getSource().getRoleId() == null) {
            event.getSource().setRoleId((int) primarySequenceService.getNextValue());
        }
    }

}
