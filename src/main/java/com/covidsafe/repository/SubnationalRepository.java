package com.covidsafe.repository;

import com.covidsafe.models.Subnational;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubnationalRepository extends MongoRepository<Subnational, String> {
    Optional<Subnational> findById(String id);

    Optional<Subnational> findByName(String name);

    Optional<Subnational> findByParentId(String parentId);

    Page<Subnational> findAll(Pageable pageable);

}
