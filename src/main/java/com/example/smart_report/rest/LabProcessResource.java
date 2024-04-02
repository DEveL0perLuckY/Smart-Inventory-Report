package com.example.smart_report.rest;

import com.example.smart_report.model.LabProcessDTO;
import com.example.smart_report.service.LabProcessService;
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
@RequestMapping(value = "/api/labProcesses", produces = MediaType.APPLICATION_JSON_VALUE)
public class LabProcessResource {

    private final LabProcessService labProcessService;

    public LabProcessResource(final LabProcessService labProcessService) {
        this.labProcessService = labProcessService;
    }

    @GetMapping
    public ResponseEntity<List<LabProcessDTO>> getAllLabProcesses() {
        return ResponseEntity.ok(labProcessService.findAll());
    }

    @GetMapping("/{processId}")
    public ResponseEntity<LabProcessDTO> getLabProcess(
            @PathVariable(name = "processId") final Integer processId) {
        return ResponseEntity.ok(labProcessService.get(processId));
    }

    @PostMapping
    public ResponseEntity<Integer> createLabProcess(
            @RequestBody @Valid final LabProcessDTO labProcessDTO) {
        final Integer createdProcessId = labProcessService.create(labProcessDTO);
        return new ResponseEntity<>(createdProcessId, HttpStatus.CREATED);
    }

    @PutMapping("/{processId}")
    public ResponseEntity<Integer> updateLabProcess(
            @PathVariable(name = "processId") final Integer processId,
            @RequestBody @Valid final LabProcessDTO labProcessDTO) {
        labProcessService.update(processId, labProcessDTO);
        return ResponseEntity.ok(processId);
    }

    @DeleteMapping("/{processId}")
    public ResponseEntity<Void> deleteLabProcess(
            @PathVariable(name = "processId") final Integer processId) {
        labProcessService.delete(processId);
        return ResponseEntity.noContent().build();
    }

}
