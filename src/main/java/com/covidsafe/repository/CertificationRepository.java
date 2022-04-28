package com.covidsafe.repository;

import com.covidsafe.models.Certification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CertificationRepository extends MongoRepository<Certification, String> {

    Optional<Certification> findByIdentification(String id);

    Page<Certification> findAll(Pageable pageable);

    boolean existsById(String id);

}
