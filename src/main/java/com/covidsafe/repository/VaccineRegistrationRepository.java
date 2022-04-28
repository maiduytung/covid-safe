package com.covidsafe.repository;

import com.covidsafe.models.VaccineRegistration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VaccineRegistrationRepository extends MongoRepository<VaccineRegistration, String> {
    Optional<VaccineRegistration> findById(String id);

    Optional<VaccineRegistration> findByIdAndUserId(String id, String userId);

    Page<VaccineRegistration> findAll(Pageable pageable);

    Page<VaccineRegistration> findAllByUserId(Pageable pageable, String id);

    boolean existsById(String id);

}
