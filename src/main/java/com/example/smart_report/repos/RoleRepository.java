package com.example.smart_report.repos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.smart_report.domain.Role;

public interface RoleRepository extends MongoRepository<Role, Integer> {

}
