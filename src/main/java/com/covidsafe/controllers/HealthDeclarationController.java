package com.covidsafe.controllers;

import com.covidsafe.hateoas.event.PaginatedResultsRetrievedEvent;
import com.covidsafe.payload.request.HealthDeclarationRequest;
import com.covidsafe.payload.response.HealthDeclarationResponse;
import com.covidsafe.payload.response.PagedResponse;
import com.covidsafe.security.jwt.CurrentUser;
import com.covidsafe.security.jwt.UserPrincipal;
import com.covidsafe.security.services.HealthDeclarationService;
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
public class HealthDeclarationController {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    HealthDeclarationService healthDeclarationService;

    @GetMapping("/user/declaration")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUserDeclarations(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                        @RequestParam(name = "per_page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                                        @CurrentUser UserPrincipal currentUser,
                                                        UriComponentsBuilder uriBuilder,
                                                        HttpServletResponse response) {
        final Page<HealthDeclarationResponse> resultPage = healthDeclarationService.getDeclarationsByUserId(page, size, currentUser.getId());
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                HealthDeclarationResponse.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return ResponseEntity.ok(new PagedResponse<>(resultPage.getTotalElements(), false, resultPage.getContent()));
    }

    @PostMapping("/user/declaration")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addDeclarationByUserId(@Valid @RequestBody HealthDeclarationRequest healthDeclarationRequest,
                                                    @CurrentUser UserPrincipal currentUser) {
        return healthDeclarationService.addDeclarationByUserId(healthDeclarationRequest, currentUser.getId());
    }

    @PutMapping("/user/declaration/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateDeclarationById(@Valid @RequestBody HealthDeclarationRequest healthDeclarationRequest,
                                                   @CurrentUser UserPrincipal currentUser,
                                                   @PathVariable("id") String id) {
        return healthDeclarationService.updateDeclarationById(healthDeclarationRequest, id, currentUser.getId());
    }

    @GetMapping("/users/declaration")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getDeclarations(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                             @RequestParam(name = "per_page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                             UriComponentsBuilder uriBuilder,
                                             HttpServletResponse response) {
        final Page<HealthDeclarationResponse> resultPage = healthDeclarationService.getDeclarations(page, size);
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                HealthDeclarationResponse.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return ResponseEntity.ok(new PagedResponse<>(resultPage.getTotalElements(), false, resultPage.getContent()));
    }

    @GetMapping("/users/{id}/declaration")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getDeclarationsByUserId(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                     @RequestParam(name = "per_page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                                     @PathVariable String id,
                                                     UriComponentsBuilder uriBuilder,
                                                     HttpServletResponse response) {
        final Page<HealthDeclarationResponse> resultPage = healthDeclarationService.getDeclarationsByUserId(page, size, id);
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                HealthDeclarationResponse.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return ResponseEntity.ok(new PagedResponse<>(resultPage.getTotalElements(), false, resultPage.getContent()));
    }

    @GetMapping("/users/declaration/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getDeclarationById(@PathVariable String id) {
        return healthDeclarationService.getDeclarationById(id);
    }

}
