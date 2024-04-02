package com.example.smart_report.rest;

import com.example.smart_report.model.TestLabMappingDTO;
import com.example.smart_report.service.TestLabMappingService;
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
@RequestMapping(value = "/api/testLabMappings", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestLabMappingResource {

    private final TestLabMappingService testLabMappingService;

    public TestLabMappingResource(final TestLabMappingService testLabMappingService) {
        this.testLabMappingService = testLabMappingService;
    }

    @GetMapping
    public ResponseEntity<List<TestLabMappingDTO>> getAllTestLabMappings() {
        return ResponseEntity.ok(testLabMappingService.findAll());
    }

    @GetMapping("/{labId}")
    public ResponseEntity<TestLabMappingDTO> getTestLabMapping(
            @PathVariable(name = "labId") final Integer labId) {
        return ResponseEntity.ok(testLabMappingService.get(labId));
    }

    @PostMapping
    public ResponseEntity<Integer> createTestLabMapping(
            @RequestBody @Valid final TestLabMappingDTO testLabMappingDTO) {
        final Integer createdLabId = testLabMappingService.create(testLabMappingDTO);
        return new ResponseEntity<>(createdLabId, HttpStatus.CREATED);
    }

    @PutMapping("/{labId}")
    public ResponseEntity<Integer> updateTestLabMapping(
            @PathVariable(name = "labId") final Integer labId,
            @RequestBody @Valid final TestLabMappingDTO testLabMappingDTO) {
        testLabMappingService.update(labId, testLabMappingDTO);
        return ResponseEntity.ok(labId);
    }

    @DeleteMapping("/{labId}")
    public ResponseEntity<Void> deleteTestLabMapping(
            @PathVariable(name = "labId") final Integer labId) {
        testLabMappingService.delete(labId);
        return ResponseEntity.noContent().build();
    }

}
