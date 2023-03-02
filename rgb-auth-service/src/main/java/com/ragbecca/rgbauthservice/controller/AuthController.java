package com.ragbecca.rgbauthservice.controller;

import com.ragbecca.rgbauthservice.payload.LoginRequest;
import com.ragbecca.rgbauthservice.payload.OAuth2TokenRequest;
import com.ragbecca.rgbauthservice.payload.RefreshTokenRequest;
import com.ragbecca.rgbauthservice.payload.SignUpRequest;
import com.ragbecca.rgbauthservice.service.AuthService;
import com.ragbecca.rgbauthservice.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.signup(signUpRequest);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshTokenCheck(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refreshTokenCheck(request);
    }

    @PostMapping("/oauth2/createToken")
    public ResponseEntity<?> createToken(@Valid @RequestBody OAuth2TokenRequest oAuth2TokenRequest) {
        return authService.createTokenOAuth2(oAuth2TokenRequest);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody String username) {
        return authService.logout(username);
    }

    @GetMapping("/issuer")
    public JwtDecoder jwtDecoder() {
        return jwtService.getJwtDecoder();
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> jwkKeySet() {
        return jwtService.getJWKSet();
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam String id) {
        return authService.verifyUser(id);
    }

}
