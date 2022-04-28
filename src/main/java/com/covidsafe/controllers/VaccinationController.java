package com.covidsafe.controllers;

import com.covidsafe.hateoas.event.PaginatedResultsRetrievedEvent;
import com.covidsafe.payload.request.VaccinationRequest;
import com.covidsafe.payload.response.PagedResponse;
import com.covidsafe.payload.response.VaccinationResponse;
import com.covidsafe.security.jwt.CurrentUser;
import com.covidsafe.security.jwt.UserPrincipal;
import com.covidsafe.security.services.VaccinationService;
import com.covidsafe.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class VaccinationController {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    VaccinationService vaccinationService;

    @GetMapping("/user/vaccination")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUserVaccinations(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                        @RequestParam(name = "per_page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                                        @CurrentUser UserPrincipal currentUser,
                                                        UriComponentsBuilder uriBuilder,
                                                        HttpServletResponse response) {
        final Page<VaccinationResponse> resultPage = vaccinationService.getCurrentUserVaccinations(page, size, currentUser);
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                VaccinationResponse.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return ResponseEntity.ok(new PagedResponse<>(resultPage.getTotalElements(), false, resultPage.getContent()));
    }


    @PostMapping("/users/vaccination")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> addVaccination(@Valid @RequestBody VaccinationRequest vaccinationRequest) {
        return vaccinationService.addVaccination(vaccinationRequest);
    }

//    @PutMapping("/user/registration/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
//    public ResponseEntity<?> updateVaccinationById(@Valid @RequestBody VaccinationRequest vaccinationRequest,
//                                                           @CurrentUser UserPrincipal currentUser,
//                                                           @PathVariable("id") String id) {
//        return vaccinationService.updateVaccinationById(vaccinationRequest, id, currentUser.getId());
//    }

    @GetMapping("/users/vaccination")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getVaccinations(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                             @RequestParam(name = "per_page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                             UriComponentsBuilder uriBuilder,
                                             HttpServletResponse response) {
        final Page<VaccinationResponse> resultPage = vaccinationService.getVaccinations(page, size);
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                VaccinationResponse.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return ResponseEntity.ok(new PagedResponse<>(resultPage.getTotalElements(), false, resultPage.getContent()));
    }

    @GetMapping("/users/{id}/vaccination")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getVaccinationsByUserId(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                             @RequestParam(name = "per_page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                                             @PathVariable String id,
                                                             UriComponentsBuilder uriBuilder,
                                                             HttpServletResponse response) {
        final Page<VaccinationResponse> resultPage = vaccinationService.getVaccinationsByUserId(page, size, id);
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                VaccinationResponse.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return ResponseEntity.ok(new PagedResponse<>(resultPage.getTotalElements(), false, resultPage.getContent()));
    }

    @GetMapping("/users/vaccination/identification/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getVaccinationsByIdentification(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                             @RequestParam(name = "per_page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                                             @PathVariable String id,
                                                             UriComponentsBuilder uriBuilder,
                                                             HttpServletResponse response) {
        final Page<VaccinationResponse> resultPage = vaccinationService.getVaccinationsByIdentification(page, size, id);
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                VaccinationResponse.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return ResponseEntity.ok(new PagedResponse<>(resultPage.getTotalElements(), false, resultPage.getContent()));
    }

    @GetMapping("/users/vaccination/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getVaccinationById(@PathVariable String id) {
        return vaccinationService.getVaccinationById(id);
    }

}
