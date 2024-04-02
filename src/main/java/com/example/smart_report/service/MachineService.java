package com.example.smart_report.service;

import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.domain.Machine;
import com.example.smart_report.model.MachineDTO;
import com.example.smart_report.repos.HealthTestRepository;
import com.example.smart_report.repos.MachineRepository;
import com.example.smart_report.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@SuppressWarnings("null")

@Service
public class MachineService {

    private final MachineRepository machineRepository;
    private final HealthTestRepository healthTestRepository;

    public MachineService(final MachineRepository machineRepository,
            final HealthTestRepository healthTestRepository) {
        this.machineRepository = machineRepository;
        this.healthTestRepository = healthTestRepository;
    }

    public List<MachineDTO> findAll() {
        final List<Machine> machines = machineRepository.findAll(Sort.by("machineId"));
        return machines.stream()
                .map(machine -> mapToDTO(machine, new MachineDTO()))
                .toList();
    }

    public MachineDTO get(final Integer machineId) {
        return machineRepository.findById(machineId)
                .map(machine -> mapToDTO(machine, new MachineDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final MachineDTO machineDTO) {
        final Machine machine = new Machine();
        mapToEntity(machineDTO, machine);
        return machineRepository.save(machine).getMachineId();
    }

    public void update(final Integer machineId, final MachineDTO machineDTO) {
        final Machine machine = machineRepository.findById(machineId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(machineDTO, machine);
        machineRepository.save(machine);
    }

    public void delete(final Integer machineId) {
        final Machine machine = machineRepository.findById(machineId)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        healthTestRepository.findAllByTestMachineMappingMachines(machine)
                .forEach(healthTest -> healthTest.getTestMachineMappingMachines().remove(machine));
        machineRepository.delete(machine);
    }

    private MachineDTO mapToDTO(final Machine machine, final MachineDTO machineDTO) {
        machineDTO.setMachineId(machine.getMachineId());
        machineDTO.setMachineName(machine.getMachineName());
        machineDTO.setTest(machine.getTest() == null ? null : machine.getTest().getTestId());
        return machineDTO;
    }

    private Machine mapToEntity(final MachineDTO machineDTO, final Machine machine) {
        machine.setMachineName(machineDTO.getMachineName());
        final HealthTest test = machineDTO.getTest() == null ? null
                : healthTestRepository.findById(machineDTO.getTest())
                        .orElseThrow(() -> new NotFoundException("test not found"));
        machine.setTest(test);
        return machine;
    }

}
