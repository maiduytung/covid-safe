package com.covidsafe.repository;

import com.covidsafe.models.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReportRepository extends MongoRepository<Report, String> {
    Optional<Report> findById(String id);

    Optional<Report> findByIdAndUserId(String id, String userId);

    Page<Report> findAll(Pageable pageable);

    Page<Report> findAllByUserId(Pageable pageable, String id);

    boolean existsById(String id);

}
