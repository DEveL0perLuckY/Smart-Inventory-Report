package com.example.smart_report.repos;

import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.domain.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface InventoryRepository extends MongoRepository<Inventory, Integer> {

    Inventory findFirstByItem(HealthTest healthTest);

}
