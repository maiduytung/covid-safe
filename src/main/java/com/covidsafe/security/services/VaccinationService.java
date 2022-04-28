package com.covidsafe.security.services;

import com.covidsafe.payload.request.VaccinationRequest;
import com.covidsafe.payload.response.VaccinationResponse;
import com.covidsafe.security.jwt.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface VaccinationService {

    Page<VaccinationResponse> getCurrentUserVaccinations(int page, int size, UserPrincipal currentUser);

    Page<VaccinationResponse> getVaccinationsByUserId(int page, int size, String id);

    ResponseEntity<?> getVaccinationById(String id);

    Page<VaccinationResponse> getVaccinationsByIdentification(int page, int size, String id);

    Page<VaccinationResponse> getVaccinations(int page, int size);

    ResponseEntity<?> addVaccination(VaccinationRequest vaccinationRequest);

//    ResponseEntity<?> updateVaccinationById(VaccinationRequest vaccinationRequest, String id, String userId);
}
