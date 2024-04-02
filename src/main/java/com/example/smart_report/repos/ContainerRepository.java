package com.example.smart_report.repos;

import com.example.smart_report.domain.Container;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ContainerRepository extends MongoRepository<Container, Integer> {
}
