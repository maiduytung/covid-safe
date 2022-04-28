package com.covidsafe.repository;

import com.covidsafe.models.OTP;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TwilioRepository extends MongoRepository<OTP, String> {
}
