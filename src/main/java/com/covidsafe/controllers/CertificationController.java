package com.covidsafe.controllers;

import com.covidsafe.hateoas.event.PaginatedResultsRetrievedEvent;
import com.covidsafe.payload.response.CertificationResponse;
import com.covidsafe.payload.response.PagedResponse;
import com.covidsafe.security.jwt.CurrentUser;
import com.covidsafe.security.jwt.UserPrincipal;
import com.covidsafe.security.services.CertificationService;
import com.covidsafe.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CertificationController {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    CertificationService certificationService;

    @GetMapping("/user/certification")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUserCertification(@CurrentUser UserPrincipal currentUser) {
        return certificationService.getCurrentUserCertification(currentUser);
    }

    @GetMapping("/users/{id}/certification")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getCertificationByUserId(@PathVariable String id) {
        return certificationService.getCertificationByUserId(id);
    }

    @GetMapping("/users/certification/identification/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getCertificationByIdentification(@PathVariable String id) {
        return certificationService.getCertificationByIdentification(id);
    }

    @GetMapping("/users/certification")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getCertifications(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                               @RequestParam(name = "per_page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                               UriComponentsBuilder uriBuilder,
                                               HttpServletResponse response) {
        final Page<CertificationResponse> resultPage = certificationService.getCertifications(page, size);
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                CertificationResponse.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return ResponseEntity.ok(new PagedResponse<>(resultPage.getTotalElements(), false, resultPage.getContent()));
    }
}
