package com.covidsafe.repository;

import com.covidsafe.models.Ethnic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EthnicRepository extends MongoRepository<Ethnic, String> {
    Optional<Ethnic> findById(String id);

    Optional<Ethnic> findByName(String name);

    Page<Ethnic> findAll(Pageable pageable);

}
