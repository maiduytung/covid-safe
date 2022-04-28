package com.covidsafe.repository;

import com.covidsafe.models.HealthDeclaration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface HealthDeclarationRepository extends MongoRepository<HealthDeclaration, String> {
    Optional<HealthDeclaration> findById(String id);

    Optional<HealthDeclaration> findByIdAndUserId(String id, String userId);

    Page<HealthDeclaration> findAll(Pageable pageable);

    Page<HealthDeclaration> findAllByUserId(Pageable pageable, String id);

    boolean existsById(String id);

}
