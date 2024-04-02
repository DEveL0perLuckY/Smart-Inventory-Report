package com.example.smart_report.repos;

import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.domain.Machine;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface MachineRepository extends MongoRepository<Machine, Integer> {

    Machine findFirstByTest(HealthTest healthTest);

}
