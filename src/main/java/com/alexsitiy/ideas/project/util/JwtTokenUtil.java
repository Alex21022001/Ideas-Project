package com.alexsitiy.ideas.project.util;

import com.alexsitiy.ideas.project.config.JwtTokenProperties;
import com.alexsitiy.ideas.project.security.SecurityUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private static final String CLAIM_USERNAME = "username";

    private final JwtTokenProperties jwtProperties;

    public String generateToken(SecurityUser user) {
        return JWT.create()
                .withSubject(jwtProperties.getSubject())
                .withClaim(CLAIM_USERNAME, user.getUsername())
                .withIssuer(jwtProperties.getIssuer())
                .withIssuedAt(Instant.now())
                .withExpiresAt(ZonedDateTime.now().plusMinutes(jwtProperties.getLongevity()).toInstant())
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }

    public String validateAndExtractUsername(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtProperties.getSecret()))
                .withSubject(jwtProperties.getSubject())
                .withIssuer(jwtProperties.getIssuer())
                .build();

        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT.getClaim(CLAIM_USERNAME).asString();
    }
}
