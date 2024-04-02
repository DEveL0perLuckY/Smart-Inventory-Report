package com.example.smart_report.repos;

import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.domain.Machine;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface HealthTestRepository extends MongoRepository<HealthTest, Integer> {

    HealthTest findFirstByTestMachineMappingMachines(Machine machine);

    List<HealthTest> findAllByTestMachineMappingMachines(Machine machine);

}
