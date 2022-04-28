package com.covidsafe.repository;

import com.covidsafe.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);

    Page<User> findAll(Pageable pageable);

    Boolean existsByUsername(String username);
}
