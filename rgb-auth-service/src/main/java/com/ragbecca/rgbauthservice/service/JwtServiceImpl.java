package com.ragbecca.rgbauthservice.service;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    private RSAKey rsaKey;

    @Override
    public JwtDecoder getJwtDecoder() {
        return jwtDecoder;
    }

    @Override
    public Map<String, Object> getJWKSet() {
        return new JWKSet(rsaKey).toJSONObject();
    }
}
