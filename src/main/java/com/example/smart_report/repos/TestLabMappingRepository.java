package com.example.smart_report.repos;

import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.domain.TestLabMapping;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TestLabMappingRepository extends MongoRepository<TestLabMapping, Integer> {

    TestLabMapping findFirstByTest(HealthTest healthTest);

}
