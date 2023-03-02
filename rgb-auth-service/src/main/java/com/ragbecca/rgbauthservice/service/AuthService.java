package com.ragbecca.rgbauthservice.service;

import com.ragbecca.rgbauthservice.payload.LoginRequest;
import com.ragbecca.rgbauthservice.payload.OAuth2TokenRequest;
import com.ragbecca.rgbauthservice.payload.RefreshTokenRequest;
import com.ragbecca.rgbauthservice.payload.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> login(LoginRequest loginRequest);

    ResponseEntity<?> signup(SignUpRequest signUpRequest);

    ResponseEntity<?> refreshTokenCheck(RefreshTokenRequest refreshTokenRequest);

    ResponseEntity<?> createTokenOAuth2(OAuth2TokenRequest oAuth2TokenRequest);

    ResponseEntity<?> logout(String username);

    ResponseEntity<?> verifyUser(String id);

}
