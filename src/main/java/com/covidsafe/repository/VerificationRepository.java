package com.covidsafe.repository;

import com.covidsafe.models.Verification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerificationRepository extends MongoRepository<Verification, String> {
}
