package com.alexsitiy.ideas.project.util;

import com.alexsitiy.ideas.project.config.JwtTokenProperties;
import com.alexsitiy.ideas.project.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private static final String CLAIM_USERNAME = "username";

    private final JwtTokenProperties jwtProperties;

    public String generateToken(User user) {
        return JWT.create()
                .withSubject(jwtProperties.getSubject())
                .withClaim(CLAIM_USERNAME, user.getUsername())
                .withIssuer(jwtProperties.getIssuer())
                .withIssuedAt(Instant.now())
                .withExpiresAt(ZonedDateTime.now().plusMinutes(jwtProperties.getLongevity()).toInstant())
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }
}
