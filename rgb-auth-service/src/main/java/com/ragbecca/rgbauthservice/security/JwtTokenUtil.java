package com.ragbecca.rgbauthservice.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@Component
@Configuration
public class JwtTokenUtil implements Serializable {

    @Serial
    private static final long serialVersionUID = -2550185165626007488L;
    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration.minutes}")
    @Getter
    private int expiration;

    @Value("${app.jwt.issuer}")
    private String issuer;

    //Retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //Retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        Map<String, Object> decodedClaims = jwtDecoder().decode(token).getClaims();
        Claims claims = new DefaultClaims();

        claims.setExpiration(dateParserToUTC(decodedClaims.get("exp").toString()));
        claims.setIssuedAt(dateParserToUTC(decodedClaims.get("iat").toString()));
        claims.setSubject(decodedClaims.get("sub").toString());
        claims.setIssuer(decodedClaims.get("iss").toString());
        return claims;
    }

    private Date dateParserToUTC(String dateStringGotten) {
        String[] dateString = dateSplitter(dateStringGotten);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Integer.parseInt(dateString[0]),
                Integer.parseInt(dateString[1]),
                Integer.parseInt(dateString[2]),
                Integer.parseInt(dateString[3]),
                Integer.parseInt(dateString[4]),
                Integer.parseInt(dateString[5]));
        return calendar.getTime();
    }


    private String[] dateSplitter(String date) {
        String[] newDateAndTime = new String[6];
        String[] splitDateTime = date.split("T");
        String[] splitTime = splitDateTime[1].split(":");
        String[] splitDate = splitDateTime[0].split("-");
        newDateAndTime[0] = splitDate[0];
        newDateAndTime[1] = splitDate[1];
        newDateAndTime[2] = splitDate[2];
        newDateAndTime[3] = splitTime[0];
        newDateAndTime[4] = splitTime[1];
        newDateAndTime[5] = splitDate[2].replace("Z", "");
        return newDateAndTime;
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateTokenFromUsername(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuer(issuer)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + (long) expiration * 60 * 1000))
                    .signWith(rsaKeyPair().toPrivateKey(), SignatureAlgorithm.RS512).compact();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        try {
            return NimbusJwtDecoder.withPublicKey(rsaKeyPair().toRSAPublicKey())
                    .signatureAlgorithm(org.springframework.security.oauth2.jose.jws.SignatureAlgorithm.RS512).build();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public RSAKey rsaKeyPair() {
        try {
            return new RSAKeyGenerator(5120)
                    .keyUse(KeyUse.SIGNATURE)
                    .keyID(UUID.randomUUID().toString())
                    .generate();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
