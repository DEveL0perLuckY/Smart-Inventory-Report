package com.example.smart_report.repos;

import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.domain.Reagent;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ReagentRepository extends MongoRepository<Reagent, Integer> {

    Reagent findFirstByReagentTestMappingHealthTests(HealthTest healthTest);

    List<Reagent> findAllByReagentTestMappingHealthTests(HealthTest healthTest);

}
