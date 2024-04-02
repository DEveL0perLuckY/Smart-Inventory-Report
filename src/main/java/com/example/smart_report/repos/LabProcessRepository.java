package com.example.smart_report.repos;

import com.example.smart_report.domain.LabProcess;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface LabProcessRepository extends MongoRepository<LabProcess, Integer> {
}
