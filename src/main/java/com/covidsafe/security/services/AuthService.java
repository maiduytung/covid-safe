package com.covidsafe.security.services;

import com.covidsafe.payload.request.RefreshTokenRequest;
import com.covidsafe.payload.request.LoginRequest;
import com.covidsafe.payload.request.LogoutRequest;
import com.covidsafe.payload.request.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);

    ResponseEntity<?> registerUser(RegisterRequest registerRequest);

    ResponseEntity<?> refresh(RefreshTokenRequest refreshTokenRequest);

    ResponseEntity<?> logout(LogoutRequest logoutRequest);

}
