package com.covidsafe.security.services;

import com.covidsafe.payload.request.VaccineRegistrationRequest;
import com.covidsafe.payload.response.VaccineRegistrationResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface VaccineRegistrationService {

    ResponseEntity<?> getVaccineRegistrationById(String id);

    Page<VaccineRegistrationResponse> getVaccineRegistrationsByUserId(int page, int size, String id);

    Page<VaccineRegistrationResponse> getVaccineRegistrations(int page, int size);

    ResponseEntity<?> addVaccineRegistrationByUserId(VaccineRegistrationRequest vaccineRegistrationRequest, String id);

    ResponseEntity<?> updateVaccineRegistrationById(VaccineRegistrationRequest vaccineRegistrationRequest, String id, String userId);
}
