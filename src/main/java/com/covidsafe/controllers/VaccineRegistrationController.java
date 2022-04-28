package com.covidsafe.controllers;

import com.covidsafe.hateoas.event.PaginatedResultsRetrievedEvent;
import com.covidsafe.payload.request.VaccineRegistrationRequest;
import com.covidsafe.payload.response.VaccineRegistrationResponse;
import com.covidsafe.payload.response.PagedResponse;
import com.covidsafe.security.jwt.CurrentUser;
import com.covidsafe.security.jwt.UserPrincipal;
import com.covidsafe.security.services.VaccineRegistrationService;
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
public class VaccineRegistrationController {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    VaccineRegistrationService vaccineRegistrationService;

    @GetMapping("/user/registration")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUserVaccineRegistrations(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                                @RequestParam(name = "per_page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                                                @CurrentUser UserPrincipal currentUser,
                                                                UriComponentsBuilder uriBuilder,
                                                                HttpServletResponse response) {
        final Page<VaccineRegistrationResponse> resultPage = vaccineRegistrationService.getVaccineRegistrationsByUserId(page, size, currentUser.getId());
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                VaccineRegistrationResponse.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return ResponseEntity.ok(new PagedResponse<>(resultPage.getTotalElements(), false, resultPage.getContent()));
    }

    @PostMapping("/user/registration")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addVaccineRegistrationByUserId(@Valid @RequestBody VaccineRegistrationRequest vaccineRegistrationRequest,
                                                            @CurrentUser UserPrincipal currentUser) {
        return vaccineRegistrationService.addVaccineRegistrationByUserId(vaccineRegistrationRequest, currentUser.getId());
    }

    @PutMapping("/user/registration/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    public ResponseEntity<?> updateVaccineRegistrationById(@Valid @RequestBody VaccineRegistrationRequest vaccineRegistrationRequest,
                                                           @CurrentUser UserPrincipal currentUser,
                                                           @PathVariable("id") String id) {
        return vaccineRegistrationService.updateVaccineRegistrationById(vaccineRegistrationRequest, id, currentUser.getId());
    }

    @GetMapping("/users/registration")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getVaccineRegistrations(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                     @RequestParam(name = "per_page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                                     UriComponentsBuilder uriBuilder,
                                                     HttpServletResponse response) {
        final Page<VaccineRegistrationResponse> resultPage = vaccineRegistrationService.getVaccineRegistrations(page, size);
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                VaccineRegistrationResponse.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return ResponseEntity.ok(new PagedResponse<>(resultPage.getTotalElements(), false, resultPage.getContent()));
    }

    @GetMapping("/users/{id}/registration")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getVaccineRegistrationsByUserId(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                             @RequestParam(name = "per_page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                                             @PathVariable String id,
                                                             UriComponentsBuilder uriBuilder,
                                                             HttpServletResponse response) {
        final Page<VaccineRegistrationResponse> resultPage = vaccineRegistrationService.getVaccineRegistrationsByUserId(page, size, id);
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                VaccineRegistrationResponse.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return ResponseEntity.ok(new PagedResponse<>(resultPage.getTotalElements(), false, resultPage.getContent()));
    }

    @GetMapping("/users/registration/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getVaccineRegistrationById(@PathVariable String id) {
        return vaccineRegistrationService.getVaccineRegistrationById(id);
    }

}
