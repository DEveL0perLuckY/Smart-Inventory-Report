package com.example.smart_report.service;

import com.example.smart_report.domain.Reagent;
import com.example.smart_report.domain.ReagentAction;
import com.example.smart_report.model.ReagentActionDTO;
import com.example.smart_report.repos.ReagentActionRepository;
import com.example.smart_report.repos.ReagentRepository;
import com.example.smart_report.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@SuppressWarnings("null")
@Service
public class ReagentActionService {

    private final ReagentActionRepository reagentActionRepository;
    private final ReagentRepository reagentRepository;

    public ReagentActionService(final ReagentActionRepository reagentActionRepository,
            final ReagentRepository reagentRepository) {
        this.reagentActionRepository = reagentActionRepository;
        this.reagentRepository = reagentRepository;
    }

    public List<ReagentActionDTO> findAll() {
        final List<ReagentAction> reagentActions = reagentActionRepository.findAll(Sort.by("actionId"));
        return reagentActions.stream()
                .map(reagentAction -> mapToDTO(reagentAction, new ReagentActionDTO()))
                .toList();
    }

    public ReagentActionDTO get(final Integer actionId) {
        return reagentActionRepository.findById(actionId)
                .map(reagentAction -> mapToDTO(reagentAction, new ReagentActionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ReagentActionDTO reagentActionDTO) {
        final ReagentAction reagentAction = new ReagentAction();
        mapToEntity(reagentActionDTO, reagentAction);
        return reagentActionRepository.save(reagentAction).getActionId();
    }

    public void update(final Integer actionId, final ReagentActionDTO reagentActionDTO) {
        final ReagentAction reagentAction = reagentActionRepository.findById(actionId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reagentActionDTO, reagentAction);
        reagentActionRepository.save(reagentAction);
    }

    public void delete(final Integer actionId) {
        reagentActionRepository.deleteById(actionId);
    }

    private ReagentActionDTO mapToDTO(final ReagentAction reagentAction,
            final ReagentActionDTO reagentActionDTO) {
        reagentActionDTO.setActionId(reagentAction.getActionId());
        reagentActionDTO.setActionName(reagentAction.getActionName());
        reagentActionDTO
                .setReagent(reagentAction.getReagent() == null ? null : reagentAction.getReagent().getReagentId());
        return reagentActionDTO;
    }

    private ReagentAction mapToEntity(final ReagentActionDTO reagentActionDTO,
            final ReagentAction reagentAction) {
        reagentAction.setActionName(reagentActionDTO.getActionName());
        final Reagent reagent = reagentActionDTO.getReagent() == null ? null
                : reagentRepository.findById(reagentActionDTO.getReagent())
                        .orElseThrow(() -> new NotFoundException("reagent not found"));
        reagentAction.setReagent(reagent);
        return reagentAction;
    }

}
