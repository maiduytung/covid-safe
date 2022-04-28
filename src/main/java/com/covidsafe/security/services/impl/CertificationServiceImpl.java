package com.covidsafe.security.services.impl;

import com.covidsafe.exceptions.BadRequestException;
import com.covidsafe.exceptions.ResourceNotFoundException;
import com.covidsafe.models.Certification;
import com.covidsafe.models.Profile;
import com.covidsafe.payload.response.CertificationResponse;
import com.covidsafe.repository.CertificationRepository;
import com.covidsafe.repository.ProfileRepository;
import com.covidsafe.security.jwt.UserPrincipal;
import com.covidsafe.security.services.CertificationService;
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
public class CertificationServiceImpl implements CertificationService {

    @Autowired
    CertificationRepository certificationRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> getCurrentUserCertification(UserPrincipal currentUser) {
        Profile profile = profileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new BadRequestException("Identification not update"));

        Certification certification = certificationRepository.findByIdentification(profile.getIdentification())
                .orElseThrow(() -> new ResourceNotFoundException("Certification", "id", profile.getIdentification()));

        CertificationResponse certificationResponse = modelMapper.map(certification, CertificationResponse.class);

        return new ResponseEntity<>(certificationResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getCertificationByUserId(String id) {
        Profile profile = profileRepository.findByUserId(id)
                .orElseThrow(() -> new BadRequestException("Identification not update"));

        Certification certification = certificationRepository.findByIdentification(profile.getIdentification())
                .orElseThrow(() -> new ResourceNotFoundException("Certification", "id", profile.getIdentification()));

        CertificationResponse certificationResponse = modelMapper.map(certification, CertificationResponse.class);

        return new ResponseEntity<>(certificationResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getCertificationByIdentification(String id) {
        Certification certification = certificationRepository.findByIdentification(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certification", "id", id));

        CertificationResponse certificationResponse = modelMapper.map(certification, CertificationResponse.class);

        return new ResponseEntity<>(certificationResponse, HttpStatus.OK);
    }

    @Override
    public Page<CertificationResponse> getCertifications(int page, int size) {
        if (page < 0 || size < 0 || size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Invalid page or size!");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<Certification> certifications = certificationRepository.findAll(pageable);

        return certifications.map(entity -> modelMapper.map(entity, CertificationResponse.class));
    }
}
