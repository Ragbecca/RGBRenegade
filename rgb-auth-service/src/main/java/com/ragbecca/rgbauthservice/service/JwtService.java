package com.ragbecca.rgbauthservice.service;

import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.Map;

public interface JwtService {

    JwtDecoder getJwtDecoder();

    Map<String, Object> getJWKSet();

}
