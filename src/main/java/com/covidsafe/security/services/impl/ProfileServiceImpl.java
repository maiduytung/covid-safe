package com.covidsafe.security.services.impl;

import com.covidsafe.exceptions.BadRequestException;
import com.covidsafe.exceptions.ResourceNotFoundException;
import com.covidsafe.models.*;
import com.covidsafe.payload.request.ProfileRequest;
import com.covidsafe.payload.response.PagedResponse;
import com.covidsafe.payload.response.ProfileResponse;
import com.covidsafe.repository.*;
import com.covidsafe.security.services.ProfileService;
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
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SubnationalRepository subnationalRepository;

    @Autowired
    NationalityRepository nationalityRepository;

    @Autowired
    EthnicRepository ethnicRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> getProfileByUserId(String id) {
        Profile profile = profileRepository.findByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "UserId", id));

        ProfileResponse profileResponse = modelMapper.map(profile, ProfileResponse.class);
        return new ResponseEntity<>(profileResponse, HttpStatus.OK);
    }

    @Override
    public PagedResponse<?> getProfiles(int page, int size) {
        if (page < 0 || size < 0 || size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Invalid page or size!");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);
        Page<Profile> profiles = profileRepository.findAll(pageable);

        if (profiles.getNumberOfElements() == 0) {
            return new PagedResponse<>(profiles.getTotalElements(), false, Collections.emptyList());
        }

        List<ProfileResponse> profileResponses = Arrays.asList(modelMapper.map(profiles.getContent(), ProfileResponse[].class));
        return new PagedResponse<>(profiles.getTotalElements(), false, profileResponses);
    }

    @Override
    public ResponseEntity<?> updateProfileByUserId(ProfileRequest profileRequest, String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        Profile profile = profileRepository.findByUserId(user.getId()).orElse(new Profile());

        profile.setUserId(user.getId());
        profile.setFullName(profileRequest.getFullName());

        if (profileRequest.getAvatar() != null) {
            profile.setAvatar(new Binary(Base64.getMimeDecoder().decode(profileRequest.getAvatar())));
        } else {
            profile.setAvatar(null);
        }

        profile.setDateOfBirth(profileRequest.getDateOfBirth());
        profile.setGender(profileRequest.isGender());
        profile.setPhoneNumber(profileRequest.getPhoneNumber());
        profile.setIdentification(profileRequest.getIdentification());
        profile.setEmail(profileRequest.getEmail());

        if (profileRequest.getProvince() == null || profileRequest.getProvince().isBlank()) {
            profile.setProvince(null);
        } else {
            Subnational province = subnationalRepository.findById(profileRequest.getProvince())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", profileRequest.getProvince()));
            profile.setProvince(province);
        }

        if (profileRequest.getDistrict() == null || profileRequest.getDistrict().isBlank()) {
            profile.setDistrict(null);
        } else {
            Subnational district = subnationalRepository.findById(profileRequest.getDistrict())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", profileRequest.getDistrict()));
            profile.setDistrict(district);
        }

        if (profileRequest.getWard() == null || profileRequest.getWard().isBlank()) {
            profile.setWard(null);
        } else {
            Subnational ward = subnationalRepository.findById(profileRequest.getWard())
                    .orElseThrow(() -> new ResourceNotFoundException("Subnational", "id", profileRequest.getWard()));
            profile.setWard(ward);
        }

        profile.setAddress(profileRequest.getAddress());
        profile.setHealthInsuranceNumber(profileRequest.getHealthInsuranceNumber());

        if (profileRequest.getNationality() == null || profileRequest.getNationality().isBlank()) {
            profile.setNationality(null);
        } else {
            Nationality nationality = nationalityRepository.findById(profileRequest.getNationality())
                    .orElseThrow(() -> new ResourceNotFoundException("Nationality", "id", profileRequest.getNationality()));
            profile.setNationality(nationality);
        }

        if (profileRequest.getEthnic() == null || profileRequest.getEthnic().isBlank()) {
            profile.setEthnic(null);
        } else {
            Ethnic ethnic = ethnicRepository.findById(profileRequest.getEthnic())
                    .orElseThrow(() -> new ResourceNotFoundException("Ethnic", "id", profileRequest.getEthnic()));
            profile.setEthnic(ethnic);
        }

        profile.setReligion(profileRequest.getReligion());
        profile.setOccupation(profileRequest.getOccupation());

        byte[] qrCode = generateQRCode(profile.getIdentification());
        if (qrCode != null) {
            profile.setQrCode(new Binary(BsonBinarySubType.BINARY, qrCode));
        }

        profileRepository.save(profile);

        user.setActive(true);
        userRepository.save(user);

        ProfileResponse profileResponse = modelMapper.map(profile, ProfileResponse.class);
        return new ResponseEntity<>(profileResponse, HttpStatus.OK);
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
