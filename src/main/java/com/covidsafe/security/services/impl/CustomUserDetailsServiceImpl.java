package com.covidsafe.security.services.impl;

import com.covidsafe.security.jwt.UserPrincipal;
import com.covidsafe.security.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.covidsafe.models.User;
import com.covidsafe.repository.UserRepository;

import java.util.Optional;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService, CustomUserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        return user.map(UserPrincipal::build).orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching username in the database for " + username));
    }

    @Override
    @Transactional
    public UserDetails loadUserById(String userId) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(userId);

        return user.map(UserPrincipal::build).orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching userId in the database for " + userId));
    }

}
