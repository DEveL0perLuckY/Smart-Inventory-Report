package com.example.smart_report.repos;

import com.example.smart_report.domain.HealthParameter;
import com.example.smart_report.domain.HealthTest;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface HealthParameterRepository extends MongoRepository<HealthParameter, Integer> {

    HealthParameter findFirstByTest(HealthTest healthTest);

}
