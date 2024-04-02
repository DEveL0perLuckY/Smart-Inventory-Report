package com.example.smart_report.rest;

import com.example.smart_report.model.ContainerDTO;
import com.example.smart_report.service.ContainerService;
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
@RequestMapping(value = "/api/containers", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContainerResource {

    private final ContainerService containerService;

    public ContainerResource(final ContainerService containerService) {
        this.containerService = containerService;
    }

    @GetMapping
    public ResponseEntity<List<ContainerDTO>> getAllContainers() {
        return ResponseEntity.ok(containerService.findAll());
    }

    @GetMapping("/{containerId}")
    public ResponseEntity<ContainerDTO> getContainer(
            @PathVariable(name = "containerId") final Integer containerId) {
        return ResponseEntity.ok(containerService.get(containerId));
    }

    @PostMapping
    public ResponseEntity<Integer> createContainer(
            @RequestBody @Valid final ContainerDTO containerDTO) {
        final Integer createdContainerId = containerService.create(containerDTO);
        return new ResponseEntity<>(createdContainerId, HttpStatus.CREATED);
    }

    @PutMapping("/{containerId}")
    public ResponseEntity<Integer> updateContainer(
            @PathVariable(name = "containerId") final Integer containerId,
            @RequestBody @Valid final ContainerDTO containerDTO) {
        containerService.update(containerId, containerDTO);
        return ResponseEntity.ok(containerId);
    }

    @DeleteMapping("/{containerId}")
    public ResponseEntity<Void> deleteContainer(
            @PathVariable(name = "containerId") final Integer containerId) {
        containerService.delete(containerId);
        return ResponseEntity.noContent().build();
    }

}
