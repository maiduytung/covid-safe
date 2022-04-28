package com.covidsafe.security.services;

import com.covidsafe.payload.request.UserRequest;
import com.covidsafe.payload.request.PasswordRequest;
import com.covidsafe.payload.response.PagedResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> getUserById(String id);

    ResponseEntity<?> changePassword(PasswordRequest passwordRequest, String id);

    ResponseEntity<?> getUserByUsername(String username);

    PagedResponse<?> getUsers(int page, int size);

    ResponseEntity<?> addUser(UserRequest userRequest);

}
