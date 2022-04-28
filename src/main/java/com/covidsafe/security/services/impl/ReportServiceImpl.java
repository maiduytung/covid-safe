package com.covidsafe.security.services.impl;

import com.covidsafe.exceptions.BadRequestException;
import com.covidsafe.exceptions.ResourceNotFoundException;
import com.covidsafe.models.Report;
import com.covidsafe.models.Subnational;
import com.covidsafe.payload.request.ReportRequest;
import com.covidsafe.payload.response.ReportResponse;
import com.covidsafe.repository.*;
import com.covidsafe.security.services.ReportService;
import com.covidsafe.utils.AppConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    PriorityRepository priorityRepository;

    @Autowired
    SubnationalRepository subnationalRepository;

    @Autowired
    NationalityRepository nationalityRepository;

    @Autowired
    EthnicRepository ethnicRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> getReportById(String id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaccine Registration", "id", id));

        ReportResponse reportResponse = modelMapper.map(report, ReportResponse.class);

        return new ResponseEntity<>(reportResponse, HttpStatus.OK);
    }

    @Override
    public Page<ReportResponse> getReportsByUserId(int page, int size, String id) {
        if (page < 0 || size < 0 || size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Invalid page or size!");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<Report> reports = reportRepository.findAllByUserId(pageable, id);

        return reports.map(entity -> modelMapper.map(entity, ReportResponse.class));
    }

    @Override
    public Page<ReportResponse> getReports(int page, int size) {
        if (page < 0 || size < 0 || size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Invalid page or size!");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<Report> reports = reportRepository.findAll(pageable);

        return reports.map(entity -> modelMapper.map(entity, ReportResponse.class));
    }

    @Override
    public ResponseEntity<?> addReportByUserId(ReportRequest reportRequest, String id) {

        Report report = new Report();
        report.setUserId(id);
        report.setReport(reportRequest.getReport());

        if (reportRequest.getProvince() == null || reportRequest.getProvince().isBlank()) {
            report.setProvince(null);
        } else {
            Subnational province = subnationalRepository.findById(reportRequest.getProvince())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", reportRequest.getProvince()));
            report.setProvince(province);
        }

        if (reportRequest.getDistrict() == null || reportRequest.getDistrict().isBlank()) {
            report.setDistrict(null);
        } else {
            Subnational district = subnationalRepository.findById(reportRequest.getDistrict())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", reportRequest.getDistrict()));
            report.setDistrict(district);
        }

        if (reportRequest.getWard() == null || reportRequest.getWard().isBlank()) {
            report.setWard(null);
        } else {
            Subnational ward = subnationalRepository.findById(reportRequest.getWard())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", reportRequest.getWard()));
            report.setWard(ward);
        }

        report.setAddress(reportRequest.getAddress());

        report.setStatus(0);

        reportRepository.save(report);

        ReportResponse reportResponse = modelMapper.map(report, ReportResponse.class);

        return new ResponseEntity<>(reportResponse, HttpStatus.OK);
    }

}
