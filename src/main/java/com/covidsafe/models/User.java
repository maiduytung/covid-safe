package com.covidsafe.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "users")
@NoArgsConstructor
public class User extends AuditMetadata {

    @Id
    private String id;

    private String username;

    private String password;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    private boolean active;

}
