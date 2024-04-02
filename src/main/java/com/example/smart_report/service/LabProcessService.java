package com.example.smart_report.service;

import com.example.smart_report.domain.LabProcess;
import com.example.smart_report.model.LabProcessDTO;
import com.example.smart_report.repos.LabProcessRepository;
import com.example.smart_report.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@SuppressWarnings("null")

@Service
public class LabProcessService {

    private final LabProcessRepository labProcessRepository;

    public LabProcessService(final LabProcessRepository labProcessRepository) {
        this.labProcessRepository = labProcessRepository;
    }

    public List<LabProcessDTO> findAll() {
        final List<LabProcess> labProcesses = labProcessRepository.findAll(Sort.by("processId"));
        return labProcesses.stream()
                .map(labProcess -> mapToDTO(labProcess, new LabProcessDTO()))
                .toList();
    }

    public LabProcessDTO get(final Integer processId) {
        return labProcessRepository.findById(processId)
                .map(labProcess -> mapToDTO(labProcess, new LabProcessDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final LabProcessDTO labProcessDTO) {
        final LabProcess labProcess = new LabProcess();
        mapToEntity(labProcessDTO, labProcess);
        return labProcessRepository.save(labProcess).getProcessId();
    }

    public void update(final Integer processId, final LabProcessDTO labProcessDTO) {
        final LabProcess labProcess = labProcessRepository.findById(processId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(labProcessDTO, labProcess);
        labProcessRepository.save(labProcess);
    }

    public void delete(final Integer processId) {
        labProcessRepository.deleteById(processId);
    }

    private LabProcessDTO mapToDTO(final LabProcess labProcess, final LabProcessDTO labProcessDTO) {
        labProcessDTO.setProcessId(labProcess.getProcessId());
        labProcessDTO.setProcessName(labProcess.getProcessName());
        return labProcessDTO;
    }

    private LabProcess mapToEntity(final LabProcessDTO labProcessDTO, final LabProcess labProcess) {
        labProcess.setProcessName(labProcessDTO.getProcessName());
        return labProcess;
    }

}
