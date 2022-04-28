package com.covidsafe.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UserRequest {

    @NotBlank
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank
    private String username;
    
    @NotBlank
    @Size(min = 8)
    private String password;
    
    private Set<String> roles;
    
}
