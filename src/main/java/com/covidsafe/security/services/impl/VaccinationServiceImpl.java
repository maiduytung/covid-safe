package com.covidsafe.security.services.impl;

import com.covidsafe.exceptions.BadRequestException;
import com.covidsafe.exceptions.ResourceNotFoundException;
import com.covidsafe.models.Certification;
import com.covidsafe.models.Profile;
import com.covidsafe.models.Vaccination;
import com.covidsafe.models.VaccineRegistration;
import com.covidsafe.payload.request.VaccinationRequest;
import com.covidsafe.payload.response.VaccinationResponse;
import com.covidsafe.repository.*;
import com.covidsafe.security.jwt.UserPrincipal;
import com.covidsafe.security.services.VaccinationService;
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
public class VaccinationServiceImpl implements VaccinationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    VaccinationRepository vaccinationRepository;

    @Autowired
    VaccineRegistrationRepository vaccineRegistrationRepository;

    @Autowired
    CertificationRepository certificationRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<VaccinationResponse> getCurrentUserVaccinations(int page, int size, UserPrincipal currentUser) {
        Profile profile = profileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "id", currentUser.getId()));
        return getVaccinationsByIdentification(page, size, profile.getIdentification());
    }

    @Override
    public Page<VaccinationResponse> getVaccinationsByUserId(int page, int size, String id) {
        Profile profile = profileRepository.findByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "id", id));
        return getVaccinationsByIdentification(page, size, profile.getIdentification());
    }

    @Override
    public ResponseEntity<?> getVaccinationById(String id) {
        Vaccination vaccination = vaccinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaccine Registration", "id", id));

        VaccinationResponse vaccinationResponse = modelMapper.map(vaccination, VaccinationResponse.class);

        return new ResponseEntity<>(vaccinationResponse, HttpStatus.OK);
    }

    @Override
    public Page<VaccinationResponse> getVaccinationsByIdentification(int page, int size, String id) {
        if (page < 0 || size < 0 || size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Invalid page or size!");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<Vaccination> vaccinations = vaccinationRepository.findAllByIdentification(pageable, id);

        return vaccinations.map(entity -> modelMapper.map(entity, VaccinationResponse.class));
    }

    @Override
    public Page<VaccinationResponse> getVaccinations(int page, int size) {
        if (page < 0 || size < 0 || size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Invalid page or size!");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<Vaccination> vaccinations = vaccinationRepository.findAll(pageable);

        return vaccinations.map(entity -> modelMapper.map(entity, VaccinationResponse.class));
    }

    @Override
    public ResponseEntity<?> addVaccination(VaccinationRequest vaccinationRequest) {

        Vaccination vaccination = new Vaccination();
        vaccination.setIdentification(vaccinationRequest.getIdentification());

        vaccination.setFullName(vaccinationRequest.getFullName());
        vaccination.setDateOfBirth(vaccinationRequest.getDateOfBirth());
        vaccination.setGender(vaccinationRequest.isGender());

        if (vaccinationRequest.getVaccineRegistrationId() == null || vaccinationRequest.getVaccineRegistrationId().isBlank()) {
            vaccination.setVaccineRegistrationId(null);
        } else {
            VaccineRegistration vaccineRegistration = vaccineRegistrationRepository.findById(vaccinationRequest.getVaccineRegistrationId())
                    .orElseThrow(() -> new ResourceNotFoundException("VaccineRegistration", "id", vaccinationRequest.getVaccineRegistrationId()));
            vaccination.setVaccineRegistrationId(vaccineRegistration.getId());
        }

        vaccination.setAddress(vaccinationRequest.getAddress());
        vaccination.setVaccinationDate(vaccinationRequest.getVaccinationDate());
        vaccination.setVaccinationType(vaccinationRequest.getVaccinationType());
        vaccination.setVaccinationCenter(vaccinationRequest.getVaccinationCenter());

        Vaccination vac = vaccinationRepository.save(vaccination);

        Certification certification = certificationRepository.findByIdentification(vac.getIdentification())
                .orElse(new Certification());
        certification.setIdentification(vaccinationRequest.getIdentification());
        certification.setCountTheDose(certification.getCountTheDose() + 1);
        certificationRepository.save(certification);

        VaccinationResponse vaccinationResponse = modelMapper.map(vac, VaccinationResponse.class);

        return new ResponseEntity<>(vaccinationResponse, HttpStatus.OK);
    }

//    @Override
//    public ResponseEntity<?> updateVaccinationById(VaccinationRequest vaccinationRequest, String id, String userId) {
//
//        Vaccination vaccination = vaccinationRepository.findByIdAndUserId(id, userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Vaccine Registration", "id", id));
//
//        vaccination.setFullName(vaccinationRequest.getFullName());
//        vaccination.setDateOfBirth(vaccinationRequest.getDateOfBirth());
//        vaccination.setGender(vaccinationRequest.isGender());
//        vaccination.setIdentification(vaccinationRequest.getIdentification());
//        vaccination.setHealthInsuranceNumber(vaccinationRequest.getHealthInsuranceNumber());
//        vaccination.setPreferredVaccinationDate(vaccinationRequest.getPreferredVaccinationDate());
//        vaccination.setOccupation(vaccinationRequest.getOccupation());
//
//        if (vaccinationRequest.getPriority() == null || vaccinationRequest.getPriority().isBlank()) {
//            vaccination.setPriority(null);
//        } else {
//            Priority priority = priorityRepository.findById(vaccinationRequest.getPriority())
//                    .orElseThrow(() -> new ResourceNotFoundException("Priority", "id", vaccinationRequest.getNationality()));
//            vaccination.setPriority(priority);
//        }
//
//        if (vaccinationRequest.getProvince() == null || vaccinationRequest.getProvince().isBlank()) {
//            vaccination.setProvince(null);
//        } else {
//            Subnational province = subnationalRepository.findById(vaccinationRequest.getProvince())
//                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", vaccinationRequest.getProvince()));
//            vaccination.setProvince(province);
//        }
//
//        if (vaccinationRequest.getDistrict() == null || vaccinationRequest.getDistrict().isBlank()) {
//            vaccination.setDistrict(null);
//        } else {
//            Subnational district = subnationalRepository.findById(vaccinationRequest.getDistrict())
//                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", vaccinationRequest.getDistrict()));
//            vaccination.setDistrict(district);
//        }
//
//        if (vaccinationRequest.getWard() == null || vaccinationRequest.getWard().isBlank()) {
//            vaccination.setWard(null);
//        } else {
//            Subnational ward = subnationalRepository.findById(vaccinationRequest.getWard())
//                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", vaccinationRequest.getWard()));
//            vaccination.setWard(ward);
//        }
//
//        vaccination.setAddress(vaccinationRequest.getAddress());
//
//        if (vaccinationRequest.getEthnic() == null || vaccinationRequest.getEthnic().isBlank()) {
//            vaccination.setEthnic(null);
//        } else {
//            Ethnic ethnic = ethnicRepository.findById(vaccinationRequest.getEthnic())
//                    .orElseThrow(() -> new ResourceNotFoundException("Ethnic", "id", vaccinationRequest.getEthnic()));
//            vaccination.setEthnic(ethnic);
//        }
//
//        if (vaccinationRequest.getNationality() == null || vaccinationRequest.getNationality().isBlank()) {
//            vaccination.setNationality(null);
//        } else {
//            Nationality nationality = nationalityRepository.findById(vaccinationRequest.getNationality())
//                    .orElseThrow(() -> new ResourceNotFoundException("Nationality", "id", vaccinationRequest.getNationality()));
//            vaccination.setNationality(nationality);
//        }
//
//        Vaccination registration = vaccinationRepository.save(vaccination);
//
//        VaccinationResponse vaccinationResponse = modelMapper.map(registration, VaccinationResponse.class);
//
//        return new ResponseEntity<>(vaccinationResponse, HttpStatus.OK);
//    }
}
