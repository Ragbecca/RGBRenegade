package com.ragbecca.renegadeclient.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.ragbecca.renegadeclient.exception.BadRequestException;
import com.ragbecca.renegadeclient.exception.TokenRefreshException;
import com.ragbecca.renegadeclient.models.AuthProvider;
import com.ragbecca.renegadeclient.models.RefreshToken;
import com.ragbecca.renegadeclient.models.User;
import com.ragbecca.renegadeclient.payload.request.LoginRequest;
import com.ragbecca.renegadeclient.payload.request.OAuth2TokenRequest;
import com.ragbecca.renegadeclient.payload.request.SignUpRequest;
import com.ragbecca.renegadeclient.payload.request.TokenRefreshRequest;
import com.ragbecca.renegadeclient.payload.response.ApiResponse;
import com.ragbecca.renegadeclient.payload.response.JwtResponse;
import com.ragbecca.renegadeclient.repository.UserRepository;
import com.ragbecca.renegadeclient.security.JwtTokenUtil;
import com.ragbecca.renegadeclient.security.RGBUserDetailsService;
import com.ragbecca.renegadeclient.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private RGBUserDetailsService userDetailsService;
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    private RSAKey rsaKey;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        final User user = userRepository.findByUsername(loginRequest.getEmail()).get();

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), user.getId(),
                userDetails.getUsername(), user.getName(), user.getImageUrl(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setUsername(signUpRequest.getEmail());
        user.setProvider(AuthProvider.local);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole("USER");

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully!"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenUtil.generateTokenFromUsername(user.getUsername());
                    refreshTokenService.deleteByUserId(user.getId());
                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
                    return ResponseEntity.ok(new JwtResponse(token, refreshToken.getToken(), user.getId(),
                            user.getUsername(), user.getName(), user.getImageUrl(), Collections.singletonList(user.getRole())));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/oauth2/createToken")
    public ResponseEntity<?> createToken(@Valid @RequestBody OAuth2TokenRequest oAuth2TokenRequest) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(oAuth2TokenRequest.getUsername());
        final User user = userRepository.findByUsername(oAuth2TokenRequest.getUsername()).get();

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok(new JwtResponse(oAuth2TokenRequest.getToken(), refreshToken.getToken(), user.getId(),
                userDetails.getUsername(), user.getName(), user.getImageUrl(), roles));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody String username) {
        if (username.startsWith("\"")) {
            username = username.replace("\"", "");
        }
        if (userRepository.findByUsername(username).isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "You are not logged in"));
        }

        User user = userRepository.findByUsername(username).get();
        refreshTokenService.deleteByUserId(user.getId());
        return ResponseEntity.ok()
                .body(new ApiResponse(true, "User logout successfully!"));
    }

    @GetMapping("/issuer")
    public JwtDecoder getJWTDecoder() {
        return jwtDecoder;
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> keys() {
        return new JWKSet(rsaKey).toJSONObject();
    }

}
