package com.covidsafe.repository;

import com.covidsafe.models.Nationality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NationalityRepository extends MongoRepository<Nationality, String> {
    Optional<Nationality> findById(String id);

    Page<Nationality> findAll(Pageable pageable);

}
