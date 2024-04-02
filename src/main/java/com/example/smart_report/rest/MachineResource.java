package com.example.smart_report.rest;

import com.example.smart_report.model.MachineDTO;
import com.example.smart_report.service.MachineService;
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
@RequestMapping(value = "/api/machines", produces = MediaType.APPLICATION_JSON_VALUE)
public class MachineResource {

    private final MachineService machineService;

    public MachineResource(final MachineService machineService) {
        this.machineService = machineService;
    }

    @GetMapping
    public ResponseEntity<List<MachineDTO>> getAllMachines() {
        return ResponseEntity.ok(machineService.findAll());
    }

    @GetMapping("/{machineId}")
    public ResponseEntity<MachineDTO> getMachine(
            @PathVariable(name = "machineId") final Integer machineId) {
        return ResponseEntity.ok(machineService.get(machineId));
    }

    @PostMapping
    public ResponseEntity<Integer> createMachine(@RequestBody @Valid final MachineDTO machineDTO) {
        final Integer createdMachineId = machineService.create(machineDTO);
        return new ResponseEntity<>(createdMachineId, HttpStatus.CREATED);
    }

    @PutMapping("/{machineId}")
    public ResponseEntity<Integer> updateMachine(
            @PathVariable(name = "machineId") final Integer machineId,
            @RequestBody @Valid final MachineDTO machineDTO) {
        machineService.update(machineId, machineDTO);
        return ResponseEntity.ok(machineId);
    }

    @DeleteMapping("/{machineId}")
    public ResponseEntity<Void> deleteMachine(
            @PathVariable(name = "machineId") final Integer machineId) {
        machineService.delete(machineId);
        return ResponseEntity.noContent().build();
    }

}
