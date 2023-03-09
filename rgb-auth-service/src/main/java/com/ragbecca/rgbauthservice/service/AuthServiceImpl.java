package com.ragbecca.rgbauthservice.service;

import com.ragbecca.rgbauthservice.exception.TokenRefreshException;
import com.ragbecca.rgbauthservice.models.AuthProvider;
import com.ragbecca.rgbauthservice.models.EmailVerificationRequest;
import com.ragbecca.rgbauthservice.models.RefreshToken;
import com.ragbecca.rgbauthservice.models.User;
import com.ragbecca.rgbauthservice.payload.*;
import com.ragbecca.rgbauthservice.repository.UserRepository;
import com.ragbecca.rgbauthservice.security.JwtTokenUtil;
import com.ragbecca.rgbauthservice.security.RGBUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

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
    private WebClient.Builder webClient;

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        final User user = userRepository.findByUsername(loginRequest.getEmail()).get();
        if (!user.isEmailVerified()) {
            return ResponseEntity.accepted().body(new ApiResponse(false,
                    "Email is not verified yet. Please go to your email!"));
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.accepted().body(new JwtResponse(jwtTokenUtil.generateToken(userDetails)
                , refreshToken.getToken(), user.getId(),
                userDetails.getUsername(), user.getName(),
                user.getImageUrl(), roles));
    }

    @Override
    public ResponseEntity<?> signup(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Email already in use!"));
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getEmail(),
                AuthProvider.local, passwordEncoder.encode(signUpRequest.getPassword()), "USER");

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("api/userInfo/user/me")
                .buildAndExpand(result.getId()).toUri();

        EmailVerificationRequest emailVerificationRequest = new EmailVerificationRequest(
                result.getId(),
                result.getName(),
                "http://localhost:3000/user/verify?id=" + result.getId(),
                result.getUsername()
        );

        // Send mail request to mail server
        webClient.build().post().uri("http://localhost:8093/api/mail/send")
                .body(BodyInserters.fromValue(emailVerificationRequest))
                .exchange().block();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully!"));
    }

    @Override
    public ResponseEntity<?> refreshTokenCheck(RefreshTokenRequest refreshTokenRequest) {
        String requestRefreshToken = refreshTokenRequest.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenUtil.generateTokenFromUsername(user.getUsername());
                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
                    return ResponseEntity.ok(new JwtResponse(token, refreshToken.getToken(), user.getId(),
                            user.getUsername(), user.getName(), user.getImageUrl(), Collections.singletonList(user.getRole())));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @Override
    public ResponseEntity<?> createTokenOAuth2(OAuth2TokenRequest oAuth2TokenRequest) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(oAuth2TokenRequest.getUsername());
        final User user = userRepository.findByUsername(oAuth2TokenRequest.getUsername()).get();

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok(new JwtResponse(oAuth2TokenRequest.getToken(), refreshToken.getToken(), user.getId(),
                userDetails.getUsername(), user.getName(), user.getImageUrl(), roles));
    }

    @Override
    public ResponseEntity<?> logout(String username) {
        // Removal of common bug
        if (username.startsWith("\"")) {
            username = username.replace("\"", "");
        }
        if (userRepository.findByUsername(username).isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "You are not logged in"));
        }

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "User logout successfully!"));
    }

    @Override
    public ResponseEntity<?> verifyUser(String id) {
        Long idParsed = Long.parseLong(id);
        if (!userRepository.existsById(idParsed)) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "The id you are using isn't valid"));
        }
        User user = userRepository.findById(idParsed).get();
        if (user.isEmailVerified()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "You are already verified!"));
        }
        user.setEmailVerified(true);
        userRepository.save(user);
        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Email Verified!"));

    }
}
