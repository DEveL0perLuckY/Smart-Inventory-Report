package com.example.smart_report.rest;

import com.example.smart_report.model.HealthTestDTO;
import com.example.smart_report.service.HealthTestService;
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
@RequestMapping(value = "/api/healthTests", produces = MediaType.APPLICATION_JSON_VALUE)
public class HealthTestResource {

    private final HealthTestService healthTestService;

    public HealthTestResource(final HealthTestService healthTestService) {
        this.healthTestService = healthTestService;
    }

    @GetMapping
    public ResponseEntity<List<HealthTestDTO>> getAllHealthTests() {
        return ResponseEntity.ok(healthTestService.findAll());
    }

    @GetMapping("/{testId}")
    public ResponseEntity<HealthTestDTO> getHealthTest(
            @PathVariable(name = "testId") final Integer testId) {
        return ResponseEntity.ok(healthTestService.get(testId));
    }

    @PostMapping
    public ResponseEntity<Integer> createHealthTest(
            @RequestBody @Valid final HealthTestDTO healthTestDTO) {
        final Integer createdTestId = healthTestService.create(healthTestDTO);
        return new ResponseEntity<>(createdTestId, HttpStatus.CREATED);
    }

    @PutMapping("/{testId}")
    public ResponseEntity<Integer> updateHealthTest(
            @PathVariable(name = "testId") final Integer testId,
            @RequestBody @Valid final HealthTestDTO healthTestDTO) {
        healthTestService.update(testId, healthTestDTO);
        return ResponseEntity.ok(testId);
    }

    @DeleteMapping("/{testId}")
    public ResponseEntity<Void> deleteHealthTest(
            @PathVariable(name = "testId") final Integer testId) {
        final ReferencedWarning referencedWarning = healthTestService.getReferencedWarning(testId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        healthTestService.delete(testId);
        return ResponseEntity.noContent().build();
    }

}
