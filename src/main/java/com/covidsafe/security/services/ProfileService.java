package com.covidsafe.security.services;

import com.covidsafe.payload.request.ProfileRequest;
import com.covidsafe.payload.response.PagedResponse;
import org.springframework.http.ResponseEntity;

public interface ProfileService {

    ResponseEntity<?> getProfileByUserId(String id);

    PagedResponse<?> getProfiles(int page, int size);

    ResponseEntity<?> updateProfileByUserId(ProfileRequest profileRequest, String id);
}
