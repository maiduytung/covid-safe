package com.covidsafe.security.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailsService {
    UserDetails loadUserById(String userId);
}
