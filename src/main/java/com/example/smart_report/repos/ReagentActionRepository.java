package com.example.smart_report.repos;

import com.example.smart_report.domain.Reagent;
import com.example.smart_report.domain.ReagentAction;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ReagentActionRepository extends MongoRepository<ReagentAction, Integer> {

    ReagentAction findFirstByReagent(Reagent reagent);

}
