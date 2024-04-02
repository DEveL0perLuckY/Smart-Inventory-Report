package com.example.smart_report.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.smart_report.domain.Role;
import com.example.smart_report.domain.User;

public interface UserRepository extends MongoRepository<User, Integer> {

    User findFirstByRoleId(Role role);

    List<User> findAllByRoleId(Role role);

    Optional<User> findByEmail(String email);

}
