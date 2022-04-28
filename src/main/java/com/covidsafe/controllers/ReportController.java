package com.covidsafe.controllers;

import com.covidsafe.hateoas.event.PaginatedResultsRetrievedEvent;
import com.covidsafe.payload.request.ReportRequest;
import com.covidsafe.payload.response.PagedResponse;
import com.covidsafe.payload.response.ReportResponse;
import com.covidsafe.security.jwt.CurrentUser;
import com.covidsafe.security.jwt.UserPrincipal;
import com.covidsafe.security.services.ReportService;
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
public class ReportController {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    ReportService reportService;

    @GetMapping("/user/report")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUserReports(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                                @RequestParam(name = "per_page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                                                @CurrentUser UserPrincipal currentUser,
                                                                UriComponentsBuilder uriBuilder,
                                                                HttpServletResponse response) {
        final Page<ReportResponse> resultPage = reportService.getReportsByUserId(page, size, currentUser.getId());
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                ReportResponse.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return ResponseEntity.ok(new PagedResponse<>(resultPage.getTotalElements(), false, resultPage.getContent()));
    }

    @PostMapping("/user/report")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addReportByUserId(@Valid @RequestBody ReportRequest reportRequest,
                                                            @CurrentUser UserPrincipal currentUser) {
        return reportService.addReportByUserId(reportRequest, currentUser.getId());
    }

    @GetMapping("/users/report")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getReports(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                     @RequestParam(name = "per_page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                                     UriComponentsBuilder uriBuilder,
                                                     HttpServletResponse response) {
        final Page<ReportResponse> resultPage = reportService.getReports(page, size);
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                ReportResponse.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return ResponseEntity.ok(new PagedResponse<>(resultPage.getTotalElements(), false, resultPage.getContent()));
    }

    @GetMapping("/users/{id}/report")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getReportsByUserId(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                             @RequestParam(name = "per_page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                                             @PathVariable String id,
                                                             UriComponentsBuilder uriBuilder,
                                                             HttpServletResponse response) {
        final Page<ReportResponse> resultPage = reportService.getReportsByUserId(page, size, id);
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                ReportResponse.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return ResponseEntity.ok(new PagedResponse<>(resultPage.getTotalElements(), false, resultPage.getContent()));
    }

    @GetMapping("/users/report/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> getReportById(@PathVariable String id) {
        return reportService.getReportById(id);
    }

}
