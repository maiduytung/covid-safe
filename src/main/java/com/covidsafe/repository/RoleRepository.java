package com.covidsafe.repository;

import com.covidsafe.models.Role;
import com.covidsafe.utils.RoleType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(RoleType name);
}
