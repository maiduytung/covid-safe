package com.covidsafe.controllers;

import com.covidsafe.payload.request.UserRequest;
import com.covidsafe.payload.request.PasswordRequest;
import com.covidsafe.payload.response.PagedResponse;
import com.covidsafe.security.jwt.CurrentUser;
import com.covidsafe.security.jwt.UserPrincipal;
import com.covidsafe.security.services.UserService;
import com.covidsafe.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal authenticatedUser) {
        return userService.getUserById(authenticatedUser.getId());
    }

    @PutMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    public ResponseEntity<?> updateCurrentUser(@Valid @RequestBody PasswordRequest passwordRequest, @CurrentUser UserPrincipal currentUser) {
        return userService.changePassword(passwordRequest, currentUser.getId());
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public PagedResponse<?> getUsers(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                     @RequestParam(name = "per_page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        return userService.getUsers(page, size);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserRequest userRequest) {
        return userService.addUser(userRequest);
    }
}
