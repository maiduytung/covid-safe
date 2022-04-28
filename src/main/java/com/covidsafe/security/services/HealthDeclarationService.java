package com.covidsafe.security.services;

import com.covidsafe.payload.request.HealthDeclarationRequest;
import com.covidsafe.payload.response.HealthDeclarationResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface HealthDeclarationService {

    ResponseEntity<?> getDeclarationById(String id);

    Page<HealthDeclarationResponse> getDeclarationsByUserId(int page, int size, String id);

    Page<HealthDeclarationResponse> getDeclarations(int page, int size);

    ResponseEntity<?> addDeclarationByUserId(HealthDeclarationRequest healthDeclarationRequest, String id);

    ResponseEntity<?> updateDeclarationById(HealthDeclarationRequest healthDeclarationRequest, String id, String userId);
}
