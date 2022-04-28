package com.covidsafe.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class UserResponse {
    private String id;
    private String username;
    private Set<String> roles;
    private boolean active;
    @JsonProperty("last_modified_date")
    private Instant lastModifiedDate;
}
