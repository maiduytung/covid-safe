package com.covidsafe.security.services;

import com.covidsafe.payload.response.CertificationResponse;
import com.covidsafe.security.jwt.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface CertificationService {

    ResponseEntity<?> getCurrentUserCertification(UserPrincipal currentUser);

    ResponseEntity<?> getCertificationByUserId(String id);

    ResponseEntity<?> getCertificationByIdentification(String id);

    Page<CertificationResponse> getCertifications(int page, int size);
}
