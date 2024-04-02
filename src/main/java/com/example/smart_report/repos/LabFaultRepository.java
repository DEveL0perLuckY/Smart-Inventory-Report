package com.example.smart_report.repos;

import com.example.smart_report.domain.LabFault;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface LabFaultRepository extends MongoRepository<LabFault, Integer> {
}
