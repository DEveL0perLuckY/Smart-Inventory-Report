package com.example.smart_report.service;

import com.example.smart_report.domain.HealthParameter;
import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.model.HealthParameterDTO;
import com.example.smart_report.repos.HealthParameterRepository;
import com.example.smart_report.repos.HealthTestRepository;
import com.example.smart_report.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@SuppressWarnings("null")

@Service
public class HealthParameterService {

    private final HealthParameterRepository healthParameterRepository;
    private final HealthTestRepository healthTestRepository;

    public HealthParameterService(final HealthParameterRepository healthParameterRepository,
            final HealthTestRepository healthTestRepository) {
        this.healthParameterRepository = healthParameterRepository;
        this.healthTestRepository = healthTestRepository;
    }

    public List<HealthParameterDTO> findAll() {
        final List<HealthParameter> healthParameters = healthParameterRepository.findAll(Sort.by("parameterId"));
        return healthParameters.stream()
                .map(healthParameter -> mapToDTO(healthParameter, new HealthParameterDTO()))
                .toList();
    }

    public HealthParameterDTO get(final Integer parameterId) {
        return healthParameterRepository.findById(parameterId)
                .map(healthParameter -> mapToDTO(healthParameter, new HealthParameterDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final HealthParameterDTO healthParameterDTO) {
        final HealthParameter healthParameter = new HealthParameter();
        mapToEntity(healthParameterDTO, healthParameter);
        return healthParameterRepository.save(healthParameter).getParameterId();
    }

    public void update(final Integer parameterId, final HealthParameterDTO healthParameterDTO) {
        final HealthParameter healthParameter = healthParameterRepository.findById(parameterId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(healthParameterDTO, healthParameter);
        healthParameterRepository.save(healthParameter);
    }

    public void delete(final Integer parameterId) {
        healthParameterRepository.deleteById(parameterId);
    }

    private HealthParameterDTO mapToDTO(final HealthParameter healthParameter,
            final HealthParameterDTO healthParameterDTO) {
        healthParameterDTO.setParameterId(healthParameter.getParameterId());
        healthParameterDTO.setParameterName(healthParameter.getParameterName());
        healthParameterDTO.setTest(healthParameter.getTest() == null ? null : healthParameter.getTest().getTestId());
        return healthParameterDTO;
    }

    private HealthParameter mapToEntity(final HealthParameterDTO healthParameterDTO,
            final HealthParameter healthParameter) {
        healthParameter.setParameterName(healthParameterDTO.getParameterName());
        final HealthTest test = healthParameterDTO.getTest() == null ? null
                : healthTestRepository.findById(healthParameterDTO.getTest())
                        .orElseThrow(() -> new NotFoundException("test not found"));
        healthParameter.setTest(test);
        return healthParameter;
    }

}
