package com.covidsafe.repository;

import com.covidsafe.models.Vaccination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VaccinationRepository extends MongoRepository<Vaccination, String> {
    Optional<Vaccination> findById(String id);

    Optional<Vaccination> findByIdentification(String id);

    Optional<Vaccination> findByVaccineRegistrationId(String id);

    Page<Vaccination> findAll(Pageable pageable);

    Page<Vaccination> findAllByIdentification(Pageable pageable, String id);

    boolean existsById(String id);

}
