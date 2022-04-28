package com.covidsafe.security.services.impl;

import com.covidsafe.exceptions.BadRequestException;
import com.covidsafe.exceptions.ResourceNotFoundException;
import com.covidsafe.models.*;
import com.covidsafe.payload.request.VaccineRegistrationRequest;
import com.covidsafe.payload.response.VaccineRegistrationResponse;
import com.covidsafe.repository.*;
import com.covidsafe.security.services.VaccineRegistrationService;
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
public class VaccineRegistrationServiceImpl implements VaccineRegistrationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    VaccineRegistrationRepository vaccineRegistrationRepository;

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
    public ResponseEntity<?> getVaccineRegistrationById(String id) {
        VaccineRegistration vaccineRegistration = vaccineRegistrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaccine Registration", "id", id));

        VaccineRegistrationResponse vaccineRegistrationResponse = modelMapper.map(vaccineRegistration, VaccineRegistrationResponse.class);

        return new ResponseEntity<>(vaccineRegistrationResponse, HttpStatus.OK);
    }

    @Override
    public Page<VaccineRegistrationResponse> getVaccineRegistrationsByUserId(int page, int size, String id) {
        if (page < 0 || size < 0 || size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Invalid page or size!");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<VaccineRegistration> vaccineRegistrations = vaccineRegistrationRepository.findAllByUserId(pageable, id);

        return vaccineRegistrations.map(entity -> modelMapper.map(entity, VaccineRegistrationResponse.class));
    }

    @Override
    public Page<VaccineRegistrationResponse> getVaccineRegistrations(int page, int size) {
        if (page < 0 || size < 0 || size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Invalid page or size!");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<VaccineRegistration> vaccineRegistrations = vaccineRegistrationRepository.findAll(pageable);

        return vaccineRegistrations.map(entity -> modelMapper.map(entity, VaccineRegistrationResponse.class));
    }

    @Override
    public ResponseEntity<?> addVaccineRegistrationByUserId(VaccineRegistrationRequest vaccineRegistrationRequest, String id) {

        VaccineRegistration vaccineRegistration = new VaccineRegistration();
        vaccineRegistration.setUserId(id);
        vaccineRegistration.setFullName(vaccineRegistrationRequest.getFullName());
        vaccineRegistration.setDateOfBirth(vaccineRegistrationRequest.getDateOfBirth());
        vaccineRegistration.setGender(vaccineRegistrationRequest.isGender());
        vaccineRegistration.setIdentification(vaccineRegistrationRequest.getIdentification());
        vaccineRegistration.setHealthInsuranceNumber(vaccineRegistrationRequest.getHealthInsuranceNumber());
        vaccineRegistration.setPreferredVaccinationDate(vaccineRegistrationRequest.getPreferredVaccinationDate());
        vaccineRegistration.setOccupation(vaccineRegistrationRequest.getOccupation());

        if (vaccineRegistrationRequest.getPriority() == null || vaccineRegistrationRequest.getPriority().isBlank()) {
            vaccineRegistration.setPriority(null);
        } else {
            Priority priority = priorityRepository.findById(vaccineRegistrationRequest.getPriority())
                    .orElseThrow(() -> new ResourceNotFoundException("Priority", "id", vaccineRegistrationRequest.getNationality()));
            vaccineRegistration.setPriority(priority);
        }

        if (vaccineRegistrationRequest.getProvince() == null || vaccineRegistrationRequest.getProvince().isBlank()) {
            vaccineRegistration.setProvince(null);
        } else {
            Subnational province = subnationalRepository.findById(vaccineRegistrationRequest.getProvince())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", vaccineRegistrationRequest.getProvince()));
            vaccineRegistration.setProvince(province);
        }

        if (vaccineRegistrationRequest.getDistrict() == null || vaccineRegistrationRequest.getDistrict().isBlank()) {
            vaccineRegistration.setDistrict(null);
        } else {
            Subnational district = subnationalRepository.findById(vaccineRegistrationRequest.getDistrict())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", vaccineRegistrationRequest.getDistrict()));
            vaccineRegistration.setDistrict(district);
        }

        if (vaccineRegistrationRequest.getWard() == null || vaccineRegistrationRequest.getWard().isBlank()) {
            vaccineRegistration.setWard(null);
        } else {
            Subnational ward = subnationalRepository.findById(vaccineRegistrationRequest.getWard())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", vaccineRegistrationRequest.getWard()));
            vaccineRegistration.setWard(ward);
        }

        vaccineRegistration.setAddress(vaccineRegistrationRequest.getAddress());

        if (vaccineRegistrationRequest.getEthnic() == null || vaccineRegistrationRequest.getEthnic().isBlank()) {
            vaccineRegistration.setEthnic(null);
        } else {
            Ethnic ethnic = ethnicRepository.findById(vaccineRegistrationRequest.getEthnic())
                    .orElseThrow(() -> new ResourceNotFoundException("Ethnic", "id", vaccineRegistrationRequest.getEthnic()));
            vaccineRegistration.setEthnic(ethnic);
        }

        if (vaccineRegistrationRequest.getNationality() == null || vaccineRegistrationRequest.getNationality().isBlank()) {
            vaccineRegistration.setNationality(null);
        } else {
            Nationality nationality = nationalityRepository.findById(vaccineRegistrationRequest.getNationality())
                    .orElseThrow(() -> new ResourceNotFoundException("Nationality", "id", vaccineRegistrationRequest.getNationality()));
            vaccineRegistration.setNationality(nationality);
        }

        vaccineRegistration.setAnaphylaxis(vaccineRegistrationRequest.getAnaphylaxis());
        vaccineRegistration.setCovid19(vaccineRegistrationRequest.getCovid19());
        vaccineRegistration.setOtherVaccinations(vaccineRegistrationRequest.getOtherVaccinations());
        vaccineRegistration.setCancer(vaccineRegistrationRequest.getCancer());
        vaccineRegistration.setTakingMedicine(vaccineRegistrationRequest.getTakingMedicine());
        vaccineRegistration.setAcuteIllness(vaccineRegistrationRequest.getAcuteIllness());
        vaccineRegistration.setChronicProgressiveDisease(vaccineRegistrationRequest.getChronicProgressiveDisease());
        vaccineRegistration.setChronicTreatedWell(vaccineRegistrationRequest.getChronicTreatedWell());
        vaccineRegistration.setPregnant(vaccineRegistrationRequest.getPregnant());
        vaccineRegistration.setMoreThan65(vaccineRegistrationRequest.getMoreThan65());
        vaccineRegistration.setCoagulation(vaccineRegistrationRequest.getCoagulation());
        vaccineRegistration.setAllergy(vaccineRegistrationRequest.getAllergy());
        vaccineRegistration.setStatus(0);

        VaccineRegistration registration = vaccineRegistrationRepository.save(vaccineRegistration);

        byte[] qrCode = generateQRCode(registration.getId());
        if (qrCode != null) {
            registration.setQrCode(new Binary(BsonBinarySubType.BINARY, qrCode));
        }

        vaccineRegistrationRepository.save(registration);

        VaccineRegistrationResponse vaccineRegistrationResponse = modelMapper.map(registration, VaccineRegistrationResponse.class);

        return new ResponseEntity<>(vaccineRegistrationResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateVaccineRegistrationById(VaccineRegistrationRequest vaccineRegistrationRequest, String id, String userId) {

        VaccineRegistration vaccineRegistration = vaccineRegistrationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Vaccine Registration", "id", id));

        vaccineRegistration.setFullName(vaccineRegistrationRequest.getFullName());
        vaccineRegistration.setDateOfBirth(vaccineRegistrationRequest.getDateOfBirth());
        vaccineRegistration.setGender(vaccineRegistrationRequest.isGender());
        vaccineRegistration.setIdentification(vaccineRegistrationRequest.getIdentification());
        vaccineRegistration.setHealthInsuranceNumber(vaccineRegistrationRequest.getHealthInsuranceNumber());
        vaccineRegistration.setPreferredVaccinationDate(vaccineRegistrationRequest.getPreferredVaccinationDate());
        vaccineRegistration.setOccupation(vaccineRegistrationRequest.getOccupation());

        if (vaccineRegistrationRequest.getPriority() == null || vaccineRegistrationRequest.getPriority().isBlank()) {
            vaccineRegistration.setPriority(null);
        } else {
            Priority priority = priorityRepository.findById(vaccineRegistrationRequest.getPriority())
                    .orElseThrow(() -> new ResourceNotFoundException("Priority", "id", vaccineRegistrationRequest.getNationality()));
            vaccineRegistration.setPriority(priority);
        }

        if (vaccineRegistrationRequest.getProvince() == null || vaccineRegistrationRequest.getProvince().isBlank()) {
            vaccineRegistration.setProvince(null);
        } else {
            Subnational province = subnationalRepository.findById(vaccineRegistrationRequest.getProvince())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", vaccineRegistrationRequest.getProvince()));
            vaccineRegistration.setProvince(province);
        }

        if (vaccineRegistrationRequest.getDistrict() == null || vaccineRegistrationRequest.getDistrict().isBlank()) {
            vaccineRegistration.setDistrict(null);
        } else {
            Subnational district = subnationalRepository.findById(vaccineRegistrationRequest.getDistrict())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", vaccineRegistrationRequest.getDistrict()));
            vaccineRegistration.setDistrict(district);
        }

        if (vaccineRegistrationRequest.getWard() == null || vaccineRegistrationRequest.getWard().isBlank()) {
            vaccineRegistration.setWard(null);
        } else {
            Subnational ward = subnationalRepository.findById(vaccineRegistrationRequest.getWard())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", vaccineRegistrationRequest.getWard()));
            vaccineRegistration.setWard(ward);
        }

        vaccineRegistration.setAddress(vaccineRegistrationRequest.getAddress());

        if (vaccineRegistrationRequest.getEthnic() == null || vaccineRegistrationRequest.getEthnic().isBlank()) {
            vaccineRegistration.setEthnic(null);
        } else {
            Ethnic ethnic = ethnicRepository.findById(vaccineRegistrationRequest.getEthnic())
                    .orElseThrow(() -> new ResourceNotFoundException("Ethnic", "id", vaccineRegistrationRequest.getEthnic()));
            vaccineRegistration.setEthnic(ethnic);
        }

        if (vaccineRegistrationRequest.getNationality() == null || vaccineRegistrationRequest.getNationality().isBlank()) {
            vaccineRegistration.setNationality(null);
        } else {
            Nationality nationality = nationalityRepository.findById(vaccineRegistrationRequest.getNationality())
                    .orElseThrow(() -> new ResourceNotFoundException("Nationality", "id", vaccineRegistrationRequest.getNationality()));
            vaccineRegistration.setNationality(nationality);
        }

        VaccineRegistration registration = vaccineRegistrationRepository.save(vaccineRegistration);

        VaccineRegistrationResponse vaccineRegistrationResponse = modelMapper.map(registration, VaccineRegistrationResponse.class);

        return new ResponseEntity<>(vaccineRegistrationResponse, HttpStatus.OK);
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
