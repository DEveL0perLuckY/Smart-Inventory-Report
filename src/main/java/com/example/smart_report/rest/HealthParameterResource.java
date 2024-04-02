package com.example.smart_report.rest;

import com.example.smart_report.model.HealthParameterDTO;
import com.example.smart_report.service.HealthParameterService;
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
@RequestMapping(value = "/api/healthParameters", produces = MediaType.APPLICATION_JSON_VALUE)
public class HealthParameterResource {

    private final HealthParameterService healthParameterService;

    public HealthParameterResource(final HealthParameterService healthParameterService) {
        this.healthParameterService = healthParameterService;
    }

    @GetMapping
    public ResponseEntity<List<HealthParameterDTO>> getAllHealthParameters() {
        return ResponseEntity.ok(healthParameterService.findAll());
    }

    @GetMapping("/{parameterId}")
    public ResponseEntity<HealthParameterDTO> getHealthParameter(
            @PathVariable(name = "parameterId") final Integer parameterId) {
        return ResponseEntity.ok(healthParameterService.get(parameterId));
    }

    @PostMapping
    public ResponseEntity<Integer> createHealthParameter(
            @RequestBody @Valid final HealthParameterDTO healthParameterDTO) {
        final Integer createdParameterId = healthParameterService.create(healthParameterDTO);
        return new ResponseEntity<>(createdParameterId, HttpStatus.CREATED);
    }

    @PutMapping("/{parameterId}")
    public ResponseEntity<Integer> updateHealthParameter(
            @PathVariable(name = "parameterId") final Integer parameterId,
            @RequestBody @Valid final HealthParameterDTO healthParameterDTO) {
        healthParameterService.update(parameterId, healthParameterDTO);
        return ResponseEntity.ok(parameterId);
    }

    @DeleteMapping("/{parameterId}")
    public ResponseEntity<Void> deleteHealthParameter(
            @PathVariable(name = "parameterId") final Integer parameterId) {
        healthParameterService.delete(parameterId);
        return ResponseEntity.noContent().build();
    }

}
