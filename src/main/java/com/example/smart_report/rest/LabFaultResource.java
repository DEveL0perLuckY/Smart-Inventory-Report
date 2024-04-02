package com.example.smart_report.rest;

import com.example.smart_report.model.LabFaultDTO;
import com.example.smart_report.service.LabFaultService;
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
@RequestMapping(value = "/api/labFaults", produces = MediaType.APPLICATION_JSON_VALUE)
public class LabFaultResource {

    private final LabFaultService labFaultService;

    public LabFaultResource(final LabFaultService labFaultService) {
        this.labFaultService = labFaultService;
    }

    @GetMapping
    public ResponseEntity<List<LabFaultDTO>> getAllLabFaults() {
        return ResponseEntity.ok(labFaultService.findAll());
    }

    @GetMapping("/{faultId}")
    public ResponseEntity<LabFaultDTO> getLabFault(
            @PathVariable(name = "faultId") final Integer faultId) {
        return ResponseEntity.ok(labFaultService.get(faultId));
    }

    @PostMapping
    public ResponseEntity<Integer> createLabFault(
            @RequestBody @Valid final LabFaultDTO labFaultDTO) {
        final Integer createdFaultId = labFaultService.create(labFaultDTO);
        return new ResponseEntity<>(createdFaultId, HttpStatus.CREATED);
    }

    @PutMapping("/{faultId}")
    public ResponseEntity<Integer> updateLabFault(
            @PathVariable(name = "faultId") final Integer faultId,
            @RequestBody @Valid final LabFaultDTO labFaultDTO) {
        labFaultService.update(faultId, labFaultDTO);
        return ResponseEntity.ok(faultId);
    }

    @DeleteMapping("/{faultId}")
    public ResponseEntity<Void> deleteLabFault(
            @PathVariable(name = "faultId") final Integer faultId) {
        labFaultService.delete(faultId);
        return ResponseEntity.noContent().build();
    }

}
