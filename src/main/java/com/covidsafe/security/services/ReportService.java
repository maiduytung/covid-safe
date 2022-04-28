package com.covidsafe.security.services;

import com.covidsafe.payload.request.ReportRequest;
import com.covidsafe.payload.response.ReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface ReportService {

    ResponseEntity<?> getReportById(String id);

    Page<ReportResponse> getReportsByUserId(int page, int size, String id);

    Page<ReportResponse> getReports(int page, int size);

    ResponseEntity<?> addReportByUserId(ReportRequest reportRequest, String id);
}
