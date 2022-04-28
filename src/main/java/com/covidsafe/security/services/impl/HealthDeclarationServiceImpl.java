package com.covidsafe.security.services.impl;

import com.covidsafe.exceptions.BadRequestException;
import com.covidsafe.exceptions.ResourceNotFoundException;
import com.covidsafe.models.HealthDeclaration;
import com.covidsafe.models.Nationality;
import com.covidsafe.models.Subnational;
import com.covidsafe.payload.request.HealthDeclarationRequest;
import com.covidsafe.payload.response.HealthDeclarationResponse;
import com.covidsafe.repository.HealthDeclarationRepository;
import com.covidsafe.repository.NationalityRepository;
import com.covidsafe.repository.SubnationalRepository;
import com.covidsafe.repository.UserRepository;
import com.covidsafe.security.services.HealthDeclarationService;
import com.covidsafe.utils.AppConstants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class HealthDeclarationServiceImpl implements HealthDeclarationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HealthDeclarationRepository healthDeclarationRepository;

    @Autowired
    SubnationalRepository subnationalRepository;

    @Autowired
    NationalityRepository nationalityRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> getDeclarationById(String id) {
        HealthDeclaration healthDeclaration = healthDeclarationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Health declaration", "id", id));

        HealthDeclarationResponse healthDeclarationResponse = modelMapper.map(healthDeclaration, HealthDeclarationResponse.class);

        return new ResponseEntity<>(healthDeclarationResponse, HttpStatus.OK);
    }

    @Override
    public Page<HealthDeclarationResponse> getDeclarationsByUserId(int page, int size, String id) {
        if (page < 0 || size < 0 || size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Invalid page or size!");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<HealthDeclaration> healthDeclarations = healthDeclarationRepository.findAllByUserId(pageable, id);

        return healthDeclarations.map(entity -> modelMapper.map(entity, HealthDeclarationResponse.class));
    }

    @Override
    public Page<HealthDeclarationResponse> getDeclarations(int page, int size) {
        if (page < 0 || size < 0 || size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Invalid page or size!");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<HealthDeclaration> healthDeclarations = healthDeclarationRepository.findAll(pageable);

        return healthDeclarations.map(entity -> modelMapper.map(entity, HealthDeclarationResponse.class));
    }

    @Override
    public ResponseEntity<?> addDeclarationByUserId(HealthDeclarationRequest healthDeclarationRequest, String id) {

        HealthDeclaration healthDeclaration = new HealthDeclaration();
        healthDeclaration.setUserId(id);
        healthDeclaration.setFullName(healthDeclarationRequest.getFullName());
        healthDeclaration.setYearOfBirth(healthDeclarationRequest.getYearOfBirth());
        healthDeclaration.setIdentification(healthDeclarationRequest.getIdentification());
        healthDeclaration.setGender(healthDeclarationRequest.isGender());

        if (healthDeclarationRequest.getNationality() == null || healthDeclarationRequest.getNationality().isBlank()) {
            healthDeclaration.setNationality(null);
        } else {
            Nationality nationality = nationalityRepository.findById(healthDeclarationRequest.getNationality())
                    .orElseThrow(() -> new ResourceNotFoundException("Nationality", "id", healthDeclarationRequest.getNationality()));
            healthDeclaration.setNationality(nationality);
        }

        if (healthDeclarationRequest.getProvince() == null || healthDeclarationRequest.getProvince().isBlank()) {
            healthDeclaration.setProvince(null);
        } else {
            Subnational province = subnationalRepository.findById(healthDeclarationRequest.getProvince())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", healthDeclarationRequest.getProvince()));
            healthDeclaration.setProvince(province);
        }

        if (healthDeclarationRequest.getDistrict() == null || healthDeclarationRequest.getDistrict().isBlank()) {
            healthDeclaration.setDistrict(null);
        } else {
            Subnational district = subnationalRepository.findById(healthDeclarationRequest.getDistrict())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", healthDeclarationRequest.getDistrict()));
            healthDeclaration.setDistrict(district);
        }

        if (healthDeclarationRequest.getWard() == null || healthDeclarationRequest.getWard().isBlank()) {
            healthDeclaration.setWard(null);
        } else {
            Subnational ward = subnationalRepository.findById(healthDeclarationRequest.getWard())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", healthDeclarationRequest.getWard()));
            healthDeclaration.setWard(ward);
        }

        healthDeclaration.setAddress(healthDeclarationRequest.getAddress());
        healthDeclaration.setPhoneNumber(healthDeclarationRequest.getPhoneNumber());
        healthDeclaration.setEmail(healthDeclarationRequest.getEmail());

        healthDeclaration.setVisit(healthDeclarationRequest.isVisit());
        if (healthDeclarationRequest.isVisit()) {
            healthDeclaration.setVisitDetail(healthDeclarationRequest.getVisitDetail());
        }

        healthDeclaration.setSymptoms(healthDeclarationRequest.isSymptoms());
        if (healthDeclarationRequest.isSymptoms()) {
            healthDeclaration.setSymptomsDetail(healthDeclarationRequest.getSymptomsDetail());
        }

        healthDeclaration.setContactSickPeople(healthDeclarationRequest.isContactSickPeople());
        healthDeclaration.setContactEpidemicArea(healthDeclarationRequest.isContactEpidemicArea());
        healthDeclaration.setContactSymptomsPeople(healthDeclarationRequest.isContactSymptomsPeople());

        HealthDeclaration declaration = healthDeclarationRepository.save(healthDeclaration);

        byte[] qrCode = generateQRCode(declaration.getId());
        if (qrCode != null) {
            declaration.setQrCode(new Binary(BsonBinarySubType.BINARY, qrCode));
        }

        healthDeclarationRepository.save(declaration);

        HealthDeclarationResponse healthDeclarationResponse = modelMapper.map(declaration, HealthDeclarationResponse.class);

        return new ResponseEntity<>(healthDeclarationResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateDeclarationById(HealthDeclarationRequest healthDeclarationRequest, String id, String userId) {

        HealthDeclaration healthDeclaration = healthDeclarationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Health declaration", "id", id));

        healthDeclaration.setFullName(healthDeclarationRequest.getFullName());
        healthDeclaration.setYearOfBirth(healthDeclarationRequest.getYearOfBirth());
        healthDeclaration.setIdentification(healthDeclarationRequest.getIdentification());
        healthDeclaration.setGender(healthDeclarationRequest.isGender());

        if (healthDeclarationRequest.getNationality() == null || healthDeclarationRequest.getNationality().isBlank()) {
            healthDeclaration.setNationality(null);
        } else {
            Nationality nationality = nationalityRepository.findById(healthDeclarationRequest.getNationality())
                    .orElseThrow(() -> new ResourceNotFoundException("Nationality", "id", healthDeclarationRequest.getNationality()));
            healthDeclaration.setNationality(nationality);
        }

        if (healthDeclarationRequest.getProvince() == null || healthDeclarationRequest.getProvince().isBlank()) {
            healthDeclaration.setProvince(null);
        } else {
            Subnational province = subnationalRepository.findById(healthDeclarationRequest.getProvince())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", healthDeclarationRequest.getProvince()));
            healthDeclaration.setProvince(province);
        }

        if (healthDeclarationRequest.getDistrict() == null || healthDeclarationRequest.getDistrict().isBlank()) {
            healthDeclaration.setDistrict(null);
        } else {
            Subnational district = subnationalRepository.findById(healthDeclarationRequest.getDistrict())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", healthDeclarationRequest.getDistrict()));
            healthDeclaration.setDistrict(district);
        }

        if (healthDeclarationRequest.getWard() == null || healthDeclarationRequest.getWard().isBlank()) {
            healthDeclaration.setWard(null);
        } else {
            Subnational ward = subnationalRepository.findById(healthDeclarationRequest.getWard())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", healthDeclarationRequest.getWard()));
            healthDeclaration.setWard(ward);
        }

        healthDeclaration.setAddress(healthDeclarationRequest.getAddress());
        healthDeclaration.setPhoneNumber(healthDeclarationRequest.getPhoneNumber());
        healthDeclaration.setEmail(healthDeclarationRequest.getEmail());

        healthDeclaration.setVisit(healthDeclarationRequest.isVisit());
        if (healthDeclarationRequest.isVisit()) {
            healthDeclaration.setVisitDetail(healthDeclarationRequest.getVisitDetail());
        }

        healthDeclaration.setSymptoms(healthDeclarationRequest.isSymptoms());
        if (healthDeclarationRequest.isSymptoms()) {
            healthDeclaration.setSymptomsDetail(healthDeclarationRequest.getSymptomsDetail());
        }

        healthDeclaration.setContactSickPeople(healthDeclarationRequest.isContactSickPeople());
        healthDeclaration.setContactEpidemicArea(healthDeclarationRequest.isContactEpidemicArea());
        healthDeclaration.setContactSymptomsPeople(healthDeclarationRequest.isContactSymptomsPeople());

        healthDeclarationRepository.save(healthDeclaration);

        HealthDeclarationResponse healthDeclarationResponse = modelMapper.map(healthDeclaration, HealthDeclarationResponse.class);
        return new ResponseEntity<>(healthDeclarationResponse, HttpStatus.OK);
    }

    private byte[] generateQRCode(String data) {
        byte[] pngByteArray = null;

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix matrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
            pngByteArray = outputStream.toByteArray();
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }

        return pngByteArray;
    }
}
