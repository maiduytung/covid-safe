package com.covidsafe.security.services.impl;

import com.covidsafe.exceptions.BadRequestException;
import com.covidsafe.exceptions.ResourceNotFoundException;
import com.covidsafe.models.RefreshToken;
import com.covidsafe.models.Role;
import com.covidsafe.models.User;
import com.covidsafe.models.Verification;
import com.covidsafe.payload.request.LoginRequest;
import com.covidsafe.payload.request.LogoutRequest;
import com.covidsafe.payload.request.RefreshTokenRequest;
import com.covidsafe.payload.request.RegisterRequest;
import com.covidsafe.payload.response.JwtResponse;
import com.covidsafe.payload.response.RefreshTokenResponse;
import com.covidsafe.repository.RefreshTokenRepository;
import com.covidsafe.repository.RoleRepository;
import com.covidsafe.repository.UserRepository;
import com.covidsafe.repository.VerificationRepository;
import com.covidsafe.security.jwt.JwtUtil;
import com.covidsafe.security.jwt.UserPrincipal;
import com.covidsafe.security.services.AuthService;
import com.covidsafe.utils.RoleType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    VerificationRepository verificationRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        Verification verification = verificationRepository.findById(registerRequest.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Verification", "DeviceId", registerRequest.getDeviceId()));
        String verificationKey = verification.getVerificationKey();

        if (!registerRequest.getVerificationKey().equals(verificationKey)) {
            throw new BadRequestException("Invalid verification key!");
        }

        //verificationService.deleteVerificationById(registerRequest.getDeviceId());

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "Role name", RoleType.ROLE_USER));
        roles.add(userRole);

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        user.setRoles(roles);

        userRepository.save(user);

        return authenticateUser(new LoginRequest(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getDeviceId()));
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String accessToken = jwtUtil.generateToken(userPrincipal.getId());
        String refreshToken = generateRefreshToken(loginRequest.getDeviceId(), userPrincipal.getId());

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setTokenType("Bearer");
        jwtResponse.setAccessToken(accessToken);
        jwtResponse.setExpiresIn(360000);
        jwtResponse.setRefreshToken(refreshToken);

        return ResponseEntity.ok(jwtResponse);
    }

    private String generateRefreshToken(String deviceId, String username) {
        String rdmToken = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(deviceId, username, rdmToken);
        refreshTokenRepository.save(refreshToken);

        return rdmToken;
    }

    @Override
    public ResponseEntity<?> refresh(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenRequest.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException("RefreshToken", "deviceId", refreshTokenRequest.getDeviceId()));

        if (!refreshTokenRequest.getRefreshToken().equals(refreshToken.getRefreshToken())) {
            throw new BadRequestException("Invalid refresh token!");
        }

        String accessToken = jwtUtil.generateToken(refreshToken.getUserId());

        refreshToken.setExpireAt(Date.from(Instant.now().plus(30, ChronoUnit.DAYS)));
        refreshTokenRepository.save(refreshToken);

        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse();
        refreshTokenResponse.setTokenType("Bearer");
        refreshTokenResponse.setAccessToken(accessToken);
        refreshTokenResponse.setExpiresIn(360000);

        return ResponseEntity.ok(refreshTokenResponse);
    }

    @Override
    public ResponseEntity<?> logout(LogoutRequest logoutRequest) {
        RefreshToken refreshToken = refreshTokenRepository.findById(logoutRequest.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException("RefreshToken", "deviceId", logoutRequest.getDeviceId()));
        refreshTokenRepository.delete(refreshToken);

        return ResponseEntity.ok(null);
    }
}
