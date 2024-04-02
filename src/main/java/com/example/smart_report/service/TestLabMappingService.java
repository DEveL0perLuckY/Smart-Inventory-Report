package com.example.smart_report.service;

import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.domain.TestLabMapping;
import com.example.smart_report.model.TestLabMappingDTO;
import com.example.smart_report.repos.HealthTestRepository;
import com.example.smart_report.repos.TestLabMappingRepository;
import com.example.smart_report.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@SuppressWarnings("null")
@Service
public class TestLabMappingService {

    private final TestLabMappingRepository testLabMappingRepository;
    private final HealthTestRepository healthTestRepository;

    public TestLabMappingService(final TestLabMappingRepository testLabMappingRepository,
            final HealthTestRepository healthTestRepository) {
        this.testLabMappingRepository = testLabMappingRepository;
        this.healthTestRepository = healthTestRepository;
    }

    public List<TestLabMappingDTO> findAll() {
        final List<TestLabMapping> testLabMappings = testLabMappingRepository.findAll(Sort.by("labId"));
        return testLabMappings.stream()
                .map(testLabMapping -> mapToDTO(testLabMapping, new TestLabMappingDTO()))
                .toList();
    }

    public TestLabMappingDTO get(final Integer labId) {
        return testLabMappingRepository.findById(labId)
                .map(testLabMapping -> mapToDTO(testLabMapping, new TestLabMappingDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final TestLabMappingDTO testLabMappingDTO) {
        final TestLabMapping testLabMapping = new TestLabMapping();
        mapToEntity(testLabMappingDTO, testLabMapping);
        return testLabMappingRepository.save(testLabMapping).getLabId();
    }

    public void update(final Integer labId, final TestLabMappingDTO testLabMappingDTO) {
        final TestLabMapping testLabMapping = testLabMappingRepository.findById(labId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(testLabMappingDTO, testLabMapping);
        testLabMappingRepository.save(testLabMapping);
    }

    public void delete(final Integer labId) {
        testLabMappingRepository.deleteById(labId);
    }

    private TestLabMappingDTO mapToDTO(final TestLabMapping testLabMapping,
            final TestLabMappingDTO testLabMappingDTO) {
        testLabMappingDTO.setLabId(testLabMapping.getLabId());
        testLabMappingDTO.setTest(testLabMapping.getTest() == null ? null : testLabMapping.getTest().getTestId());
        return testLabMappingDTO;
    }

    private TestLabMapping mapToEntity(final TestLabMappingDTO testLabMappingDTO,
            final TestLabMapping testLabMapping) {
        final HealthTest test = testLabMappingDTO.getTest() == null ? null
                : healthTestRepository.findById(testLabMappingDTO.getTest())
                        .orElseThrow(() -> new NotFoundException("test not found"));
        testLabMapping.setTest(test);
        return testLabMapping;
    }

}
