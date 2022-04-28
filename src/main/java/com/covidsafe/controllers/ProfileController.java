package com.covidsafe.controllers;

import com.covidsafe.payload.request.ProfileRequest;
import com.covidsafe.payload.response.PagedResponse;
import com.covidsafe.security.jwt.CurrentUser;
import com.covidsafe.security.jwt.UserPrincipal;
import com.covidsafe.security.services.ProfileService;
import com.covidsafe.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    @GetMapping("/user/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    public ResponseEntity<?> getCurrentUserProfile(@CurrentUser UserPrincipal currentUser) {
        return profileService.getProfileByUserId(currentUser.getId());
    }

    @PutMapping("/user/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    public ResponseEntity<?> updateCurrentUserProfile(@Valid @RequestBody ProfileRequest profileRequest, @CurrentUser UserPrincipal currentUser) {
        return profileService.updateProfileByUserId(profileRequest, currentUser.getId());
    }

    @GetMapping("/users/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public PagedResponse<?> getAll(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                   @RequestParam(name = "per_page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        return profileService.getProfiles(page, size);
    }

    @GetMapping("/users/{id}/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getUserProfileByUserId(@PathVariable String id) {
        return profileService.getProfileByUserId(id);
    }
}
