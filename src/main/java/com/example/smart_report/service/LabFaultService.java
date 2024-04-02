package com.example.smart_report.service;

import com.example.smart_report.domain.LabFault;
import com.example.smart_report.model.LabFaultDTO;
import com.example.smart_report.repos.LabFaultRepository;
import com.example.smart_report.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@SuppressWarnings("null")

@Service
public class LabFaultService {

    private final LabFaultRepository labFaultRepository;

    public LabFaultService(final LabFaultRepository labFaultRepository) {
        this.labFaultRepository = labFaultRepository;
    }

    public List<LabFaultDTO> findAll() {
        final List<LabFault> labFaults = labFaultRepository.findAll(Sort.by("faultId"));
        return labFaults.stream()
                .map(labFault -> mapToDTO(labFault, new LabFaultDTO()))
                .toList();
    }

    public LabFaultDTO get(final Integer faultId) {
        return labFaultRepository.findById(faultId)
                .map(labFault -> mapToDTO(labFault, new LabFaultDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final LabFaultDTO labFaultDTO) {
        final LabFault labFault = new LabFault();
        mapToEntity(labFaultDTO, labFault);
        return labFaultRepository.save(labFault).getFaultId();
    }

    public void update(final Integer faultId, final LabFaultDTO labFaultDTO) {
        final LabFault labFault = labFaultRepository.findById(faultId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(labFaultDTO, labFault);
        labFaultRepository.save(labFault);
    }

    public void delete(final Integer faultId) {
        labFaultRepository.deleteById(faultId);
    }

    private LabFaultDTO mapToDTO(final LabFault labFault, final LabFaultDTO labFaultDTO) {
        labFaultDTO.setFaultId(labFault.getFaultId());
        labFaultDTO.setFaultName(labFault.getFaultName());
        return labFaultDTO;
    }

    private LabFault mapToEntity(final LabFaultDTO labFaultDTO, final LabFault labFault) {
        labFault.setFaultName(labFaultDTO.getFaultName());
        return labFault;
    }

}
