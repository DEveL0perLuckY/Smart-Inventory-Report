package com.example.smart_report.service;

import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.domain.Reagent;
import com.example.smart_report.domain.ReagentAction;
import com.example.smart_report.model.ReagentDTO;
import com.example.smart_report.repos.HealthTestRepository;
import com.example.smart_report.repos.ReagentActionRepository;
import com.example.smart_report.repos.ReagentRepository;
import com.example.smart_report.util.NotFoundException;
import com.example.smart_report.util.ReferencedWarning;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@SuppressWarnings("null")
@Service
public class ReagentService {

    private final ReagentRepository reagentRepository;
    private final HealthTestRepository healthTestRepository;
    private final ReagentActionRepository reagentActionRepository;

    public ReagentService(final ReagentRepository reagentRepository,
            final HealthTestRepository healthTestRepository,
            final ReagentActionRepository reagentActionRepository) {
        this.reagentRepository = reagentRepository;
        this.healthTestRepository = healthTestRepository;
        this.reagentActionRepository = reagentActionRepository;
    }

    public List<ReagentDTO> findAll() {
        final List<Reagent> reagents = reagentRepository.findAll(Sort.by("reagentId"));
        return reagents.stream()
                .map(reagent -> mapToDTO(reagent, new ReagentDTO()))
                .toList();
    }

    public ReagentDTO get(final Integer reagentId) {
        return reagentRepository.findById(reagentId)
                .map(reagent -> mapToDTO(reagent, new ReagentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ReagentDTO reagentDTO) {
        final Reagent reagent = new Reagent();
        mapToEntity(reagentDTO, reagent);
        return reagentRepository.save(reagent).getReagentId();
    }

    public void update(final Integer reagentId, final ReagentDTO reagentDTO) {
        final Reagent reagent = reagentRepository.findById(reagentId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reagentDTO, reagent);
        reagentRepository.save(reagent);
    }

    public void delete(final Integer reagentId) {
        reagentRepository.deleteById(reagentId);
    }

    private ReagentDTO mapToDTO(final Reagent reagent, final ReagentDTO reagentDTO) {
        reagentDTO.setReagentId(reagent.getReagentId());
        reagentDTO.setReagentName(reagent.getReagentName());
        reagentDTO.setUsageType(reagent.getUsageType());
        reagentDTO.setReagentTestMappingHealthTests(reagent.getReagentTestMappingHealthTests().stream()
                .map(healthTest -> healthTest.getTestId())
                .toList());
        return reagentDTO;
    }

    private Reagent mapToEntity(final ReagentDTO reagentDTO, final Reagent reagent) {
        reagent.setReagentName(reagentDTO.getReagentName());
        reagent.setUsageType(reagentDTO.getUsageType());
        final List<HealthTest> reagentTestMappingHealthTests = iterableToList(healthTestRepository.findAllById(
                reagentDTO.getReagentTestMappingHealthTests() == null ? Collections.emptyList()
                        : reagentDTO.getReagentTestMappingHealthTests()));
        if (reagentTestMappingHealthTests.size() != (reagentDTO.getReagentTestMappingHealthTests() == null ? 0
                : reagentDTO.getReagentTestMappingHealthTests().size())) {
            throw new NotFoundException("one of reagentTestMappingHealthTests not found");
        }
        reagent.setReagentTestMappingHealthTests(new HashSet<>(reagentTestMappingHealthTests));
        return reagent;
    }

    private <T> List<T> iterableToList(final Iterable<T> iterable) {
        final List<T> list = new ArrayList<T>();
        iterable.forEach(item -> list.add(item));
        return list;
    }

    public ReferencedWarning getReferencedWarning(final Integer reagentId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Reagent reagent = reagentRepository.findById(reagentId)
                .orElseThrow(NotFoundException::new);
        final ReagentAction reagentReagentAction = reagentActionRepository.findFirstByReagent(reagent);
        if (reagentReagentAction != null) {
            referencedWarning.setKey("reagent.reagentAction.reagent.referenced");
            referencedWarning.addParam(reagentReagentAction.getActionId());
            return referencedWarning;
        }
        return null;
    }

}
