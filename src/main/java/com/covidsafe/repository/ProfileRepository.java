package com.covidsafe.repository;

import com.covidsafe.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfileRepository extends MongoRepository<Profile, String> {
    Optional<Profile> findById(String id);

    Optional<Profile> findByUserId(String id);

    Page<Profile> findAll(Pageable pageable);

    Boolean existsByUserId(String id);
}
