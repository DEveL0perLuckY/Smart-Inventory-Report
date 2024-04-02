package com.example.smart_report.rest;

import com.example.smart_report.model.ReagentDTO;
import com.example.smart_report.service.ReagentService;
import com.example.smart_report.util.ReferencedException;
import com.example.smart_report.util.ReferencedWarning;
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
@RequestMapping(value = "/api/reagents", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReagentResource {

    private final ReagentService reagentService;

    public ReagentResource(final ReagentService reagentService) {
        this.reagentService = reagentService;
    }

    @GetMapping
    public ResponseEntity<List<ReagentDTO>> getAllReagents() {
        return ResponseEntity.ok(reagentService.findAll());
    }

    @GetMapping("/{reagentId}")
    public ResponseEntity<ReagentDTO> getReagent(
            @PathVariable(name = "reagentId") final Integer reagentId) {
        return ResponseEntity.ok(reagentService.get(reagentId));
    }

    @PostMapping
    public ResponseEntity<Integer> createReagent(@RequestBody @Valid final ReagentDTO reagentDTO) {
        final Integer createdReagentId = reagentService.create(reagentDTO);
        return new ResponseEntity<>(createdReagentId, HttpStatus.CREATED);
    }

    @PutMapping("/{reagentId}")
    public ResponseEntity<Integer> updateReagent(
            @PathVariable(name = "reagentId") final Integer reagentId,
            @RequestBody @Valid final ReagentDTO reagentDTO) {
        reagentService.update(reagentId, reagentDTO);
        return ResponseEntity.ok(reagentId);
    }

    @DeleteMapping("/{reagentId}")
    public ResponseEntity<Void> deleteReagent(
            @PathVariable(name = "reagentId") final Integer reagentId) {
        final ReferencedWarning referencedWarning = reagentService.getReferencedWarning(reagentId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        reagentService.delete(reagentId);
        return ResponseEntity.noContent().build();
    }

}
