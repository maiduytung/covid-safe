package com.covidsafe.repository;

import com.covidsafe.models.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PriorityRepository extends MongoRepository<Priority, String> {
    Optional<Priority> findById(String id);

    Optional<Priority> findByName(String name);

    Page<Priority> findAll(Pageable pageable);

}
