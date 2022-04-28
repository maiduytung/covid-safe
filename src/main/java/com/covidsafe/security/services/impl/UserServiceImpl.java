package com.covidsafe.security.services.impl;

import com.covidsafe.exceptions.BadRequestException;
import com.covidsafe.exceptions.ResourceNotFoundException;
import com.covidsafe.models.Role;
import com.covidsafe.models.User;
import com.covidsafe.payload.request.PasswordRequest;
import com.covidsafe.payload.request.UserRequest;
import com.covidsafe.payload.response.PagedResponse;
import com.covidsafe.payload.response.UserResponse;
import com.covidsafe.repository.RoleRepository;
import com.covidsafe.repository.UserRepository;
import com.covidsafe.security.services.UserService;
import com.covidsafe.utils.AppConstants;
import com.covidsafe.utils.RoleType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", id));

        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> changePassword(PasswordRequest passwordRequest, String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", id));

        if (!encoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password don't match!");
        }

        user.setPassword(encoder.encode(passwordRequest.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<?> getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @Override
    public PagedResponse<?> getUsers(int page, int size) {
        if (page < 0 || size < 0 || size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Invalid page or size!");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);
        Page<User> users = userRepository.findAll(pageable);

        if (users.getNumberOfElements() == 0) {
            return new PagedResponse<>(users.getTotalElements(), false, Collections.emptyList());
        }

        List<UserResponse> userResponses = Arrays.asList(modelMapper.map(users.getContent(), UserResponse[].class));
        return new PagedResponse<>(users.getTotalElements(), false, userResponses);
    }

    @Override
    public ResponseEntity<?> addUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(encoder.encode(userRequest.getPassword()));

        Set<Role> roles = new HashSet<>();

        if (userRequest.getRoles().contains(RoleType.ROLE_MODERATOR.toString())) {
            Role role = roleRepository.findByName(RoleType.ROLE_MODERATOR)
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "Role name", RoleType.ROLE_MODERATOR));
            roles.add(role);
        }

        if (userRequest.getRoles().contains(RoleType.ROLE_USER.toString())) {
            Role role = roleRepository.findByName(RoleType.ROLE_USER)
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "Role name", RoleType.ROLE_USER));
            roles.add(role);
        }

        user.setRoles(roles);
        userRepository.save(user);

        UserResponse userResponse = modelMapper.map(user, UserResponse.class);

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
}
