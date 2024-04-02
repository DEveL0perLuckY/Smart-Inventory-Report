package com.example.smart_report.rest;

import com.example.smart_report.model.ReagentActionDTO;
import com.example.smart_report.service.ReagentActionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/reagentActions", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReagentActionResource {

    private final ReagentActionService reagentActionService;

    public ReagentActionResource(final ReagentActionService reagentActionService) {
        this.reagentActionService = reagentActionService;
    }

    @GetMapping
    public ResponseEntity<List<ReagentActionDTO>> getAllReagentActions() {
        return ResponseEntity.ok(reagentActionService.findAll());
    }

    @GetMapping("/{actionId}")
    public ResponseEntity<ReagentActionDTO> getReagentAction(
            @PathVariable(name = "actionId") final Integer actionId) {
        return ResponseEntity.ok(reagentActionService.get(actionId));
    }

    @PostMapping
    public ResponseEntity<Integer> createReagentAction(
            @RequestBody @Valid final ReagentActionDTO reagentActionDTO) {
        final Integer createdActionId = reagentActionService.create(reagentActionDTO);
        return new ResponseEntity<>(createdActionId, HttpStatus.CREATED);
    }

    @PutMapping("/{actionId}")
    public ResponseEntity<Integer> updateReagentAction(
            @PathVariable(name = "actionId") final Integer actionId,
            @RequestBody @Valid final ReagentActionDTO reagentActionDTO) {
        reagentActionService.update(actionId, reagentActionDTO);
        return ResponseEntity.ok(actionId);
    }

    @DeleteMapping("/{actionId}")
    public ResponseEntity<Void> deleteReagentAction(
            @PathVariable(name = "actionId") final Integer actionId) {
        reagentActionService.delete(actionId);
        return ResponseEntity.noContent().build();
    }

}
